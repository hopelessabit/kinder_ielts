package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.WarmUpTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarmUpTestRepository extends BaseEntityRepository<WarmUpTest, String> {
    @Query(value = """
SELECT wut.*
FROM warm_up_test wut
LEFT JOIN warm_up_test_student wuts ON wut.id = wuts.warm_up_test_id
WHERE study_schedule_id = :studyScheduleId
  AND wut.is_deleted = :isDeleted
  AND wut.view_status = :viewStatus
  AND (wut.privacy_status = 'PUBLIC' OR wuts.student_id = :studentId)
""", nativeQuery = true)
    List<WarmUpTest> findByStudyScheduleIdAndStudentIdAndIsDeleted(String studyScheduleId, String studentId, ViewStatus viewStatus, IsDelete isDelete);
}
