package com.kinder.kinder_ielts.dto.response.homework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;
import com.kinder.kinder_ielts.constant.HomeworkStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.entity.Homework;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class HomeworkResponse {
    private final String id;
    private final String title;
    private final String description;
    private final String link;
    private final StatusResponse<HomeworkStatus> status;
    private final StatusResponse<HomeworkPrivacyStatus> privacyStatus;
    private final ZonedDateTime dueDate;
    private final ZonedDateTime startDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HomeworkDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public HomeworkResponse(Homework homework, boolean includeInfoForAdmin, boolean includeDetails) {
        this.id = homework.getId();
        this.title = homework.getTitle();
        this.description = homework.getDescription();
        this.link = homework.getLink();
        this.status = StatusResponse.from(homework.getStatus());
        this.privacyStatus = StatusResponse.from(homework.getPrivacyStatus());
        this.dueDate = homework.getDueDate();
        this.startDate = homework.getStartDate();

        mapSubInfo(homework, includeInfoForAdmin);
        mapDetail(homework, includeDetails);
    }

    public void mapSubInfo(Homework homework, boolean includeInfoForAdmin) {
        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(homework);
    }

    public void mapDetail(Homework homework, boolean includeDetails) {
        if (includeDetails)
            this.detailInfo = HomeworkDetailInfoResponse.from(homework);
    }

    public static HomeworkResponse info(Homework homework) {
        return new HomeworkResponse(homework, false, false);
    }

    public static HomeworkResponse infoWithDetails(Homework homework) {
        return new HomeworkResponse(homework, false, true);
    }

    public static HomeworkResponse detail(Homework homework) {
        return new HomeworkResponse(homework, true, false);
    }

    public static HomeworkResponse detailWithDetails(Homework homework) {
        return new HomeworkResponse(homework, true, true);
    }

}
