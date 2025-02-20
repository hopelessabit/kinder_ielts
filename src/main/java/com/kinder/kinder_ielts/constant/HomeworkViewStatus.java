package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum HomeworkViewStatus implements StatusEnum {
    VIEW("Đang hiện"),
    HIDDEN("Bị ẩn");
    private String vietnamese;
    HomeworkViewStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}

