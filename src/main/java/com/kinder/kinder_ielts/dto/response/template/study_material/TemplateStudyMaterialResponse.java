package com.kinder.kinder_ielts.dto.response.template.study_material;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.template.study_schedule.TemplateStudyScheduleResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudyMaterial;
import lombok.Getter;

@Getter
public class TemplateStudyMaterialResponse {
    private final String id;
    private final String title;
    private final String description;
    private final StatusResponse<StudyMaterialStatus> privacyStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TemplateStudyScheduleResponse studyScheduleTemplate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public TemplateStudyMaterialResponse(TemplateStudyMaterial studyMaterial, boolean includeInfoForAdmin, boolean includeDetails) {
        this.id = studyMaterial.getId();
        this.title = studyMaterial.getTitle();
        this.description = studyMaterial.getDescription();
        this.privacyStatus = StatusResponse.from(studyMaterial.getPrivacyStatus());

        mapSubInfo(studyMaterial, includeInfoForAdmin);
        mapDetail(studyMaterial, includeDetails);
    }

    public void mapSubInfo(TemplateStudyMaterial studyMaterial, boolean includeInfoForAdmin) {
        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(studyMaterial);
    }

    public void mapDetail(TemplateStudyMaterial studyMaterial, boolean includeDetails) {
        if (includeDetails)
            this.studyScheduleTemplate = TemplateStudyScheduleResponse.info(studyMaterial.getTemplateStudySchedule());
    }

    public static TemplateStudyMaterialResponse info(TemplateStudyMaterial studyMaterial) {
        return new TemplateStudyMaterialResponse(studyMaterial, false, false);
    }

    public static TemplateStudyMaterialResponse infoWithDetails(TemplateStudyMaterial studyMaterial) {
        return new TemplateStudyMaterialResponse(studyMaterial, false, true);
    }

    public static TemplateStudyMaterialResponse detail(TemplateStudyMaterial studyMaterial) {
        return new TemplateStudyMaterialResponse(studyMaterial, true, false);
    }

    public static TemplateStudyMaterialResponse detailWithDetails(TemplateStudyMaterial studyMaterial) {
        return new TemplateStudyMaterialResponse(studyMaterial, true, true);
    }
}
