package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum StudyMaterialViewStatus implements StatusEnum {
    VIEW("Đang hiện"),
    HIDDEN("Bị ẩn");
    private String vietnamese;
    StudyMaterialViewStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
