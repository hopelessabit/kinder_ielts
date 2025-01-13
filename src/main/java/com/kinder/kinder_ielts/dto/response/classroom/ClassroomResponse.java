package com.kinder.kinder_ielts.dto.response.classroom;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
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
    private String timeDescription;
    private OffsetTime fromTime;
    private OffsetTime toTime;
    private ZonedDateTime startDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClassroomDetailInfoResponse detailInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public ClassroomResponse(Classroom classroom, boolean includeInfoForAdmin, boolean includeDetail) {
        this.id = classroom.getId();
        this.description = classroom.getDescription();
        this.fromTime = classroom.getFromTime();
        this.toTime = classroom.getToTime();
        this.startDate = classroom.getStartDate();
        if (includeDetail)
            this.detailInfo = ClassroomDetailInfoResponse.info(classroom);

        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(classroom);
    }

    public static ClassroomResponse infoWithDetails(Classroom classroom) {
        return new ClassroomResponse(classroom, false, true);
    }

    public static ClassroomResponse info(Classroom classroom) {
        return new ClassroomResponse(classroom, false, false);
    }

    public static ClassroomResponse detailWithDetails(Classroom classroom) {
        return new ClassroomResponse(classroom, true, true);
    }

    public static ClassroomResponse detail(Classroom classroom) {
        return new ClassroomResponse(classroom, true, false);
    }
}
