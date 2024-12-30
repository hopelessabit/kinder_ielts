package com.kinder.kinder_ielts.service;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseInfoRequest;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;

import java.util.List;

/**
 * Service interface for managing Course entities.
 */
public interface CourseService {

    CourseResponse createCourse(CreateCourseRequest request);

    CourseResponse getInfo(String id);

    CourseResponse getDetail(String id);

    List<CourseResponse> getCoursesInfo(List<String> ids);

    void deleteCourse(String id);

    CourseResponse updateCourseInfo(String id, UpdateCourseInfoRequest request);

    CourseResponse updateCourseTutor(String id, List<String> newTutorIds);
}
