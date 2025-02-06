package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.RollCall;
import com.kinder.kinder_ielts.entity.id.RollCallId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BaseRollCallService extends BaseEntityService<RollCall, RollCallId> {

    List<RollCall> findByStudyScheduleId(String studyScheduleId, IsDelete isDelete, String failMessage);

    List<RollCall> findByStudentIdAndClassId(String studentId, String classId, IsDelete isDelete, String failMessage);

    List<RollCall> findByStudentIdAndStudyScheduleIds(String studentId, List<String> studyScheduleIds, IsDelete isDelete, String failMessage);

    Page<RollCall> get(Specification<RollCall> rollCallSpecification, Pageable pageable);
}
