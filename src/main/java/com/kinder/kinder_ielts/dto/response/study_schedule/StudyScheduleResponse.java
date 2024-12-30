package com.kinder.kinder_ielts.dto.response.study_schedule;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private List<ClassroomLinkResponse> classroomLinks;
    private List<WarmUpTestResponse> warmUpTests;
    private List<HomeworkResponse> homeworks;
    private List<StudyMaterialResponse> studyMaterials;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClassroomResponse belongToClass;
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

    public StudyScheduleResponse(StudySchedule studySchedule, boolean includeInfoForAdmin, boolean includeDetail) {
        this.id = studySchedule.getId();
        this.dateTime = studySchedule.getDateTime();
        this.title = studySchedule.getTitle();
        this.description = studySchedule.getDescription();

        if (includeDetail) {
            if (studySchedule.getClassroomLinks() != null)
                this.classroomLinks = studySchedule.getClassroomLinks().stream().map(ClassroomLinkResponse::info).toList();
            else
                this.classroomLinks = new ArrayList<>();
            if (studySchedule.getWarmUpTests() != null)
                this.warmUpTests = studySchedule.getWarmUpTests().stream().map(WarmUpTestResponse::info).toList();
            else
                this.warmUpTests = new ArrayList<>();
            if (studySchedule.getHomework() != null)
                this.homeworks = studySchedule.getHomework().stream().map(HomeworkResponse::info).toList();
            else
                this.homeworks = new ArrayList<>();
            if (studySchedule.getClassroomLinks() != null)
                this.studyMaterials = studySchedule.getStudyMaterials().stream().map(StudyMaterialResponse::info).toList();
            else
                this.studyMaterials = new ArrayList<>();
            this.belongToClass = ClassroomResponse.info(studySchedule.getBelongToClassroom());
        }

        if (includeInfoForAdmin) {
            this.createTime = studySchedule.getCreateTime() != null
                    ? studySchedule.getCreateTime()
                    : null;

            this.modifyTime = studySchedule.getModifyTime() != null
                    ? studySchedule.getModifyTime()
                    : null;

            this.createBy = SubAccountResponse.from(studySchedule.getCreateBy());

            this.modifyBy = SubAccountResponse.from(studySchedule.getModifyBy());
        }
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
