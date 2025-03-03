package com.kinder.kinder_ielts.dto.response.roll_call;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.RollCallStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.student.StudentResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.RollCall;
import lombok.Getter;

@Getter
public class RollCallResponse {
    public final String studentId;
    public final String studyScheduleId;
    public final StudentResponse student;
    public final StudyScheduleResponse studySchedule;
    public final StatusResponse<RollCallStatus> status;
    public final String note;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public BaseEntityResponse extendDetail;

    public RollCallResponse(RollCall rollCall, boolean includeForAdmin, boolean includeStudySchedule) {
        this.studentId = rollCall.getId().getStudentId();
        this.studyScheduleId = rollCall.getId().getStudyScheduleId();
        this.note = rollCall.getNote();
        this.student = StudentResponse.info(rollCall.getStudent());
        this.studySchedule = includeStudySchedule ? StudyScheduleResponse.info(rollCall.getStudySchedule()) : null;
        this.status = StatusResponse.from(rollCall.getStatus());
        this.extendDetail = includeForAdmin ? BaseEntityResponse.from(rollCall) : null;
    }

    public static RollCallResponse info(RollCall rollCall) {
        return new RollCallResponse(rollCall, false, false);
    }

    public static RollCallResponse detail(RollCall rollCall) {
        return new RollCallResponse(rollCall, false, true);
    }

    public static RollCallResponse infoWithExtendDetail(RollCall rollCall) {
        return new RollCallResponse(rollCall, true, false);
    }

    public static RollCallResponse detailWithExtendDetail(RollCall rollCall) {
        return new RollCallResponse(rollCall, true, true);
    }
}
