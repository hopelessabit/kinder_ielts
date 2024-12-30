package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum StudyMaterialStatus implements StatusEnum {
    PUBLIC("Công khai"),
    PRIVATE("Không công khai");
    private String vietnamese;
    StudyMaterialStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
