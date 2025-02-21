package com.kinder.kinder_ielts.dto.response.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.CourseStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.course_level.CourseLevelResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CourseResponse {
    private String id;
    private String description;
    private String detail;
    private BigDecimal price;
    private BigDecimal sale;
    private Integer slots;
    private String thumbnail;
    private String icon;
    private List<TutorResponse> tutors;
    private CourseLevelResponse level;
    private StatusResponse<CourseStatus> status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourseDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public CourseResponse(Course course, boolean includeInfoForAdmin, boolean includeDetailInfo) {
        this.id = course.getId();
        this.description = course.getDescription();
        this.detail = course.getDetail();
        this.slots = course.getSlots();
        this.thumbnail = course.getThumbnailLink();
        this.icon = course.getIconLink();
        this.status = StatusResponse.from(course.getStatus());
        this.level = CourseLevelResponse.info(course.getLevel());
        this.price = course.getPrice();
        this.tutors = course.getCourseTutors() != null
                ? course.getCourseTutors().stream()
                .filter(courseTutor -> !courseTutor.getIsDeleted().isDeleted())
                .map(CourseTutor::getTutor)
                .map(TutorResponse::withNoAccountInfo)
                .toList()
                : null;

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(course);

        if (includeDetailInfo)
            this.detailInfo = CourseDetailInfoResponse.includeTemplateClassroom(course);
    }

    public CourseResponse(Course course, boolean includeInfoForAdmin, boolean includeTemplateClassroom, boolean includeClassroom) {
        this.id = course.getId();
        this.description = course.getDescription();
        this.detail = course.getDetail();
        this.slots = course.getSlots();
        this.status = StatusResponse.from(course.getStatus());
        this.level = CourseLevelResponse.info(course.getLevel());
        this.price = course.getPrice();
        this.tutors = course.getCourseTutors() != null
                ? course.getCourseTutors().stream()
                .filter(courseTutor -> !courseTutor.getIsDeleted().isDeleted())
                .map(CourseTutor::getTutor)
                .map(TutorResponse::withNoAccountInfo)
                .toList()
                : null;

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(course);

        if (includeTemplateClassroom && !includeClassroom)
            this.detailInfo = CourseDetailInfoResponse.includeTemplateClassroom(course);
        else if (includeClassroom && !includeTemplateClassroom)
            this.detailInfo = CourseDetailInfoResponse.includeClassroom(course);
        else if (includeTemplateClassroom && includeClassroom)
            this.detailInfo = CourseDetailInfoResponse.includeAll(course);
    }

    public static CourseResponse infoWithDetail(Course course) {
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
