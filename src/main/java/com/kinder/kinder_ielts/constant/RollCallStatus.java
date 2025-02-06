package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum RollCallStatus implements StatusEnum {
    NOT_TAKEN("Chưa điểm danh"),
    ABSENT ("Vắng mặt"),
    PASSED("Đã quá thời gian điểm danh"),
    TAKEN("Đã điểm danh"),
    NOT_YET("Chưa học"),;
    private final String vietnamese;
    RollCallStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
