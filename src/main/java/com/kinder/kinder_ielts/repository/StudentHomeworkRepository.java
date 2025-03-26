package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.StudentHomeworkId;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentHomeworkRepository extends BaseEntityRepository<StudentHomework, StudentHomeworkId> {
    List<StudentHomework> findById_HomeworkIdAndIsDeleted(String homeworkId, IsDelete isDeleted);

    @Query("SELECT sh FROM student_homework sh WHERE sh.id.homeworkId = :homeworkId AND sh.isDeleted = :isDeleted")
    Page<StudentHomework> findByHomeworkIdAndIsDeleted(String homeworkId, IsDelete isDelete, Pageable pageable);

    List<StudentHomework> findById_HomeworkId(String homeworkId);
}
