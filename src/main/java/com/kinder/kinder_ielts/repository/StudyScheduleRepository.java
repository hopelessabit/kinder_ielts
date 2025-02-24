package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.StudySchedule;
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

    Set<StudySchedule> findByClassroom_IdAndIsDeleted(String id, IsDelete isDeleted); //    List<StudySchedule> findById_ClassIdAndIsDeleted(String classId, IsDelete isDelete);

    Set<StudySchedule> findByClassroom_IdAndIsDeletedAndStatusIn(String id, IsDelete isDeleted, Collection<ViewStatus> statuses);
}
