package com.kinder.kinder_ielts.dto.response.template.classroom;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.response.template.study_schedule.TemplateStudyScheduleResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateClassroomDetailInfoResponse {
    private List<TemplateStudyScheduleResponse> studySchedules;

    public static TemplateClassroomDetailInfoResponse from(TemplateClassroom templateClassroom) {
        return new TemplateClassroomDetailInfoResponse(templateClassroom.getStudySchedules()
                .stream()
                .filter(templateStudySchedule -> templateClassroom.getIsDeleted().equals(IsDelete.NOT_DELETED))
                .sorted(Comparator.comparing(TemplateStudySchedule::getPlace))
                .map(TemplateStudyScheduleResponse::info)
                .toList());
    }
}
