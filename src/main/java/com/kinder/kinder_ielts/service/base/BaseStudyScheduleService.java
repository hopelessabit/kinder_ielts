package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.RollCall;
import com.kinder.kinder_ielts.entity.StudySchedule;

import java.util.List;

/**
 * Service interface for managing {@link StudySchedule} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseStudyScheduleService extends BaseEntityService<StudySchedule, String> {
    List<StudySchedule> findByClassId(String classId, IsDelete isDelete, String failMessage);
}