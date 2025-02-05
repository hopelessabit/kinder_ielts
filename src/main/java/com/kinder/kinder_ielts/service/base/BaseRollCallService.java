package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.RollCall;
import com.kinder.kinder_ielts.entity.id.RollCallId;

import java.util.List;

public interface BaseRollCallService extends BaseEntityService<RollCall, RollCallId> {

    List<RollCall> findByStudyScheduleId(String studyScheduleId, IsDelete isDelete, String failMessage);

    List<RollCall> findByStudentIdAndClassId(String studentId, String classId, IsDelete isDelete, String failMessage);
}
