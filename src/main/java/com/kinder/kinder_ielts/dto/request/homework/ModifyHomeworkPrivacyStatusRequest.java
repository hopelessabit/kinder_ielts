package com.kinder.kinder_ielts.dto.request.homework;

import com.kinder.kinder_ielts.constant.HomeworkViewStatus;

import java.util.List;

public class ModifyHomeworkPrivacyStatusRequest {
    public HomeworkViewStatus status;
    public List<String> studentIds;
}
