package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkRepository extends BaseEntityRepository<Homework, String> {
    List<Homework> findByIdInAndIsDeletedIn(List<String> ids, List<IsDelete> isDeletes);

    Optional<Homework> findByIdAndIsDeleted(String id, IsDelete findDeleted);

    @Query(value = """
SELECT hw.*
FROM homework hw
LEFT JOIN homework_student hws ON hw.id = hws.homework_id
WHERE study_schedule_id = :studyScheduleId
  AND hw.is_deleted = :isDeleted
  AND hw.view_status = :viewStatus
  AND (hw.privacy_status = 'PUBLIC' OR hws.student_id = :studentId)
""", nativeQuery = true)
    List<Homework> findByStudyScheduleIdAndStudentIdAndIsDeleted(String studyScheduleId, String studentId, ViewStatus viewStatus, IsDelete isDelete);
}
