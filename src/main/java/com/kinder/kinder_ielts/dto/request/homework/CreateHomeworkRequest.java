package com.kinder.kinder_ielts.dto.request.homework;

import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;
import com.kinder.kinder_ielts.constant.HomeworkStatus;

import java.time.ZonedDateTime;
import java.util.List;

import com.kinder.kinder_ielts.constant.HomeworkSubmitAllowLateStatus;
import com.kinder.kinder_ielts.constant.ViewStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateHomeworkRequest {
    private String title;
    private String description;
    private String link;
    private List<String> studentIds;
    private HomeworkStatus status;
    @Nullable
    private HomeworkPrivacyStatus privacyStatus;
    @Nullable
    private ViewStatus viewStatus;
    @Nullable
    private HomeworkSubmitAllowLateStatus submitAllowLateStatus;
    private ZonedDateTime dueDate;
    private ZonedDateTime startDate;
}
