package com.kinder.kinder_ielts.dto.request.template.study_schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateStudySchedulePlaceRequest {
    private List<TemplateStudySchedulePlace> templateStudySchedulePlaces;
}
