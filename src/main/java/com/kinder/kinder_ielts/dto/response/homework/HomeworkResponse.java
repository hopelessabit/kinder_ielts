package com.kinder.kinder_ielts.dto.response.homework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;
import com.kinder.kinder_ielts.constant.HomeworkStatus;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.student_homework.StudentHomeworkResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Homework;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class HomeworkResponse {
    private String id;
    private String title;
    private String description;
    private String link;
    private StatusResponse<HomeworkStatus> status;
    private StatusResponse<HomeworkPrivacyStatus> privacyStatus;
    private ZonedDateTime dueDate;
    private ZonedDateTime startDate;
    private TutorResponse assignBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudyScheduleResponse beLongTo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<StudentHomeworkResponse> studentHomeworks;
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
        if (includeInfoForAdmin) {
            this.createTime = homework.getCreateTime();

            this.modifyTime = homework.getModifyTime();

            this.createBy = SubAccountResponse.from(homework.getCreateBy());

            this.modifyBy = SubAccountResponse.from(homework.getModifyBy());

            this.isDeleted = IsDeletedResponse.from(homework.getIsDeleted());
        }
    }

    public void mapDetail(Homework homework, boolean includeDetails) {
        if (includeDetails) {
            this.assignBy = TutorResponse.withNoAccountInfo(homework.getAssignBy());
            this.beLongTo = StudyScheduleResponse.info(homework.getBeLongTo());
            this.studentHomeworks = homework.getStudentHomeworks().stream().map(StudentHomeworkResponse::info).toList();
        }
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
