package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum ClassroomLinkStatus implements StatusEnum {
    VIEW("Đang hiện"),
    HIDDEN("Bị ẩn");
    private String vietnamese;
    ClassroomLinkStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
