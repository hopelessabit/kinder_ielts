package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum GradeStatus implements StatusEnum {
    PENDING_REVIEW("Chờ chấm điểm"),
    GRADED("Đã chấm điểm"),
    NOT_SUBMITTED("Chưa nộp bài"),
    CANCELLED("Bị hủy");
    private final String vietnamese;
    GradeStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
