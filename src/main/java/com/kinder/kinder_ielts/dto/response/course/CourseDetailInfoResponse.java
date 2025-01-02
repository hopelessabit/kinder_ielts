package com.kinder.kinder_ielts.dto.response.course;

import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CourseDetailInfoResponse {
    private BigDecimal sale;
    private List<ClassroomResponse> classrooms;
    private List<TutorResponse> tutors;

    public CourseDetailInfoResponse(Course course) {
        this.sale = course.getSale();
        this.classrooms = course.getClassrooms() != null ? course.getClassrooms().stream().map(ClassroomResponse::info).toList() : null;
        this.tutors = course.getCourseTutors() != null
                ? course.getCourseTutors().stream().map(CourseTutor::getTutor).map(TutorResponse::withNoAccountInfo).toList()
                : null;
    }

    public static CourseDetailInfoResponse from(Course course) {
        return new CourseDetailInfoResponse(course);
    }
}
