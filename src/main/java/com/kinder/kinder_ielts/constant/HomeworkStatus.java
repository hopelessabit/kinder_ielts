package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum HomeworkStatus implements StatusEnum {
    NOT_ASSIGNED("Chưa giao bài tập"),
    ASSIGNED("Đã giao bài tập");
    private final String vietnamese;
    HomeworkStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
