package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.StudySchedule;

import java.util.List;
import java.util.Set;

/**
 * Service interface for managing {@link StudySchedule} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseStudyScheduleService extends BaseEntityService<StudySchedule, String> {
    Set<StudySchedule> findByClassId(String classId, IsDelete isDelete, List<ViewStatus> statuses);
    Set<StudySchedule> findByClassIdWithViewStatus(String classId, IsDelete isDelete, ViewStatus status);

    List<StudySchedule> findByClassIdWithViewStatusForStudent(String classroomId, String studentId, IsDelete isDelete, ViewStatus viewStatus);
}