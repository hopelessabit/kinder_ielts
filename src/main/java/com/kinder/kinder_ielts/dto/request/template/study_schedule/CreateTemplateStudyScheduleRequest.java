package com.kinder.kinder_ielts.dto.request.template.study_schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateStudyScheduleRequest {
    private ZonedDateTime dateTime;
    private String title;
    private String description;
}
