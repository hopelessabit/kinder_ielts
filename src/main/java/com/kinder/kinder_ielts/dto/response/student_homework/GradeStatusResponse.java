package com.kinder.kinder_ielts.dto.response.student_homework;

import com.kinder.kinder_ielts.constant.GradeStatus;

public class GradeStatusResponse {
    private String name;
    private String vietnamese;

    public static GradeStatusResponse from(GradeStatus status) {
        return new GradeStatusResponse(status);
    }

    public GradeStatusResponse(GradeStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
