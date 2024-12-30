package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.CourseLevel;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.exception.SqlException;

import java.util.List;

/**
 * Service interface for managing {@link CourseLevel} entities.
 * Provides methods for CRUD operations, retrieval, and logical deletion.
 */
public interface BaseCourseLevelService extends BaseEntityService<CourseLevel, String>{
}
