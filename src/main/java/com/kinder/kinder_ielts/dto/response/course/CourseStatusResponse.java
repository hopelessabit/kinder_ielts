package com.kinder.kinder_ielts.dto.response.course;

import com.kinder.kinder_ielts.constant.CourseStatus;

public class CourseStatusResponse {
    private String name;
    private String vietnamese;

    public static CourseStatusResponse from(CourseStatus status) {
        return new CourseStatusResponse(status);
    }

    public CourseStatusResponse(CourseStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
