package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.ClassroomLink;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomLinkRepository extends BaseEntityRepository<ClassroomLink, String> {
    @Query(value = """
SELECT cl.*
FROM classroom_link cl
WHERE study_schedule_id = :id
  AND is_deleted = :isDeleted
""", nativeQuery = true)
    List<ClassroomLink> findByStudyScheduleIdAndIsDeleted(String id, IsDelete isDelete);

    @Query(value = """
SELECT cl.*
FROM classroom_link cl
WHERE study_schedule_id = :id
  AND view_status = :viewStatus
  AND is_deleted = :isDeleted
""", nativeQuery = true)
    List<ClassroomLink> findByStudyScheduleIdAndViewStatusAndIsDeleted(String studyScheduleId, ViewStatus viewStatus, IsDelete isDelete);
}
