package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.ClassroomTutorId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomTutorRepository extends BaseEntityRepository<ClassroomTutor, ClassroomTutorId> {
    List<ClassroomTutor> findById_ClassIdAndIsDeleted(String classId, IsDelete isDeleted);
}
