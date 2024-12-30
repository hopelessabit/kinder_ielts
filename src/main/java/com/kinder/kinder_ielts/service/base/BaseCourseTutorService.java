package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.CourseTutorId;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;

import java.util.List;

/**
 * Service interface for managing {@link CourseTutor} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseCourseTutorService extends BaseEntityService<CourseTutor, CourseTutorId> {
    public List<CourseTutor> getByCourseId(String courseId, IsDelete isDelete);
}
