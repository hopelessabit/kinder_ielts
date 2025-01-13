package com.kinder.kinder_ielts.dto.response.template.classroom_link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.ClassroomLinkStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.template.study_schedule.TemplateStudyScheduleResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroomLink;
import lombok.Getter;

@Getter
public class TemplateClassroomLinkResponse {
    private String id;
    private String title;
    private String description;
    private String link;
    private StatusResponse<ClassroomLinkStatus> status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TemplateStudyScheduleResponse studyScheduleTemplate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public TemplateClassroomLinkResponse(TemplateClassroomLink templateClassroomLink, boolean includeInfoForAdmin, boolean includeStudySchedule) {
        this.id = templateClassroomLink.getId();
        this.title = templateClassroomLink.getTitle();
        this.description = templateClassroomLink.getDescription();
        this.link = templateClassroomLink.getLink();
        this.status = StatusResponse.from(templateClassroomLink.getStatus());

        if (includeStudySchedule)
            this.studyScheduleTemplate = TemplateStudyScheduleResponse.info(templateClassroomLink.getTemplateStudySchedule());

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(templateClassroomLink);
    }

    public static TemplateClassroomLinkResponse infoWithStudySchedule(TemplateClassroomLink templateClassroomLink) {
        return new TemplateClassroomLinkResponse(templateClassroomLink, false, true);
    }

    public static TemplateClassroomLinkResponse info(TemplateClassroomLink templateClassroomLink) {
        return new TemplateClassroomLinkResponse(templateClassroomLink, false, false);
    }

    public static TemplateClassroomLinkResponse detailWithStudySchedule(TemplateClassroomLink templateClassroomLink) {
        return new TemplateClassroomLinkResponse(templateClassroomLink, true, true);
    }

    public static TemplateClassroomLinkResponse detail(TemplateClassroomLink templateClassroomLink) {
        return new TemplateClassroomLinkResponse(templateClassroomLink, true, false);
    }
}
