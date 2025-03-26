package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.StudySchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StudyScheduleRepository extends BaseEntityRepository<StudySchedule, String> {
    List<StudySchedule> findByIdInAndIsDeletedIn(List<String> ids, List<IsDelete> isDeletes);

    Optional<StudySchedule> findByIdAndIsDeleted(String id, IsDelete findDeleted);

    Set<StudySchedule> findByClassroom_IdAndIsDeletedAndStatus(String id, IsDelete isDeleted, ViewStatus status);

    Set<StudySchedule> findByClassroom_IdAndIsDeletedOrderByFromTimeAsc(String id, IsDelete isDeleted); //    List<StudySchedule> findById_ClassIdAndIsDeleted(String classId, IsDelete isDelete);

    Set<StudySchedule> findByClassroom_IdAndIsDeletedAndStatusIn(String id, IsDelete isDeleted, Collection<ViewStatus> statuses);

    Set<StudySchedule> findByClassroom_IdAndIsDeletedAndStatusInOrderByFromTimeAsc(String id, IsDelete isDeleted, Collection<ViewStatus> statuses);

    @Query(value = """
SELECT ss
FROM study_schedule ss
join fetch ss.classroomLinks cl
join fetch ss.warmUpTests wt
join fetch ss.homework hw
join fetch ss.studyMaterials sm
WHERE ss.classId = :classroomId
  AND ss.isDeleted = :isDeleted
  AND ss.status = :status
""")
    List<StudySchedule> findByClassroomIdForStudent(String classroomId, String studentId, IsDelete isDelete, ViewStatus viewStatus);
}
