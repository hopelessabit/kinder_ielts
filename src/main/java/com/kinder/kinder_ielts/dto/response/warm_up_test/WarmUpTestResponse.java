package com.kinder.kinder_ielts.dto.response.warm_up_test;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kinder.kinder_ielts.constant.WarmUpTestStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.WarmUpTest;
import lombok.Setter;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class WarmUpTestResponse {
    private String id;
    private String title;
    private String description;
    private String link;
    private StatusResponse<WarmUpTestStatus> status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudyScheduleResponse studySchedule;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public WarmUpTestResponse(WarmUpTest warmUpTest, boolean includeInfoForAdmin, boolean includeStudySchedule) {
        this.id = warmUpTest.getId();
        this.title = warmUpTest.getTitle();
        this.description = warmUpTest.getDescription();
        this.link = warmUpTest.getLink();
        this.status = StatusResponse.from(warmUpTest.getStatus());

        if (includeStudySchedule)
            this.studySchedule = StudyScheduleResponse.info(warmUpTest.getBeLongTo());

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(warmUpTest);
    }

    public static WarmUpTestResponse infoWithStudySchedule(WarmUpTest warmUpTest) {
        return new WarmUpTestResponse(warmUpTest, false, true);
    }

    public static WarmUpTestResponse info(WarmUpTest warmUpTest) {
        return new WarmUpTestResponse(warmUpTest, false, false);
    }

    public static WarmUpTestResponse detailWithStudySchedule(WarmUpTest warmUpTest) {
        return new WarmUpTestResponse(warmUpTest, true, true);
    }

    public static WarmUpTestResponse detail(WarmUpTest warmUpTest) {
        return new WarmUpTestResponse(warmUpTest, true, false);
    }
}
