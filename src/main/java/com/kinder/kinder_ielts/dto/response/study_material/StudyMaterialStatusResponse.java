package com.kinder.kinder_ielts.dto.response.study_material;

import com.kinder.kinder_ielts.constant.StudyMaterialStatus;

public class StudyMaterialStatusResponse {
    private String name;
    private String vietnamese;

    public static StudyMaterialStatusResponse from(StudyMaterialStatus status) {
        return new StudyMaterialStatusResponse(status);
    }

    public StudyMaterialStatusResponse(StudyMaterialStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
