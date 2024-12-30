package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.id.ClassroomTutorId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomTutorRepository extends BaseEntityRepository<ClassroomTutor, ClassroomTutorId> {
}
