package com.kinder.kinder_ielts.dto.response.template.study_schedule;

import com.kinder.kinder_ielts.dto.response.template.classroom_link.TemplateClassroomLinkResponse;
import com.kinder.kinder_ielts.dto.response.template.homework.TemplateHomeworkResponse;
import com.kinder.kinder_ielts.dto.response.template.study_material.TemplateStudyMaterialResponse;
import com.kinder.kinder_ielts.dto.response.template.warm_up_test.TemplateWarmUpTestResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import lombok.Getter;

import java.util.List;

@Getter
public class TemplateStudyScheduleDetailInfoResponse {
    private List<TemplateClassroomLinkResponse> classroomLinks;
    private List<TemplateWarmUpTestResponse> warmUpTests;
    private List<TemplateHomeworkResponse> homeworks;
    private List<TemplateStudyMaterialResponse> studyMaterials;

    public TemplateStudyScheduleDetailInfoResponse(TemplateStudySchedule studySchedule) {
        this.classroomLinks = studySchedule.getClassroomLinks() != null ? studySchedule.getClassroomLinks()
                .stream()
                .map(TemplateClassroomLinkResponse::info)
                .toList() : null;
        this.warmUpTests = studySchedule.getWarmUpTests() != null ? studySchedule.getWarmUpTests()
                .stream()
                .map(TemplateWarmUpTestResponse::info)
                .toList() : null;
        this.homeworks = studySchedule.getHomework() != null ? studySchedule.getHomework()
                .stream()
                .map(TemplateHomeworkResponse::info)
                .toList() : null;
        this.studyMaterials = studySchedule.getStudyMaterials() != null ? studySchedule.getStudyMaterials()
                .stream()
                .map(TemplateStudyMaterialResponse::info)
                .toList() : null;
    }

    public static TemplateStudyScheduleDetailInfoResponse from(TemplateStudySchedule studySchedule) {
        return new TemplateStudyScheduleDetailInfoResponse(studySchedule);
    }
}
