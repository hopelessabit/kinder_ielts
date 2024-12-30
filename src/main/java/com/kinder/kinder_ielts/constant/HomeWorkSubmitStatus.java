package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum HomeWorkSubmitStatus implements StatusEnum {
    NOT_SUBMITTED("Chưa nộp bài"),
    SUBMITTED("Đã nộp bài");
    private final String vietnamese;
    HomeWorkSubmitStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
