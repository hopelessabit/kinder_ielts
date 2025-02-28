package com.kinder.kinder_ielts.dto.response.course;

import com.kinder.kinder_ielts.dto.response.classroom.ClassInfoResponse;
import com.kinder.kinder_ielts.dto.response.course_level.CourseLevelResponse;
import com.kinder.kinder_ielts.entity.Course;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CourseInfoRequest {
    private String id;
    private CourseLevelResponse level;
    private String description;
    private List<ClassInfoResponse> classes;

    public CourseInfoRequest(Course course) {
        this.id = course.getId();
        this.level = CourseLevelResponse.info(course.getLevel());
        this.description = course.getDescription();
        this.classes = course.getClassrooms() != null
                ? course.getClassrooms().stream()
                .map(ClassInfoResponse::info)
                .toList()
                : new ArrayList<>();
    }

    public static CourseInfoRequest info(Course course) {
        return new CourseInfoRequest(course);
    }
}
