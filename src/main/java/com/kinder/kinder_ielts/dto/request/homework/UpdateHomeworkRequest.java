package com.kinder.kinder_ielts.dto.request.homework;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHomeworkRequest {
    private String title;
    private String description;
    private String link;
    private ZonedDateTime dueDate;
    private ZonedDateTime startDate;
}
