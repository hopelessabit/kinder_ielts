package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomStudentRepository extends BaseEntityRepository<ClassroomStudent, ClassStudentId> {
}
