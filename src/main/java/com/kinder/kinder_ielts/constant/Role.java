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

    public boolean isUser() {
        return this == USER;
    }

    public boolean isStudent() {
        return this == STUDENT;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isModerator() {
        return this == MODERATOR;
    }

    public boolean isTutor() {
        return this == TUTOR;
    }
}
