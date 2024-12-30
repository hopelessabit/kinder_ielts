package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.CourseTutorId;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTutorRepository extends BaseEntityRepository<CourseTutor, CourseTutorId> {
    List<CourseTutor> findById_CourseIdAndIsDeleted(String courseId, IsDelete isDeleted);
}
