package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.Homework;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Service interface for managing {@link Homework} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseHomeworkService extends BaseEntityService<Homework, String> {
    List<Homework> getByStudyScheduleIdAndStudentId(String studyScheduleId, String studentId, ViewStatus viewStatus, IsDelete isDelete, String notFound);
}