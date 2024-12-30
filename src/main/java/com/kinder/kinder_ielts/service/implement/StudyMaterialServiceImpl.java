package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.dto.request.study_material.CreateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.dto.response.study_material.StudyMaterialResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.StudyMaterial;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyMaterialServiceImpl {
    private final BaseAccountService baseAccountService;
    private final BaseStudentService baseStudentService;
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseTutorService baseTutorService;
    private final BaseStudyMaterialService baseStudyMaterialService;

    public StudyMaterialResponse createStudyMaterial(String scheduleId, CreateStudyMaterialRequest request, String failMessage){
        StudyMaterial studyMaterial = ModelMapper.map(request);

        String createById = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for creator ID: {}", createById);
        Account account = baseAccountService.get(createById, IsDelete.NOT_DELETED, failMessage);
        studyMaterial.setCreateBy(account);

        StudySchedule studySchedule = baseStudyScheduleService.get(scheduleId, IsDelete.NOT_DELETED, failMessage);
        studyMaterial.setBeLongTo(studySchedule);

        if (studyMaterial.getPrivacyStatus().equals(StudyMaterialStatus.PRIVATE) && request.getStudentIds() != null && !request.getStudentIds().isEmpty()) {
            List<Student> students = baseStudentService.get(request.getStudentIds(), AccountStatus.ACTIVE, failMessage);
            studyMaterial.setStudyMaterialsForStudents(students);
        }

        StudyMaterial result = baseStudyMaterialService.create(studyMaterial, failMessage);
        return StudyMaterialResponse.detailWithDetails(result);
    }

    public StudyMaterialResponse getInfo(String id) {
        return StudyMaterialResponse.detailWithDetails(baseStudyMaterialService.get(id, IsDelete.NOT_DELETED, StudyScheduleMessage.NOT_FOUND));
    }
}
