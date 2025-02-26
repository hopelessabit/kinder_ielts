package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.student.CreateStudentRequest;
import com.kinder.kinder_ielts.dto.request.student.UpdateStudentInfoRequest;
import com.kinder.kinder_ielts.dto.response.student.StudentResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import com.kinder.kinder_ielts.util.name.NameParts;
import com.kinder.kinder_ielts.util.name.NameUtil;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl {
    private final BaseStudentService baseStudentService;
    private final BaseAccountService baseAccountService;
    private final BaseCourseService baseCourseService;
    private final BaseClassroomService baseClassroomService;
    private final BaseCourseStudentService baseCourseStudentService;
    private final BaseClassroomStudentService baseClassroomStudentService;

    public StudentResponse createStudent(CreateStudentRequest request, String failMessage) {
        Account creator = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        Student student = ModelMapper.map(request, currentTime, creator);
        Account account = baseAccountService.create(student.getAccount(), failMessage);
        student.setAccount(account);
        baseStudentService.saveStudent(student);

        if (request.getAddToCourseId() != null ){
            Course course = baseCourseService.get(request.getAddToCourseId(), IsDelete.NOT_DELETED, CourseMessage.NOT_FOUND);
            if (request.getAddToClassroomId() == null){
                baseCourseStudentService.create(student, course, creator, currentTime);
            } else {
                baseCourseStudentService.create(student, course, creator, currentTime);
                Classroom classroom = baseClassroomService.get(request.getAddToClassroomId(), IsDelete.NOT_DELETED, ClassroomMessage.NOT_FOUND);
                baseClassroomStudentService.create(student, classroom, creator, currentTime);
            }
        }

        Student thisStudent = baseStudentService.get(student.getId(), IsDelete.NOT_DELETED, failMessage);
        return StudentResponse.detail(thisStudent);
    }

    public Page<StudentResponse> search(List<String> courseIds, List<String> classIds, String name, Pageable pageable) {
        Specification<Student> spec = createStudentSearchingspecification(courseIds, classIds, name);

        Page<Student> studentPage = baseStudentService.get(spec, pageable);

        List<String> studentIds = studentPage.getContent().stream().map(Student::getId).toList();

        List<ClassroomStudent> classroomStudents = baseClassroomStudentService.getByStudentIds(studentIds);
        List<String> belongToClassIds = classroomStudents.stream().map(cs -> cs.getClassroom().getId()).distinct().toList();

        List<Classroom> classrooms = baseClassroomService.get(belongToClassIds, List.of(IsDelete.NOT_DELETED), ClassroomMessage.NOT_FOUND);

        List<StudentResponse> studentResponses = new ArrayList<>();
        for (Student student: studentPage.getContent()){
            List<ClassroomStudent> classroomStudents1 = classroomStudents.stream().filter(cs -> cs.getStudent().getId().equals(student.getId())).toList();
            List<Classroom> belongToClasses = classrooms.stream().filter(classroom -> classroomStudents1.stream().anyMatch(cs -> cs.getClassroom().getId().equals(classroom.getId()))).toList();
            studentResponses.add(StudentResponse.withCourses(student, belongToClasses));
        }

        Page<StudentResponse> result = new PageImpl<>(studentResponses, pageable, studentPage.getTotalElements());
        return result;
    }

    private Specification<Student> createStudentSearchingspecification(List<String> courseIds, List<String> classIds, String name) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addDistinct(courseIds, classIds, query);

            if (courseIds != null && classIds != null)
                addClassIds(classIds, criteriaBuilder, root, predicates, query);
            else if (courseIds != null)
                addCourseIds(courseIds, criteriaBuilder, root, predicates, query);

            addStudentName(name, criteriaBuilder, root, predicates);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addStudentName(String name, CriteriaBuilder criteriaBuilder, Root<Student> root, List<Predicate> predicates) {
        if (name == null || name.trim().isEmpty())
            return;

        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + name.toLowerCase() + "%"));
    }

    private void addDistinct(List<String> courseIds, List<String> classIds, CriteriaQuery<?> query) {
        if ((courseIds != null && !courseIds.isEmpty() && classIds != null && !classIds.isEmpty()) && (courseIds.size() > 1 || classIds.size() > 1))
            query.distinct(true);
    }

    private void addCourseIds(List<String> courseIds, CriteriaBuilder criteriaBuilder, Root<Student> root, List<Predicate> predicates, CriteriaQuery<?> query) {
        if (courseIds.isEmpty())
            return;

        Join<Student, CourseStudent> courseStudentJoin = root.join("courseStudents");
        if (courseIds.size() == 1){
            predicates.add(criteriaBuilder.equal(courseStudentJoin.get("course").get("id"), courseIds.get(0)));
        } else {
            predicates.add(courseStudentJoin.get("course").get("id").in(courseIds));
        }
    }

    private void addClassIds(List<String> classIds, CriteriaBuilder criteriaBuilder, Root<Student> root, List<Predicate> predicates, CriteriaQuery<?> query) {
        if (classIds.isEmpty())
            return;

        Join<Student, CourseStudent> courseStudentJoin = root.join("courseStudents");
        if (classIds.size() == 1)
            predicates.add(criteriaBuilder.equal(courseStudentJoin.get("class").get("id"), classIds.get(0)));
        else
            predicates.add(courseStudentJoin.get("class").get("id").in(classIds));
    }

    public StudentResponse updateInfo(String studentId, UpdateStudentInfoRequest request, String failMessage) {
        Student student = baseStudentService.get(studentId, IsDelete.NOT_DELETED, failMessage);
        if (request.name != null){
            NameParts nameParts =  NameUtil.splitName(request.name);
            student.setFirstName(CompareUtil.compare(nameParts.firstName, student.getFirstName()));
            student.setMiddleName(CompareUtil.compare(nameParts.middleName, student.getMiddleName()));
            student.setLastName(CompareUtil.compare(nameParts.lastName, student.getLastName()));
            student.setFullName(CompareUtil.compare(request.name, student.getFullName()));
        }
        student.setEmail(CompareUtil.compare(request.email, student.getEmail()));
        student.setPhone(CompareUtil.compare(request.phone, student.getPhone()));
        student.setCitizenIdentification(CompareUtil.compare(request.citizenIdentification, student.getCitizenIdentification()));
        student.setDob(CompareUtil.compare(request.dob, student.getDob()));
        student.setCountry(CompareUtil.compare(request.country, student.getCountry()));
        return StudentResponse.info(baseStudentService.update(student, failMessage));
    }
}
