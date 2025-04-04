package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.StudyMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyMaterialRepository extends BaseEntityRepository<StudyMaterial, String> {
    List<StudyMaterial> findByIdInAndIsDeletedIn(List<String> ids, List<IsDelete> isDeletes);

    Optional<StudyMaterial> findByIdAndIsDeleted(String id, IsDelete findDeleted);

    @Query(value = """
SELECT sm
FROM study_material sm
WHERE sm.study_schedule_id = :studyScheduleId
  AND sm.is_deleted = :isDeleted
""", nativeQuery = true)
    Page<StudyMaterial> findByStudyScheduleIdAndIsDeleted(String studyScheduleId, IsDelete isDelete, Pageable pageable);

    @Query(value = """
SELECT sm
FROM study_material sm
LEFT JOIN study_material_student sms ON sm.id = sms.study_material_id
WHERE sm.study_schedule_id = :studyScheduleId
  AND sm.is_deleted = :isDeleted
  AND (sm.privacy_status = 'PUBLIC' OR sms.student_id = :studentId)
""", nativeQuery = true)
    List<StudyMaterial> findByStudyScheduleIdAndStudentIdAndIsDeleted(String studyScheduleId, String studentId, IsDelete isDelete);

}
