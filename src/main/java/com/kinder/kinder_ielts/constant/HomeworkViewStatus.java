package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum HomeworkViewStatus implements StatusEnum {
    PUBLIC("Công khai"),
    PRIVATE("Không công khai");
    private String vietnamese;
    HomeworkViewStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
