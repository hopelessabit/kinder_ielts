package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.dto.request.material_link.CreateMaterialLinkRequest;
import com.kinder.kinder_ielts.dto.request.study_material.CreateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.study_material.ModifyStudyMaterialStatusRequest;
import com.kinder.kinder_ielts.dto.request.study_material.UpdateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.dto.response.study_material.StudyMaterialResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.StudyMaterial;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.service.implement.base.BaseClassroomStudentServiceImpl;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
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
    private final BaseClassroomStudentService baseClassroomStudentService;

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

        if (request.getMaterialLinks() != null && !request.getMaterialLinks().isEmpty()){
            setMaterialLink(result, request.getMaterialLinks(), account, ZonedDateTime.now());
        }

        return StudyMaterialResponse.detailWithDetails(baseStudyMaterialService.update(result, failMessage));
    }

    public void setMaterialLink(StudyMaterial studyMaterial, List<CreateMaterialLinkRequest> materialLinkRequests, Account actor, ZonedDateTime currentTime){
        studyMaterial.setMaterialLinks(materialLinkRequests.stream().map(a -> ModelMapper.map(a, studyMaterial, actor, currentTime)).toList());
    }

    public StudyMaterialResponse getInfo(String id) {
        return StudyMaterialResponse.detailWithDetails(baseStudyMaterialService.get(id, IsDelete.NOT_DELETED, StudyScheduleMessage.NOT_FOUND));
    }

    public StudyMaterialResponse update(String id, UpdateStudyMaterialRequest request, String failMessage) {
        StudyMaterial studyMaterial = baseStudyMaterialService.get(id, IsDelete.NOT_DELETED, failMessage);

        studyMaterial.setTitle(CompareUtil.compare(request.getTitle().trim(), studyMaterial.getTitle()));
        studyMaterial.setDescription(CompareUtil.compare(request.getDescription().trim(), studyMaterial.getDescription()));

        return StudyMaterialResponse.detailWithDetails(baseStudyMaterialService.update(studyMaterial, failMessage));
    }

    public Void delete(String id, String deleteFailed) {
        baseStudyMaterialService.delete(id, deleteFailed);
        return null;
    }

//    public StudyMaterialResponse updateStudyMaterialStatus(String studyMaterialId, ModifyStudyMaterialStatusRequest request, String statusUpdateFailed) {
//        StudyMaterial studyMaterial = baseStudyMaterialService.get(studyMaterialId, IsDelete.NOT_DELETED, statusUpdateFailed);
//
//        studyMaterial.setPrivacyStatus(request.status);
//        List<ClassroomStudent> classroomStudents = baseClassroomStudentService.getByClassroomId()
//        return StudyMaterialResponse.detailWithDetails(baseStudyMaterialService.update(studyMaterial, statusUpdateFailed));
//    }
}
