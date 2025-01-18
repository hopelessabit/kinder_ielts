package com.kinder.kinder_ielts.dto.response.template.classroom;

import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TemplateClassroomResponse {
    private final String id;
    private final String name;
    private TemplateClassroomDetailInfoResponse detailInfo;

    public TemplateClassroomResponse(TemplateClassroom templateClassroom, boolean includeDetail){
        this.id = templateClassroom.getId();
        this.name = templateClassroom.getName();
        if (includeDetail)
            this.detailInfo = TemplateClassroomDetailInfoResponse.from(templateClassroom);
    }

    public static TemplateClassroomResponse info(TemplateClassroom templateClassroom) {
        return new TemplateClassroomResponse(templateClassroom, false);
    }

    public static TemplateClassroomResponse detail(TemplateClassroom templateClassroom) {
        return new TemplateClassroomResponse(templateClassroom, true);
    }
}
