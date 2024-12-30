package com.kinder.kinder_ielts.dto.response.warm_up_test;

import com.kinder.kinder_ielts.constant.StatusEnum;
import com.kinder.kinder_ielts.constant.WarmUpTestStatus;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import lombok.Getter;

@Getter
public class WarmUpTestStatusResponse {
    private String name;
    private String vietnamese;

    public static WarmUpTestStatusResponse from(WarmUpTestStatus status) {
        return new WarmUpTestStatusResponse(status);
    }

    public WarmUpTestStatusResponse(WarmUpTestStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
