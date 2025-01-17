package com.kinder.kinder_ielts.dto.request.template.study_schedule;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateStudyScheduleRequest {
    private String title;
    private String description;
}
