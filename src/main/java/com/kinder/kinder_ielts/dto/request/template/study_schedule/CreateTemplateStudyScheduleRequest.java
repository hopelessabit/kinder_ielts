package com.kinder.kinder_ielts.dto.request.template.study_schedule;
import com.kinder.kinder_ielts.constant.ViewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateStudyScheduleRequest {
    private String title;
    private String description;
    private Integer place;
    private ViewStatus status;
}
