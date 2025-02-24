package com.kinder.kinder_ielts.dto.response.warm_up_test;

import com.kinder.kinder_ielts.constant.ViewStatus;
import lombok.Getter;

@Getter
public class WarmUpTestStatusResponse {
    private String name;
    private String vietnamese;

    public static WarmUpTestStatusResponse from(ViewStatus status) {
        return new WarmUpTestStatusResponse(status);
    }

    public WarmUpTestStatusResponse(ViewStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
