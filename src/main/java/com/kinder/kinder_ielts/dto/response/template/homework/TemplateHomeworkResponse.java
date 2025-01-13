package com.kinder.kinder_ielts.dto.response.template.homework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;
import com.kinder.kinder_ielts.constant.HomeworkStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateHomework;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class TemplateHomeworkResponse {
    private final String id;
    private final String title;
    private final String description;
    private final String link;
    private final StatusResponse<HomeworkStatus> status;
    private final StatusResponse<HomeworkPrivacyStatus> privacyStatus;
    private final ZonedDateTime dueDate;
    private final ZonedDateTime startDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TemplateHomeworkDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public TemplateHomeworkResponse(TemplateHomework templateHomework, boolean includeInfoForAdmin, boolean includeDetails) {
        this.id = templateHomework.getId();
        this.title = templateHomework.getTitle();
        this.description = templateHomework.getDescription();
        this.link = templateHomework.getLink();
        this.status = StatusResponse.from(templateHomework.getStatus());
        this.privacyStatus = StatusResponse.from(templateHomework.getPrivacyStatus());
        this.dueDate = templateHomework.getDueDate();
        this.startDate = templateHomework.getStartDate();

        mapSubInfo(templateHomework, includeInfoForAdmin);
        mapDetail(templateHomework, includeDetails);
    }

    public void mapSubInfo(TemplateHomework templateHomework, boolean includeInfoForAdmin) {
        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(templateHomework);
    }

    public void mapDetail(TemplateHomework templateHomework, boolean includeDetails) {
        if (includeDetails)
            this.detailInfo = TemplateHomeworkDetailInfoResponse.from(templateHomework);
    }

    public static TemplateHomeworkResponse info(TemplateHomework templateHomework) {
        return new TemplateHomeworkResponse(templateHomework, false, false);
    }

    public static TemplateHomeworkResponse infoWithDetails(TemplateHomework templateHomework) {
        return new TemplateHomeworkResponse(templateHomework, false, true);
    }

    public static TemplateHomeworkResponse detail(TemplateHomework templateHomework) {
        return new TemplateHomeworkResponse(templateHomework, true, false);
    }

    public static TemplateHomeworkResponse detailWithDetails(TemplateHomework templateHomework) {
        return new TemplateHomeworkResponse(templateHomework, true, true);
    }
}
