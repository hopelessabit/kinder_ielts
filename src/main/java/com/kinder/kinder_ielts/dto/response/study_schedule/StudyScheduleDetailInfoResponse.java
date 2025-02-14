package com.kinder.kinder_ielts.dto.response.study_schedule;

import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.response.classroom_link.ClassroomLinkResponse;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.dto.response.study_material.StudyMaterialResponse;
import com.kinder.kinder_ielts.dto.response.warm_up_test.WarmUpTestResponse;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import lombok.Getter;

import java.util.Comparator;
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
                .filter(classroomLink -> !classroomLink.getIsDeleted().isDeleted())
                .sorted(Comparator.comparing(BaseEntity::getCreateTime))
                .map(ClassroomLinkResponse::info)
                .toList() : null;
        this.warmUpTests = studySchedule.getWarmUpTests() != null ? studySchedule.getWarmUpTests()
                .stream()
                .filter(warmUpTest -> !warmUpTest.getIsDeleted().isDeleted())
                .sorted(Comparator.comparing(BaseEntity::getCreateTime))
                .map(WarmUpTestResponse::info)
                .toList() : null;
        this.homeworks = studySchedule.getHomework() != null ? studySchedule.getHomework()
                .stream()
                .filter(homework -> !homework.getIsDeleted().isDeleted())
                .sorted(Comparator.comparing(BaseEntity::getCreateTime))
                .map(HomeworkResponse::info)
                .toList() : null;
        this.studyMaterials = studySchedule.getStudyMaterials() != null ? studySchedule.getStudyMaterials()
                .stream()
                .filter(studyMaterial -> !studyMaterial.getIsDeleted().isDeleted())
                .sorted(Comparator.comparing(BaseEntity::getCreateTime))
                .map(StudyMaterialResponse::info)
                .toList() : null;
    }

    public static StudyScheduleDetailInfoResponse from(StudySchedule studySchedule) {
        return new StudyScheduleDetailInfoResponse(studySchedule);
    }
}
