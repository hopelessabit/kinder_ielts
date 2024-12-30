package com.kinder.kinder_ielts.dto.response;

import com.kinder.kinder_ielts.constant.StatusEnum;
import lombok.Getter;

@Getter
public class StatusResponse<T extends Enum<T> & StatusEnum> {
    private String name;
    private String vietnamese;

    public static <T extends Enum<T> & StatusEnum> StatusResponse<T> from(T status) {
        return new StatusResponse<>(status);
    }

    public StatusResponse(T status) {
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}