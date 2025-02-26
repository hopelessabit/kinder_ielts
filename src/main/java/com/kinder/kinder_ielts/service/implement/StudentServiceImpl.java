package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.student.CreateStudentRequest;
import com.kinder.kinder_ielts.dto.response.student.StudentResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseStudentService;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl {
    private final BaseStudentService baseStudentService;
    private final BaseAccountService baseAccountService;

    public StudentResponse createStudent(CreateStudentRequest request, String failMessage) {
        Student student = ModelMapper.map(request);
        Account account = baseAccountService.create(student.getAccount(), failMessage);
        student.setAccount(account);
        baseStudentService.saveStudent(student);
        Student thisStudent = baseStudentService.get(student.getId(), IsDelete.NOT_DELETED, failMessage);
        return StudentResponse.detail(thisStudent);
    }

    public Page<StudentResponse> search(List<String> courseIds, List<String> classIds, Pageable pageable) {
        Specification<Student> spec = createStudentSearchingspecification(courseIds, classIds);

        Page<Student> studentPage = baseStudentService.get(spec, pageable);
        return studentPage.map(StudentResponse::info);
    }

    private Specification<Student> createStudentSearchingspecification(List<String> courseIds, List<String> classIds) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addDistinct(courseIds, classIds, query);
            addCourseIds(courseIds, criteriaBuilder, root, predicates, query);
            addClassIds(classIds, criteriaBuilder, root, predicates, query);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addDistinct(List<String> courseIds, List<String> classIds, CriteriaQuery<?> query) {
        if ((courseIds != null && !courseIds.isEmpty() && classIds != null && !classIds.isEmpty()) && (courseIds.size() > 1 || classIds.size() > 1))
            query.distinct(true);
    }

    private void addCourseIds(List<String> courseIds, CriteriaBuilder criteriaBuilder, Root<Student> root, List<Predicate> predicates, CriteriaQuery<?> query) {
        if (courseIds == null || courseIds.isEmpty())
            return;

        Join<Student, CourseStudent> courseStudentJoin = root.join("courseStudents");
        if (courseIds.size() == 1){
            predicates.add(criteriaBuilder.equal(courseStudentJoin.get("course").get("id"), courseIds.get(0)));
        } else {
            predicates.add(courseStudentJoin.get("course").get("id").in(courseIds));
        }
    }

    private void addClassIds(List<String> classIds, CriteriaBuilder criteriaBuilder, Root<Student> root, List<Predicate> predicates, CriteriaQuery<?> query) {
        if (classIds == null || classIds.isEmpty())
            return;

        Join<Student, CourseStudent> courseStudentJoin = root.join("courseStudents");
        if (classIds.size() == 1)
            predicates.add(criteriaBuilder.equal(courseStudentJoin.get("class").get("id"), classIds.get(0)));
        else
            predicates.add(courseStudentJoin.get("class").get("id").in(classIds));
    }
}
