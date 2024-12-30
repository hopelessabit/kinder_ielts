package com.kinder.kinder_ielts.dto.response.student_homework;

import com.kinder.kinder_ielts.constant.HomeWorkSubmitStatus;

public class HomeWorkSubmitStatusResponse {
    private String name;
    private String vietnamese;

    public static HomeWorkSubmitStatusResponse from(HomeWorkSubmitStatus status) {
        return new HomeWorkSubmitStatusResponse(status);
    }

    public HomeWorkSubmitStatusResponse(HomeWorkSubmitStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
