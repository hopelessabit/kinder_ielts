package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.exception.SqlException;

import java.util.List;

/**
 * Service interface for managing {@link Course} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseCourseService extends BaseEntityService<Course, String> {
}