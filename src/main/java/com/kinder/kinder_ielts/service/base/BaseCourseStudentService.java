package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.id.CourseStudentId;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;

import java.time.ZonedDateTime;
import java.util.List;

public interface BaseCourseStudentService extends BaseEntityService<CourseStudent, CourseStudentId>{
    public void create(Student student, Course course, Account creator, ZonedDateTime currentTime);

    List<CourseStudent> getByStudentIds(List<String> studentIds);
    List<CourseStudent> getByStudentIdsAndCourseIds(List<String> studentIds, List<String> courseIds);
}
