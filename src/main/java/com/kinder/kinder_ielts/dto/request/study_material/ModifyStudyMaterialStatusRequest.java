package com.kinder.kinder_ielts.dto.request.study_material;

import com.kinder.kinder_ielts.constant.StudyMaterialStatus;

import java.util.List;

public class ModifyStudyMaterialStatusRequest {
    public StudyMaterialStatus status;
    public List<String> studentIds;
}
