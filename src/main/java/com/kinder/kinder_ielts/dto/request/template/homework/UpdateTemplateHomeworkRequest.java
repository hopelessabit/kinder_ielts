package com.kinder.kinder_ielts.dto.request.template.homework;
import com.kinder.kinder_ielts.constant.HomeworkStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateHomeworkRequest {
    private String title;
    private String description;
    private String link;
    private HomeworkStatus status;
    private ZonedDateTime dueDate;
    private ZonedDateTime startDate;
}
