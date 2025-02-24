package com.kinder.kinder_ielts.dto.response.classroom_link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.ClassroomLink;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ClassroomLinkResponse {
    private String id;
    private String title;
    private String description;
    private String link;
    private StatusResponse<ViewStatus> status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudyScheduleResponse studySchedule;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public ClassroomLinkResponse(ClassroomLink classroomLink, boolean includeInfoForAdmin, boolean includeStudySchedule) {
        this.id = classroomLink.getId();
        this.title = classroomLink.getTitle();
        this.description = classroomLink.getDescription();
        this.link = classroomLink.getLink();
        this.status = StatusResponse.from(classroomLink.getStatus());

        if (includeStudySchedule)
            this.studySchedule = StudyScheduleResponse.info(classroomLink.getBeLongToStudySchedule());

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(classroomLink);
    }

    public static ClassroomLinkResponse infoWithStudySchedule(ClassroomLink classroomLink) {
        return new ClassroomLinkResponse(classroomLink, false, true);
    }

    public static ClassroomLinkResponse info(ClassroomLink classroomLink) {
        return new ClassroomLinkResponse(classroomLink, false, false);
    }

    public static ClassroomLinkResponse detailWithStudySchedule(ClassroomLink classroomLink) {
        return new ClassroomLinkResponse(classroomLink, true, true);
    }

    public static ClassroomLinkResponse detail(ClassroomLink classroomLink) {
        return new ClassroomLinkResponse(classroomLink, true, false);
    }
}
