package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyScheduleRepository extends BaseEntityRepository<StudySchedule, String> {
    List<StudySchedule> findByIdInAndIsDeletedIn(List<String> ids, List<IsDelete> isDeletes);

    Optional<StudySchedule> findByIdAndIsDeleted(String id, IsDelete findDeleted);
}
