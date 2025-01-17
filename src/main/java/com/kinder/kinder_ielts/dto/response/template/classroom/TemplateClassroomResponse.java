package com.kinder.kinder_ielts.dto.response.template.classroom;

import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class TemplateClassroomResponse {
    private final String id;
    private final String name;

    public static TemplateClassroomResponse info(TemplateClassroom templateClassroom) {
        return new TemplateClassroomResponse(templateClassroom.getId(), templateClassroom.getName());
    }
}
