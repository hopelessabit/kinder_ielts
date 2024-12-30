package com.kinder.kinder_ielts.dto.response.classroom;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class ClassroomResponse {
    private String id;
    private String description;
    private String timeDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CourseResponse belongToCourse;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<StudyScheduleResponse> studySchedules;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TutorResponse> tutorResponses;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse createBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime modifyTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse modifyBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IsDeletedResponse isDeleted;

    public ClassroomResponse(Classroom classroom, boolean includeInfoForAdmin, boolean includeDetail) {
        this.id = classroom.getId();
        this.description = classroom.getDescription();
        this.timeDescription = classroom.getTimeDescription();
        this.belongToCourse = CourseResponse.info(classroom.getBelongToCourse());

        if (includeDetail){
            if (classroom.getStudySchedules() != null)
                this.studySchedules = classroom.getStudySchedules().stream().map(StudyScheduleResponse::info).toList();

            this.belongToCourse = CourseResponse.info(classroom.getBelongToCourse());

            if (classroom.getClassroomTutors() != null)
                this.tutorResponses = classroom.getClassroomTutors().stream().map(ClassroomTutor::getTutor).map(TutorResponse::withNoAccountInfo).toList();
        }

        if (includeInfoForAdmin) {
            this.createTime = classroom.getCreateTime() != null
                    ? classroom.getCreateTime()
                    : null;

            this.modifyTime = classroom.getModifyTime() != null
                    ? classroom.getModifyTime()
                    : null;

            this.createBy = SubAccountResponse.from(classroom.getCreateBy());

            this.modifyBy = SubAccountResponse.from(classroom.getModifyBy());
        }
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
