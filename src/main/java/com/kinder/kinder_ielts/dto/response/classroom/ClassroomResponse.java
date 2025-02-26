package com.kinder.kinder_ielts.dto.response.classroom;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kinder.kinder_ielts.constant.DateOfWeek;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.id.ClassroomWeeklyScheduleId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomWeeklySchedule;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class ClassroomResponse {
    private String id;
    private String description;
    private String code;
    private OffsetTime fromTime;
    private OffsetTime toTime;
    private List<StatusResponse<DateOfWeek>> weeklySchedule;
    private ZonedDateTime startDate;
    private List<TutorResponse> tutorResponses;
    private Integer numberOfSlots;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourseResponse belongToCourse;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClassroomDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public ClassroomResponse(Classroom classroom, boolean includeInfoForAdmin, boolean includeCourse, boolean includeTutor, boolean includeSchedule) {
        this.id = classroom.getId();
        this.description = classroom.getDescription();
        this.code = classroom.getCode();
        this.fromTime = classroom.getFromTime();
        this.toTime = classroom.getToTime();
        this.numberOfSlots = classroom.getWeeklySchedule().size();
        this.weeklySchedule = classroom.getWeeklySchedule() != null ? classroom.getWeeklySchedule().stream()
                .map(ClassroomWeeklySchedule::getId)
                .map(ClassroomWeeklyScheduleId::getDayOfWeek)
                .map(StatusResponse::from)
                .toList() : null;
        this.startDate = classroom.getStartDate();
        if (includeTutor)
            this.tutorResponses = classroom.getClassroomTutors() != null ? classroom.getClassroomTutors().stream()
                .filter(classroomTutor -> !classroomTutor.getIsDeleted().isDeleted())
                .map(ClassroomTutor::getTutor)
                .map(TutorResponse::withNoAccountInfo)
                .toList() : null;
        if (includeCourse)
            this.belongToCourse = CourseResponse.info(classroom.getCourse());

        this.detailInfo = new ClassroomDetailInfoResponse(classroom, includeTutor, includeSchedule);

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(classroom);
    }

    public ClassroomResponse(Classroom classroom, boolean includeInfoForAdmin, boolean includeDetail) {
        this.id = classroom.getId();
        this.description = classroom.getDescription();
        this.code = classroom.getCode();
        this.fromTime = classroom.getFromTime();
        this.toTime = classroom.getToTime();
        this.startDate = classroom.getStartDate();
        this.numberOfSlots = classroom.getWeeklySchedule().size();
        this.weeklySchedule = classroom.getWeeklySchedule() != null ? classroom.getWeeklySchedule().stream()
                .map(ClassroomWeeklySchedule::getId)
                .map(ClassroomWeeklyScheduleId::getDayOfWeek)
                .map(StatusResponse::from)
                .toList() : null;
        this.tutorResponses = classroom.getClassroomTutors() != null ? classroom.getClassroomTutors().stream()
                .filter(classroomTutor -> !classroomTutor.getIsDeleted().isDeleted())
                .map(ClassroomTutor::getTutor)
                .map(TutorResponse::withNoAccountInfo)
                .toList() : null;
        if (includeDetail){
            this.detailInfo = ClassroomDetailInfoResponse.info(classroom);
            this.belongToCourse = CourseResponse.info(classroom.getCourse());
        }

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(classroom);
    }

    public static ClassroomResponse infoWithDetails(Classroom classroom) {
        return new ClassroomResponse(classroom, false, true);
    }

    public static ClassroomResponse info(Classroom classroom) {
        return new ClassroomResponse(classroom, false, true);
    }

    public static ClassroomResponse detailWithDetails(Classroom classroom) {
        return new ClassroomResponse(classroom, true, true);
    }

    public static ClassroomResponse detail(Classroom classroom) {
        return new ClassroomResponse(classroom, true, false);
    }

    public static ClassroomResponse includeAll(Classroom classroom) {
        return new ClassroomResponse(classroom, true, true, true, true);
    }

    public static ClassroomResponse includeTutor(Classroom classroom) {
        return new ClassroomResponse(classroom, false, false, true, false);
    }
}
