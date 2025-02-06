package com.kinder.kinder_ielts.dto.request.roll_call;

import com.kinder.kinder_ielts.constant.RollCallStatus;

public class UpdateRollCallRequest {
    public String studentId;
    public RollCallStatus status;
    public String note;
}
