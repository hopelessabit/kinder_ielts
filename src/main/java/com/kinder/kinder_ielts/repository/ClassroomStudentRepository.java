package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomStudentRepository extends BaseEntityRepository<ClassroomStudent, ClassStudentId> {
    @Query(value = """
            SELECT distinct cs.*
            FROM study_material sm 
            INNER JOIN study_schedule ss ON sm.study_schedule_id = ss.id
            INNER JOIN class_student cs ON ss.class_id = cs.class_id
            WHERE sm.id = :studyMaterialId
            AND cs.is_deleted = :isDelete
            """, nativeQuery = true)
    List<ClassroomStudent> findByStudyMaterialId(String studyMaterialId, IsDelete isDelete);
}
