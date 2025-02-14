package com.kinder.kinder_ielts.dto.response.study_material;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.material_link.MaterialLinkResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.StudyMaterial;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class StudyMaterialResponse {
    private String id;
    private String title;
    private String description;
    private StatusResponse<StudyMaterialStatus> privacyStatus;
    private List<MaterialLinkResponse> links;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudyScheduleResponse studySchedule;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public StudyMaterialResponse(StudyMaterial studyMaterial, boolean includeInfoForAdmin, boolean includeDetails) {
        this.id = studyMaterial.getId();
        this.title = studyMaterial.getTitle();
        this.description = studyMaterial.getDescription();
        this.privacyStatus = StatusResponse.from(studyMaterial.getPrivacyStatus());
        this.links = studyMaterial.getMaterialLinks() != null ?
                studyMaterial.getMaterialLinks()
                        .stream()
                        .filter(a -> a.getIsDeleted().equals(IsDelete.NOT_DELETED))
                        .map(MaterialLinkResponse::info).toList()
                : new ArrayList<>();

        mapSubInfo(studyMaterial, includeInfoForAdmin);
        mapDetail(studyMaterial, includeDetails);
    }

    public void mapSubInfo(StudyMaterial studyMaterial, boolean includeInfoForAdmin) {
        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(studyMaterial);
    }

    public void mapDetail(StudyMaterial studyMaterial, boolean includeDetails) {
        if (includeDetails)
            this.studySchedule = StudyScheduleResponse.info(studyMaterial.getBeLongTo());
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
