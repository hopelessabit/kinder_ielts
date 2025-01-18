package com.kinder.kinder_ielts.service.template;

import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.dto.request.template.CreateTemplateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.template.study_material.UpdateTemplateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.response.template.study_material.TemplateStudyMaterialResponse;

import java.util.List;

public interface TemplateStudyMaterialService {
    TemplateStudyMaterialResponse create(String templateStudyScheduleId, CreateTemplateStudyMaterialRequest request, String failMessage);
    TemplateStudyMaterialResponse get(String templateStudyMaterialId, String failMessage);
    List<TemplateStudyMaterialResponse> getByTemplateStudyScheduleId(String templateStudyScheduleId, String failMessage);
    TemplateStudyMaterialResponse updateInfo(String templateStudyMaterialId, UpdateTemplateStudyMaterialRequest request, String failMessage);
    TemplateStudyMaterialResponse updatePrivacyStatus(String templateStudyMaterialId, StudyMaterialStatus privacyStatus, String failMessage);
    void delete(String templateStudyMaterialId, String failMessage);
}
