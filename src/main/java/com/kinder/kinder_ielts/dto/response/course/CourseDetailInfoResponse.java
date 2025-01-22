package com.kinder.kinder_ielts.dto.response.course;

import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.response.template.classroom.TemplateClassroomResponse;
import com.kinder.kinder_ielts.entity.Course;
import lombok.Getter;

import java.util.List;

@Getter
public class CourseDetailInfoResponse {
    private List<ClassroomResponse> classrooms;
    private List<TemplateClassroomResponse> templates;
    public CourseDetailInfoResponse(Course course, boolean includeTemplateClassroom, boolean includeClassroom) {
        if (includeClassroom)
            this.classrooms = course.getClassrooms() != null ? course.getClassrooms().stream()
                    .filter(classroom -> !classroom.getIsDeleted().isDeleted())
                    .map(ClassroomResponse::info).toList() : null;
        if (includeTemplateClassroom)
            this.templates = course.getTemplates() != null ? course.getTemplates().stream()
                    .filter(templateClassroom -> !templateClassroom.getIsDeleted().isDeleted())
                    .map(TemplateClassroomResponse::info).toList() : null;
    }

    public static CourseDetailInfoResponse includeTemplateClassroom(Course course) {
        return new CourseDetailInfoResponse(course, true, false);
    }

    public static CourseDetailInfoResponse includeClassroom(Course course) {
        return new CourseDetailInfoResponse(course, false, true);
    }

    public static CourseDetailInfoResponse includeAll(Course course) {
        return new CourseDetailInfoResponse(course, true, true);
    }
}
