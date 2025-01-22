package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum IsDelete implements StatusEnum {
    NOT_DELETED ("Chưa xóa"),
    DELETED ("Đã xóa");
    private final String vietnamese;
    IsDelete(String vietnamese) {
        this.vietnamese = vietnamese;
    }
    public boolean isDeleted(){
        return this == DELETED;
    }
}
