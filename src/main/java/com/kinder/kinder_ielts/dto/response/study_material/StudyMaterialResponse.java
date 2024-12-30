package com.kinder.kinder_ielts.dto.response.study_material;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.StudyMaterial;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class StudyMaterialResponse {
    private String id;
    private String title;
    private String description;
    private String link;
    private StatusResponse<StudyMaterialStatus> privacyStatus;
    @JsonBackReference
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

    public StudyMaterialResponse(StudyMaterial studyMaterial, boolean includeInfoForAdmin, boolean includeDetails) {
        this.id = studyMaterial.getId();
        this.title = studyMaterial.getTitle();
        this.description = studyMaterial.getDescription();
        this.link = studyMaterial.getLink();
        this.privacyStatus = StatusResponse.from(studyMaterial.getPrivacyStatus());

        mapSubInfo(studyMaterial, includeInfoForAdmin);
        mapDetail(studyMaterial, includeDetails);
    }

    public void mapSubInfo(StudyMaterial studyMaterial, boolean includeInfoForAdmin) {
        if (includeInfoForAdmin) {
            this.createTime = studyMaterial.getCreateTime();

            this.modifyTime = studyMaterial.getModifyTime();

            this.createBy = SubAccountResponse.from(studyMaterial.getCreateBy());

            this.modifyBy = SubAccountResponse.from(studyMaterial.getModifyBy());

            this.isDeleted = IsDeletedResponse.from(studyMaterial.getIsDeleted());
        }
    }

    public void mapDetail(StudyMaterial studyMaterial, boolean includeDetails) {
        if (includeDetails) {
            this.beLongTo = StudyScheduleResponse.info(studyMaterial.getBeLongTo());
        }
    }

    public static StudyMaterialResponse info(StudyMaterial studyMaterial) {
        return new StudyMaterialResponse(studyMaterial, false, false);
    }

    public static StudyMaterialResponse infoWithDetails(StudyMaterial studyMaterial) {
        return new StudyMaterialResponse(studyMaterial, false, true);
    }

    public static StudyMaterialResponse detail(StudyMaterial studyMaterial) {
        return new StudyMaterialResponse(studyMaterial, true, false);
    }

    public static StudyMaterialResponse detailWithDetails(StudyMaterial studyMaterial) {
        return new StudyMaterialResponse(studyMaterial, true, true);
    }
}
