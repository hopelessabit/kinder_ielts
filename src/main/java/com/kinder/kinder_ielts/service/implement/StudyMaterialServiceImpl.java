package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.*;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.dto.request.material_link.CreateMaterialLinkRequest;
import com.kinder.kinder_ielts.dto.request.study_material.CreateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.study_material.ModifyStudyMaterialStatusRequest;
import com.kinder.kinder_ielts.dto.request.study_material.UpdateMaterialLinksRequest;
import com.kinder.kinder_ielts.dto.request.study_material.UpdateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.response.study_material.StudyMaterialResponse;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.exception.BadRequestException;
import com.kinder.kinder_ielts.exception.UnauthorizeException;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.BaseMessage;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.StudyMaterialMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyMaterialServiceImpl {
    private final BaseAccountService baseAccountService;
    private final BaseStudentService baseStudentService;
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseTutorService baseTutorService;
    private final BaseClassroomService baseClassroomService;
    private final BaseStudyMaterialService baseStudyMaterialService;
    private final BaseClassroomStudentService baseClassroomStudentService;
    private final StudentServiceImpl studentServiceImpl;
    private final BaseMaterialLinkService baseMaterialLinkService;

    public Page<StudyMaterialResponse> getByStudyScheduleId(String studyScheduleId, Pageable pageable, IsDelete isDelete, boolean includeStudySchedule, boolean includeExtendDetail) {
        StudySchedule studySchedule = baseStudyScheduleService.get(studyScheduleId, IsDelete.NOT_DELETED, StudyScheduleMessage.NOT_FOUND);
        Page<StudyMaterial> studyMaterials = baseStudyMaterialService.getByStudyScheduleId(studyScheduleId, pageable, isDelete);
        return studyMaterials.map(sm -> new StudyMaterialResponse(sm, includeExtendDetail, includeStudySchedule));
    }

    public StudyMaterialResponse get(String studyMaterialId, String failMessage) {
        Account account = SecurityContextHolderUtil.getAccount();

        if (account.getRole().equals(Role.TUTOR)
                && baseClassroomService.getByStudyMaterialId(studyMaterialId) == null
        )
            throw new UnauthorizeException(failMessage, Error.build(BaseMessage.NOT_ALLOW));

        StudyMaterial studyMaterial = baseStudyMaterialService.get(studyMaterialId, IsDelete.NOT_DELETED, failMessage);
        if (account.getRole().equals(Role.STUDENT)
                && studyMaterial.getPrivacyStatus().equals(StudyMaterialStatus.PRIVATE)
                && studyMaterial.getStudyMaterialsForStudents().stream().noneMatch(a -> a.getId().equals(account.getId()))
        ) {
            throw new UnauthorizeException(failMessage, Error.build(BaseMessage.NOT_ALLOW));
        }

        if (account.getRole().equals(Role.ADMIN) || account.getRole().equals(Role.TUTOR)) {
            return StudyMaterialResponse.detailWithDetails(studyMaterial);
        }
        return StudyMaterialResponse.infoWithDetails(studyMaterial);
    }

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
            studyMaterial.setStudyMaterialsForStudents(new HashSet<>(students));
        }
        StudyMaterial result = baseStudyMaterialService.create(studyMaterial, failMessage);

        if (request.getMaterialLinks() != null && !request.getMaterialLinks().isEmpty()){
            List<MaterialLink> materialLinks = baseMaterialLinkService.create(
                    request.getMaterialLinks()
                            .stream()
                            .map(a -> ModelMapper.map(a, studyMaterial, account, ZonedDateTime.now()))
                            .toList(),
                    failMessage);
            result.setMaterialLinks(materialLinks);
            result = baseStudyMaterialService.update(result, failMessage);
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

    public StudyMaterialResponse updateStudyMaterialStatus(String studyMaterialId, ModifyStudyMaterialStatusRequest request, String failMessage) {
        StudyMaterial studyMaterial = baseStudyMaterialService.get(studyMaterialId, IsDelete.NOT_DELETED, failMessage);

        studyMaterial.setPrivacyStatus(request.status);
        if(request.status.equals(StudyMaterialStatus.PUBLIC)){
            studyMaterial.setStudyMaterialsForStudents(null);
        } else {
            List<ClassroomStudent> classroomStudents = baseClassroomStudentService.getClassRoomStudentsByStudyMaterialId(studyMaterialId, IsDelete.NOT_DELETED, ClassroomMessage.NOT_FOUND);
            List<String> studentIdsNotFound = new ArrayList<>();
            List<Student> studyMaterialsForStudents = new ArrayList<>();
            for (String studentId : request.studentIds) {
                ClassroomStudent classroomStudent = classroomStudents.stream().filter(a -> a.getStudent().getId().equals(studentId)).findFirst().orElse(null);
                if (classroomStudent == null) {
                    studentIdsNotFound.add(studentId);
                    continue;
                }
                studyMaterialsForStudents.add(new Student(studentId));
            }

            if (!studentIdsNotFound.isEmpty()) {
                throw new BadRequestException(failMessage, Error.build(StudyMaterialMessage.STUDENT_NOT_FOUND, studentIdsNotFound));
            }

            studyMaterial.setStudyMaterialsForStudents(new HashSet<>(studyMaterialsForStudents));
        }

        baseStudyMaterialService.update(studyMaterial, failMessage);

        return StudyMaterialResponse.detailWithDetails(baseStudyMaterialService.update(studyMaterial, failMessage));
    }

    public StudyMaterialResponse updateMaterialLinks(String studyMaterialId, UpdateMaterialLinksRequest request, String failMessage) {
        StudyMaterial studyMaterial = baseStudyMaterialService.get(studyMaterialId, IsDelete.NOT_DELETED, failMessage);

        List<MaterialLink> newMaterialLinks = studyMaterial.getMaterialLinks().stream().filter(ml -> !request.getRemove().contains(ml.getId())).toList();

        return StudyMaterialResponse.detailWithDetails(baseStudyMaterialService.update(studyMaterial, failMessage));
    }

    public StudyMaterialResponse updateViewStatus(String studyMaterialId, String failMessage) {
        StudyMaterial studyMaterial = baseStudyMaterialService.get(studyMaterialId, IsDelete.NOT_DELETED, failMessage);

        if (studyMaterial.getViewStatus().equals(StudyMaterialViewStatus.VIEW))
            studyMaterial.setViewStatus(StudyMaterialViewStatus.HIDDEN);
        else
            studyMaterial.setViewStatus(StudyMaterialViewStatus.VIEW);

        studyMaterial.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());

        baseStudyMaterialService.update(studyMaterial, failMessage);
        return StudyMaterialResponse.detailWithDetails(studyMaterial);
    }
}
