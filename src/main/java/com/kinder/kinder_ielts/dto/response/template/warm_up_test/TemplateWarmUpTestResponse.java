package com.kinder.kinder_ielts.dto.response.template.warm_up_test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.template.study_schedule.TemplateStudyScheduleResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateWarmUpTest;
import lombok.Getter;

@Getter
public class TemplateWarmUpTestResponse {
    private String id;
    private String title;
    private String description;
    private String link;
    private StatusResponse<ViewStatus> status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TemplateStudyScheduleResponse studyScheduleTemplate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public TemplateWarmUpTestResponse(TemplateWarmUpTest templateWarmUpTest, boolean includeInfoForAdmin, boolean includeStudySchedule) {
        this.id = templateWarmUpTest.getId();
        this.title = templateWarmUpTest.getTitle();
        this.description = templateWarmUpTest.getDescription();
        this.link = templateWarmUpTest.getLink();
        this.status = StatusResponse.from(templateWarmUpTest.getStatus());

        if (includeStudySchedule)
            this.studyScheduleTemplate = TemplateStudyScheduleResponse.info(templateWarmUpTest.getTemplateStudySchedule());

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(templateWarmUpTest);
    }

    public static TemplateWarmUpTestResponse infoWithStudySchedule(TemplateWarmUpTest templateWarmUpTest) {
        return new TemplateWarmUpTestResponse(templateWarmUpTest, false, true);
    }

    public static TemplateWarmUpTestResponse info(TemplateWarmUpTest templateWarmUpTest) {
        return new TemplateWarmUpTestResponse(templateWarmUpTest, false, false);
    }

    public static TemplateWarmUpTestResponse detailWithStudySchedule(TemplateWarmUpTest templateWarmUpTest) {
        return new TemplateWarmUpTestResponse(templateWarmUpTest, true, true);
    }

    public static TemplateWarmUpTestResponse detail(TemplateWarmUpTest templateWarmUpTest) {
        return new TemplateWarmUpTestResponse(templateWarmUpTest, true, false);
    }
}
