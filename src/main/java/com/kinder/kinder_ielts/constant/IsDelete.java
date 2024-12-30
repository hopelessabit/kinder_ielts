package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum IsDelete {
    NOT_DELETED ("Chưa xóa"),
    DELETED ("Đã xóa");
    private final String vietnamese;
    IsDelete(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
