package com.kinder.kinder_ielts.dto.response.classroom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import lombok.Getter;

import java.util.List;
@Getter
public class ClassroomDetailInfoResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<StudyScheduleResponse> studySchedules;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TutorResponse> tutorResponses;

    public ClassroomDetailInfoResponse(Classroom classroom) {
        this.studySchedules = classroom.getStudySchedules() != null ? classroom.getStudySchedules().stream()
                .filter(studySchedule -> !studySchedule.getIsDeleted().isDeleted())
                .map(StudyScheduleResponse::infoWithDetail).toList() : null;
        this.tutorResponses = classroom.getClassroomTutors() != null ? classroom.getClassroomTutors().stream()
                .filter(classroomTutor -> !classroomTutor.getIsDeleted().isDeleted())
                .map(ClassroomTutor::getTutor)
                .map(TutorResponse::withNoAccountInfo)
                .toList() : null;
    }

    public ClassroomDetailInfoResponse(Classroom classroom, boolean includeTutor, boolean includeStudySchedule) {
        if (includeStudySchedule)
            this.studySchedules = classroom.getStudySchedules() != null ? classroom.getStudySchedules().stream()
                .filter(studySchedule -> !studySchedule.getIsDeleted().isDeleted())
                .map(StudyScheduleResponse::infoWithDetail).toList() : null;
        if (includeTutor)
            this.tutorResponses = classroom.getClassroomTutors() != null ? classroom.getClassroomTutors().stream()
                .filter(classroomTutor -> !classroomTutor.getIsDeleted().isDeleted())
                .map(ClassroomTutor::getTutor)
                .map(TutorResponse::withNoAccountInfo)
                .toList() : null;
    }

    public static ClassroomDetailInfoResponse info(Classroom classroom) {
        return new ClassroomDetailInfoResponse(classroom);
    }
    public static ClassroomDetailInfoResponse includeTutor(Classroom classroom) {
        return new ClassroomDetailInfoResponse(classroom, true, false);
    }
    public static ClassroomDetailInfoResponse includeStudySchedule(Classroom classroom) {
        return new ClassroomDetailInfoResponse(classroom, false, true);
    }
    public static ClassroomDetailInfoResponse includeAll(Classroom classroom) {
        return new ClassroomDetailInfoResponse(classroom, true, true);
    }
}
