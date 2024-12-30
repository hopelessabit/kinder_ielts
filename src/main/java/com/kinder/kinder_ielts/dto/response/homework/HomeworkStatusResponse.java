package com.kinder.kinder_ielts.dto.response.homework;

import com.kinder.kinder_ielts.constant.HomeworkStatus;
import lombok.Getter;

@Getter
public class HomeworkStatusResponse {
    private String name;
    private String vietnamese;

    public static HomeworkStatusResponse from(HomeworkStatus status) {
        return new HomeworkStatusResponse(status);
    }

    public HomeworkStatusResponse(HomeworkStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
