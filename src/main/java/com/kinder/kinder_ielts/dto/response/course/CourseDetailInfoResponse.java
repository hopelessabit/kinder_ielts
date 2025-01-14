package com.kinder.kinder_ielts.dto.response.course;

import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.entity.Course;
import lombok.Getter;

import java.util.List;

@Getter
public class CourseDetailInfoResponse {
    private List<ClassroomResponse> classrooms;

    public CourseDetailInfoResponse(Course course) {
        this.classrooms = course.getClassrooms() != null ? course.getClassrooms().stream().map(ClassroomResponse::info).toList() : null;
    }

    public static CourseDetailInfoResponse from(Course course) {
        return new CourseDetailInfoResponse(course);
    }
}
