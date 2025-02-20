package com.kinder.kinder_ielts.dto.request.homework;

import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;

import java.util.List;

public class ModifyHomeworkPrivacyStatusRequest {
    public HomeworkPrivacyStatus status;
    public List<String> studentIds;
}
