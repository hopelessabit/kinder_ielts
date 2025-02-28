package com.kinder.kinder_ielts.service.implement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.id.CourseStudentId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseClassroomService;
import com.kinder.kinder_ielts.service.base.BaseClassroomStudentService;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.base.BaseCourseStudentService;
import com.kinder.kinder_ielts.service.base.BaseStudentService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import com.kinder.kinder_ielts.util.name.NameParts;
import com.kinder.kinder_ielts.util.name.NameUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

        if (studentPage.getContent().isEmpty())
            return studentPage.map(StudentResponse::info);

        List<String> studentIds = studentPage.getContent().stream().map(Student::getId).toList();

        List<CourseStudent> courseStudents = baseCourseStudentService.getByStudentIds(studentIds);
        List<Course> courses = new ArrayList<>();
        if (!courseStudents.isEmpty())
            courses = baseCourseService.get(courseStudents.stream().map(CourseStudent::getId).map(CourseStudentId::getCourseId).distinct().toList(), List.of(IsDelete.NOT_DELETED), CourseMessage.NOT_FOUND);
        List<ClassroomStudent> classroomStudents = baseClassroomStudentService.getByStudentIds(studentIds);
        List<Classroom> classrooms = new ArrayList<>();
        if (!classroomStudents.isEmpty())
            classrooms = baseClassroomService.get(classroomStudents.stream().map(ClassroomStudent::getId).map(ClassStudentId::getClassId).distinct().toList(), List.of(IsDelete.NOT_DELETED), ClassroomMessage.NOT_FOUND);

        if (courses.isEmpty())
            return studentPage.map(student -> StudentResponse.withCourses(student, new ArrayList<>()));
        if (courseStudents.isEmpty())
            return studentPage.map(student -> StudentResponse.withCourses(student, new ArrayList<>()));

        List<StudentResponse> studentResponses = new ArrayList<>();
        if (classrooms.isEmpty()){
            for (Student student: studentPage.getContent()){
                List<CourseStudent> courseStudents1 = courseStudents.stream().filter(cs -> cs.getStudent().getId().equals(student.getId())).toList();
                if (courseStudents1.isEmpty()){
                    studentResponses.add(StudentResponse.withCourses(student, new ArrayList<>()));
                    continue;
                }
                List<String> courseIds1 = courseStudents1.stream().map(cs -> cs.getCourse().getId()).toList();
                List<Course> courses1 = courses.stream().filter(course -> courseIds1.stream().anyMatch(cid1 -> cid1.equals(course.getId()))).toList();
                studentResponses.add(StudentResponse.withCourses(student, courses1));
            }
            return new PageImpl<>(studentResponses, pageable, studentPage.getTotalElements());
        }

        for (Student student: studentPage.getContent()){
            List<ClassroomStudent> classroomStudents1 = classroomStudents.stream().filter(cs -> cs.getStudent().getId().equals(student.getId())).toList();

            if (classroomStudents1.isEmpty()){
                List<CourseStudent> courseStudents1 = courseStudents.stream().filter(cs -> cs.getStudent().getId().equals(student.getId())).toList();
                List<String> courseIds1 = courseStudents1.stream().map(cs -> cs.getCourse().getId()).toList();
                List<Course> courses1 = courses.stream().filter(course -> courseIds1.stream().anyMatch(cs -> cs.equals(course.getId()))).toList();

                studentResponses.add(StudentResponse.withCourses(student, courses1));
                continue;
            }

            List<String> classroomIds1 = classroomStudents1.stream().map(ClassroomStudent::getId).map(ClassStudentId::getClassId).toList();
            List<Classroom> classrooms1 = classrooms.stream().filter(classroom -> classroomIds1.stream().anyMatch(cid1 -> cid1.equals(classroom.getId()))).toList();

            List<CourseStudent> courseStudents1 = courseStudents.stream().filter(cs -> cs.getStudent().getId().equals(student.getId())).toList();
            List<String> courseIds1 = courseStudents1.stream().map(cs -> cs.getCourse().getId()).toList();
            List<Course> courses1 = courses.stream().filter(course -> courseIds1.stream().anyMatch(cs -> cs.equals(course.getId()))).toList();
            for (Course course : courses1){
                List<Classroom> classrooms2 = classrooms1.stream().filter(classroom -> classroom.getCourseId().equals(course.getId())).toList();
                course.setClassrooms(classrooms2);
            }

            studentResponses.add(StudentResponse.withCourses(student, courses1));
        }

        return new PageImpl<>(studentResponses, pageable, studentPage.getTotalElements());
    }

    public List<StudentResponse> mapStudentResponse(Page<Student> studentPage, List<CourseStudent> courseStudents, List<Course> courses) {
        List<StudentResponse> result = new ArrayList<>();
        for (Student student : studentPage.getContent()){
            List<CourseStudent> courseStudents1 = courseStudents.stream().filter(cs -> cs.getStudent().getId().equals(student.getId())).toList();
            if (courseStudents1.isEmpty()){
                result.add(StudentResponse.withCourses(student, new ArrayList<>()));
                continue;
            }
            List<Course> courses1 = courses.stream().filter(course -> courseStudents1.stream().anyMatch(cs -> cs.getCourse().getId().equals(course.getId()))).toList();
            result.add(StudentResponse.withCourses(student, courses1));
        }
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
            else if (classIds != null)
                addClassIds(classIds, criteriaBuilder, root, predicates, query);

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

        Join<Student, ClassroomStudent> classroomStudentJoin = root.join("classroomStudents");
        if (classIds.size() == 1)
            predicates.add(criteriaBuilder.equal(classroomStudentJoin.get("classroom").get("id"), classIds.get(0)));
        else
            predicates.add(classroomStudentJoin.get("classroom").get("id").in(classIds));
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
