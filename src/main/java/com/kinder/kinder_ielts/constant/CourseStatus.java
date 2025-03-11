package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum CourseStatus implements IEnumerate {
    ACTIVE ("Hoạt động"),
    INACTIVE ("Không hoạt động");
    private final String vietnamese;
    CourseStatus(String vietnamese){
        this.vietnamese = vietnamese;
    }
}
