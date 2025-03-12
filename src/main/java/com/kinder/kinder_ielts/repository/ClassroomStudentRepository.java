package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    @Query(value = """
            SELECT distinct cs.*
            FROM homework hw 
            INNER JOIN study_schedule ss ON hw.study_schedule_id = ss.id
            INNER JOIN class_student cs ON ss.class_id = cs.class_id
            WHERE hw.id = :studyMaterialId
            AND cs.is_deleted = :isDelete
            """, nativeQuery = true)
    List<ClassroomStudent> findByHomeworkId(String homeworkId, IsDelete isDelete);

    List<ClassroomStudent> findById_ClassIdAndIsDeleted(String classId, IsDelete isDeleted);

    List<ClassroomStudent> findById_StudentIdIn(List<String> studentIds);

    List<ClassroomStudent> findById_StudentIdInAndIsDeleted(Collection<String> studentIds, IsDelete isDeleted);

    List<ClassroomStudent> findById_StudentIdInAndId_ClassIdInAndIsDeleted(List<String> studentIds, List<String> classIds, IsDelete isDelete);

    List<ClassroomStudent> findDistinctById_StudentIdInAndClassroom_Course_IdInAndIsDeleted(Collection<String> studentIds, Collection<String> ids, IsDelete isDeleted);

    List<ClassroomStudent> findDistinctById_StudentIdInAndClassroom_CourseId(Collection<String> studentIds, String courseId);

    List<ClassroomStudent> findDistinctById_StudentIdInAndClassroom_CourseIdAndIsDeleted(Collection<String> studentIds, String courseId, IsDelete isDeleted);
}
