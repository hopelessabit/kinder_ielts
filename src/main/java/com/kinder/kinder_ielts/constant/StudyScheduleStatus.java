package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum StudyScheduleStatus implements StatusEnum {
    VIEW("Đang hiện"),
    HIDDEN("Bị ẩn");
    private String vietnamese;
    StudyScheduleStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
