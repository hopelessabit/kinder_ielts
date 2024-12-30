package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum WarmUpTestStatus implements StatusEnum {
    VIEW("Đang hiện"),
    HIDDEN("Bị ẩn");
    private String vietnamese;
    WarmUpTestStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
