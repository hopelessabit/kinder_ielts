package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum CourseType implements IEnumerate {
    ONLINE("Trực tuyến"),
    VIDEO("Video");

    private final String vietnamese;

    CourseType(String vietnamese){
        this.vietnamese = vietnamese;
    }
}
