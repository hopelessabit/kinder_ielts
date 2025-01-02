package com.kinder.kinder_ielts.dto.response.homework;

import com.kinder.kinder_ielts.dto.response.student_homework.StudentHomeworkResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.Homework;
import lombok.Getter;

import java.util.List;

@Getter
public class HomeworkDetailInfoResponse {
    private List<StudentHomeworkResponse> studentHomeworks;
    private StudyScheduleResponse studySchedule;

    public HomeworkDetailInfoResponse(Homework homework) {
        this.studySchedule = StudyScheduleResponse.info(homework.getBeLongTo());
        this.studentHomeworks = homework.getStudentHomeworks() != null ? homework.getStudentHomeworks()
                .stream()
                .map(StudentHomeworkResponse::info)
                .toList() : null;
    }

    public static HomeworkDetailInfoResponse from(Homework homework) {
        return new HomeworkDetailInfoResponse(homework);
    }
}
