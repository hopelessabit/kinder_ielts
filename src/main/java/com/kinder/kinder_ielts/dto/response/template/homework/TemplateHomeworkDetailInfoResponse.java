package com.kinder.kinder_ielts.dto.response.template.homework;

import com.kinder.kinder_ielts.dto.response.template.study_schedule.TemplateStudyScheduleResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateHomework;
import lombok.Getter;

@Getter
public class TemplateHomeworkDetailInfoResponse {
    private final TemplateStudyScheduleResponse studyScheduleTemplate;

    public TemplateHomeworkDetailInfoResponse(TemplateHomework templateHomework) {
        this.studyScheduleTemplate = TemplateStudyScheduleResponse.info(templateHomework.getTemplateStudySchedule());
    }

    public static TemplateHomeworkDetailInfoResponse from(TemplateHomework templateHomework) {
        return new TemplateHomeworkDetailInfoResponse(templateHomework);
    }
}
