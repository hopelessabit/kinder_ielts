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
                inner join study_schedule ss
            where rc.id.studentId = :studentId
                and ss.classroom.id = :classId
                and rc.isDeleted = :isDeleted
            group by rc
            """)
    List<RollCall> findById_StudentIdAndId_ClassIdAndIsDeleted(String studentId, String classId, IsDelete isDelete);

    List<RollCall> findById_StudentIdAndId_StudyScheduleIdInAndIsDeleted(String studentId, Collection<String> studyScheduleIds, IsDelete isDeleted);

    Page<RollCall> findAll(Specification<RollCall> rollCallSpecification, Pageable pageable);

    @Query(value = """
SELECT rc
FROM roll_call rc
INNER JOIN FETCH study_schedule ss
INNER JOIN FETCH student st
INNER JOIN class c on ss.classroom.id = c.id
WHERE c.id = :classroomId
AND rc.isDeleted = :isDeleted
""")
    List<RollCall> findByStudySchedule_Classroom_IdAndIsDeleted(String classroomId, IsDelete isDeleted);

    List<RollCall> findByStudySchedule_Classroom_IdAndIsDeletedOrderByStudySchedule_FromTimeAsc(String id, IsDelete isDeleted);
}
