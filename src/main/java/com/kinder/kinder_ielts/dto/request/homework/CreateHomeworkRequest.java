package com.kinder.kinder_ielts.dto.request.homework;

import com.kinder.kinder_ielts.constant.HomeworkStatus;

import java.time.ZonedDateTime;
import java.util.List;

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
    private ZonedDateTime dueDate;
    private ZonedDateTime startDate;
}
