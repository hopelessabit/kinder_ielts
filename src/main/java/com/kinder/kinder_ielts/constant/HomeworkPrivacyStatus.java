package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum HomeworkPrivacyStatus implements IEnumerate {
    PUBLIC("Công khai"),
    PRIVATE("Không công khai");
    private String vietnamese;
    HomeworkPrivacyStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
