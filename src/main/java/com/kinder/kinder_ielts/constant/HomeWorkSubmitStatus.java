package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum HomeWorkSubmitStatus implements IEnumerate {
    NOT_SUBMITTED("Chưa nộp bài"),
    SUBMITTED("Đã nộp bài"),
    OVER_DUE("Quá hạn");
    private final String vietnamese;
    HomeWorkSubmitStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
