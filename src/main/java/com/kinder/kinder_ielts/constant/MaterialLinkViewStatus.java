package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum MaterialLinkViewStatus implements StatusEnum {
    VIEW("Đang hiện"),
    HIDDEN("Bị ẩn");
    private String vietnamese;
    MaterialLinkViewStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
