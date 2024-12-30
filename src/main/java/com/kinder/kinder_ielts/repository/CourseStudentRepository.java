package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.id.CourseStudentId;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseStudentRepository extends BaseEntityRepository<CourseStudent, CourseStudentId> {
}
