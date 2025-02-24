package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum ViewStatus implements StatusEnum {
    VIEW("Đang hiện"),
    HIDDEN("Bị ẩn");
    private String vietnamese;
    ViewStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
