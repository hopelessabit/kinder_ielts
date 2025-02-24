package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.StudyScheduleStatus;
import com.kinder.kinder_ielts.entity.StudySchedule;

import java.util.Set;

/**
 * Service interface for managing {@link StudySchedule} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseStudyScheduleService extends BaseEntityService<StudySchedule, String> {
    Set<StudySchedule> findByClassId(String classId, IsDelete isDelete, StudyScheduleStatus status);
}