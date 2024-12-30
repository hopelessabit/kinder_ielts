package com.kinder.kinder_ielts.dto.response.classroom_link;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private ClassroomLinkStatusResponse status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudyScheduleResponse beLongTo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse createBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime modifyTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse modifyBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IsDeletedResponse isDeleted;

    public ClassroomLinkResponse(ClassroomLink classroomLink, boolean includeInfoForAdmin, boolean includeStudySchedule) {
        this.id = classroomLink.getId();
        this.title = classroomLink.getTitle();
        this.description = classroomLink.getDescription();
        this.link = classroomLink.getLink();
        this.status = ClassroomLinkStatusResponse.from(classroomLink.getStatus());

        if (includeStudySchedule){
            this.beLongTo = StudyScheduleResponse.info(classroomLink.getBeLongToStudySchedule());
        }

        if (includeInfoForAdmin) {
            this.createTime = classroomLink.getCreateTime() != null
                    ? classroomLink.getCreateTime()
                    : null;

            this.modifyTime = classroomLink.getModifyTime() != null
                    ? classroomLink.getModifyTime()
                    : null;

            this.createBy = SubAccountResponse.from(classroomLink.getCreateBy());

            this.modifyBy = SubAccountResponse.from(classroomLink.getModifyBy());
        }
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
