package com.kinder.kinder_ielts.dto.response.study_schedule;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.response.classroom_link.ClassroomLinkResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.dto.response.study_material.StudyMaterialResponse;
import com.kinder.kinder_ielts.dto.response.warm_up_test.WarmUpTestResponse;
import com.kinder.kinder_ielts.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudyScheduleResponse {
    private String id;
    private ZonedDateTime dateTime;
    private String title;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudyScheduleDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public StudyScheduleResponse(StudySchedule studySchedule, boolean includeInfoForAdmin, boolean includeDetail) {
        this.id = studySchedule.getId();
        this.dateTime = studySchedule.getDateTime();
        this.title = studySchedule.getTitle();
        this.description = studySchedule.getDescription();

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
