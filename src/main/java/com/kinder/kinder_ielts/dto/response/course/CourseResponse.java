package com.kinder.kinder_ielts.dto.response.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.response.course_level.CourseLevelResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class CourseResponse {

    private String id;
    private String description;
    private String detail;
    private BigDecimal price;
    private CourseStatusResponse status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal sale;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ClassroomResponse> classrooms;
    @JsonManagedReference
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourseLevelResponse level;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TutorResponse> tutors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse createBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime modifyTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse modifyBy;

    public CourseResponse(Course course, boolean includeInfoForAdmin, boolean includeDetails) {
        this.id = course.getId();
        this.description = course.getDescription();
        this.detail = course.getDetail();
        this.price = course.getPrice();
        this.sale = course.getSale();
        this.status = CourseStatusResponse.from(course.getStatus());
        this.level = CourseLevelResponse.info(course.getLevel());

        if (includeDetails){
            this.classrooms = course.getClassrooms().stream().map(ClassroomResponse::info).toList();
            this.tutors = course.getCourseTutors() != null
                    ? course.getCourseTutors().stream().map(CourseTutor::getTutor).map(TutorResponse::withNoAccountInfo).toList()
                    : null;
        }

        if (includeInfoForAdmin) {
            if (includeDetails)
                this.classrooms = course.getClassrooms().stream().map(ClassroomResponse::detail).toList();

            this.tutors = course.getCourseTutors() != null
                    ? course.getCourseTutors().stream().map(CourseTutor::getTutor).map(TutorResponse::withAccountInfo).toList()
                    : null;

            this.level = CourseLevelResponse.detailed(course.getLevel());

            this.createTime = course.getCreateTime() != null
                    ? course.getCreateTime()
                    : null;

            this.modifyTime = course.getModifyTime() != null
                    ? course.getModifyTime()
                    : null;

            this.createBy = SubAccountResponse.from(course.getCreateBy());

            this.modifyBy = SubAccountResponse.from(course.getModifyBy());
        }
    }

    public static CourseResponse infoWithDetails(Course course) {
        return new CourseResponse(course, false, true);
    }

    public static CourseResponse info(Course course) {
        return new CourseResponse(course, false, false);
    }

    public static CourseResponse detailWithDetails(Course course) {
        return new CourseResponse(course, true, true);
    }

    public static CourseResponse detail(Course course) {
        return new CourseResponse(course, true, true);
    }
}
