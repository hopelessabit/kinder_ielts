package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum Role {
    USER ("Người dùng"),
    STUDENT ("Học sinh"),
    ADMIN ("Admin"),
    MODERATOR ("Quản lý"),
    TUTOR ("Gia sư"),
    SYSTEM ("Hệ thống");

    private String vietnamese;
    Role(String vietnamese){
        this.vietnamese = vietnamese;
    }
}
