package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.id.StudentHomeworkId;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentHomeworkRepository extends BaseEntityRepository<StudentHomework, StudentHomeworkId> {
}
