package com.kinder.kinder_ielts.dto.request.roll_call;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateRollCallsRequest {
    private List<RollCallRequest> rollCalls;
}
