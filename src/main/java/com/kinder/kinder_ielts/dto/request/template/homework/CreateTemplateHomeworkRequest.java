package com.kinder.kinder_ielts.dto.request.template.homework;

import com.kinder.kinder_ielts.constant.HomeworkViewStatus;
import com.kinder.kinder_ielts.constant.HomeworkStatus;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateHomeworkRequest {
    private String title;
    private String description;
    private String link;
    private HomeworkViewStatus privacyStatus;
    private HomeworkStatus status;
    private ZonedDateTime dueDate;
    private ZonedDateTime startDate;
}
