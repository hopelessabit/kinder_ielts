package com.kinder.kinder_ielts.dto.response.template.study_schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import lombok.Getter;

@Getter
public class TemplateStudyScheduleResponse {
    private final String id;
    private final String title;
    private final String description;
    private final Integer place;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TemplateStudyScheduleDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public TemplateStudyScheduleResponse(TemplateStudySchedule studySchedule, boolean includeInfoForAdmin, boolean includeDetail) {
        this.id = studySchedule.getId();
        this.title = studySchedule.getTitle();
        this.description = studySchedule.getDescription();
        this.place = studySchedule.getPlace();

        if (includeDetail)
            this.detailInfo = TemplateStudyScheduleDetailInfoResponse.from(studySchedule);

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(studySchedule);
    }

    public static TemplateStudyScheduleResponse infoWithDetail(TemplateStudySchedule studySchedule) {
        return new TemplateStudyScheduleResponse(studySchedule, false, true);
    }

    public static TemplateStudyScheduleResponse info(TemplateStudySchedule studySchedule) {
        return new TemplateStudyScheduleResponse(studySchedule, false, false);
    }

    public static TemplateStudyScheduleResponse detailWithDetail(TemplateStudySchedule studySchedule) {
        return new TemplateStudyScheduleResponse(studySchedule, true, true);
    }

    public static TemplateStudyScheduleResponse detail(TemplateStudySchedule studySchedule) {
        return new TemplateStudyScheduleResponse(studySchedule, true, false);
    }
}
