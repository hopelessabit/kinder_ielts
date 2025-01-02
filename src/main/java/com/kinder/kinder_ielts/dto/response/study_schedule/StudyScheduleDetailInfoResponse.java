package com.kinder.kinder_ielts.dto.response.study_schedule;

import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.response.classroom_link.ClassroomLinkResponse;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.dto.response.study_material.StudyMaterialResponse;
import com.kinder.kinder_ielts.dto.response.warm_up_test.WarmUpTestResponse;
import com.kinder.kinder_ielts.entity.StudySchedule;
import lombok.Getter;

import java.util.List;

@Getter
public class StudyScheduleDetailInfoResponse {
    private List<ClassroomLinkResponse> classroomLinks;
    private List<WarmUpTestResponse> warmUpTests;
    private List<HomeworkResponse> homeworks;
    private List<StudyMaterialResponse> studyMaterials;

    public StudyScheduleDetailInfoResponse(StudySchedule studySchedule) {
        this.classroomLinks = studySchedule.getClassroomLinks() != null ? studySchedule.getClassroomLinks()
                .stream()
                .map(ClassroomLinkResponse::info)
                .toList() : null;
        this.warmUpTests = studySchedule.getWarmUpTests() != null ? studySchedule.getWarmUpTests()
                .stream()
                .map(WarmUpTestResponse::info)
                .toList() : null;
        this.homeworks = studySchedule.getHomework() != null ? studySchedule.getHomework()
                .stream()
                .map(HomeworkResponse::info)
                .toList() : null;
        this.studyMaterials = studySchedule.getStudyMaterials() != null ? studySchedule.getStudyMaterials()
                .stream()
                .map(StudyMaterialResponse::info)
                .toList() : null;
    }

    public static StudyScheduleDetailInfoResponse from(StudySchedule studySchedule) {
        return new StudyScheduleDetailInfoResponse(studySchedule);
    }
}
