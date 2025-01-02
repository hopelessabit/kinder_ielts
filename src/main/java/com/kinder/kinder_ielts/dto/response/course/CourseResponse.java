package com.kinder.kinder_ielts.dto.response.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.CourseStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.course_level.CourseLevelResponse;
import com.kinder.kinder_ielts.entity.Course;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CourseResponse {
    private String id;
    private String description;
    private String detail;
    private BigDecimal price;
    private CourseLevelResponse level;
    private StatusResponse<CourseStatus> status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourseDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public CourseResponse(Course course, boolean includeInfoForAdmin, boolean includeDetails) {
        this.id = course.getId();
        this.description = course.getDescription();
        this.detail = course.getDetail();
        this.price = course.getPrice();
        this.status = StatusResponse.from(course.getStatus());
        this.level = CourseLevelResponse.info(course.getLevel());

        if (includeDetails)
            this.detailInfo = CourseDetailInfoResponse.from(course);

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(course);
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
