package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.RollCall;
import com.kinder.kinder_ielts.entity.id.RollCallId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RollCallRepository extends BaseEntityRepository<RollCall, RollCallId> {
    List<RollCall> findById_StudyScheduleIdAndIsDeleted(String studyScheduleId, IsDelete isDeleted);

    @Query( value = """
            select rc
                from roll_call rc
                inner join study_schedule ss on rc.id.studyScheduleId = ss.id
            where rc.id.studentId = :studentId
                and ss.classroom.id = :classId
                and rc.isDeleted = :isDeleted
            """)
    List<RollCall> findById_StudentIdAndId_ClassIdAndIsDeleted(String studentId, String classId, IsDelete isDelete);

    List<RollCall> findById_StudentIdAndId_StudyScheduleIdInAndIsDeleted(String studentId, Collection<String> studyScheduleIds, IsDelete isDeleted);

    Page<RollCall> findAll(Specification<RollCall> rollCallSpecification, Pageable pageable);
}
