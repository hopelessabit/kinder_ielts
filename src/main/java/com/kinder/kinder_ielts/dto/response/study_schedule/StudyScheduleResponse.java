package com.kinder.kinder_ielts.dto.response.study_schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.StudyScheduleStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class StudyScheduleResponse {
    private String id;
    private Integer place;
    private ZonedDateTime fromTime;
    private ZonedDateTime toTime;
    private String title;
    private String description;
    private StatusResponse<StudyScheduleStatus> status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudyScheduleDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public StudyScheduleResponse(StudySchedule studySchedule, boolean includeInfoForAdmin, boolean includeDetail) {
        this.id = studySchedule.getId();
        this.place = studySchedule.getPlace();
        this.fromTime = studySchedule.getFromTime();
        this.toTime = studySchedule.getToTime();
        this.title = studySchedule.getTitle();
        this.description = studySchedule.getDescription();
        this.status = StatusResponse.from(studySchedule.getStatus());
        if (includeDetail)
            this.detailInfo = StudyScheduleDetailInfoResponse.from(studySchedule);

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(studySchedule);
    }

    public static StudyScheduleResponse infoWithDetail(StudySchedule studySchedule) {
        return new StudyScheduleResponse(studySchedule, false, true);
    }

    public static StudyScheduleResponse info(StudySchedule studySchedule) {
        return new StudyScheduleResponse(studySchedule, false, false);
    }

    public static StudyScheduleResponse detailWithDetail(StudySchedule studySchedule) {
        return new StudyScheduleResponse(studySchedule, true, true);
    }

    public static StudyScheduleResponse detail(StudySchedule studySchedule) {
        return new StudyScheduleResponse(studySchedule, true, false);
    }
}
