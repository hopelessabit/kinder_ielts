package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.StudyScheduleStatus;
import com.kinder.kinder_ielts.dto.request.study_schedule.CreateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.study_schedule.UpdateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseClassroomService;
import com.kinder.kinder_ielts.service.base.BaseStudyScheduleService;
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
public class StudyScheduleServiceImpl {
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseAccountService baseAccountService;
    private final BaseClassroomService baseClassroomService;
    public StudyScheduleResponse createStudySchedule(String classroomId, CreateStudyScheduleRequest request, String failMessage) {
        StudySchedule studySchedule = ModelMapper.map(request);

        String createBy = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for creator ID: {}", createBy);
        Account creator = baseAccountService.get(createBy, IsDelete.NOT_DELETED, failMessage);
        studySchedule.setCreateBy(creator);

        Classroom classroom = baseClassroomService.get(classroomId, IsDelete.NOT_DELETED, failMessage);
        studySchedule.setClassroom(classroom);

        return StudyScheduleResponse.detailWithDetail(baseStudyScheduleService.create(studySchedule, failMessage));
    }

    public StudyScheduleResponse getInfoWithDetail(String id) {
        return StudyScheduleResponse.infoWithDetail(baseStudyScheduleService.get(id, IsDelete.NOT_DELETED, StudyScheduleMessage.NOT_FOUND));
    }

    public List<StudyScheduleResponse> getAllInfo() {
        return baseStudyScheduleService.all().stream().map(StudyScheduleResponse::detailWithDetail).toList();
    }

    public StudyScheduleResponse updateInfo(String id, UpdateStudyScheduleRequest request, String failMessage) {
        StudySchedule studySchedule = baseStudyScheduleService.get(id, IsDelete.NOT_DELETED, failMessage);

        performUpdateInfo(studySchedule, request, failMessage);
        return StudyScheduleResponse.detail(studySchedule);
    }

    private void performUpdateInfo(StudySchedule studySchedule, UpdateStudyScheduleRequest request, String failMessage) {
        studySchedule.setTitle(CompareUtil.compare(request.getTitle().trim(), studySchedule.getTitle()));
        studySchedule.setDescription(CompareUtil.compare(request.getDescription().trim(), studySchedule.getDescription()));
        studySchedule.setFromTime(CompareUtil.compare(request.getFromTime(), studySchedule.getFromTime()));
        studySchedule.setToTime(CompareUtil.compare(request.getToTime(), studySchedule.getToTime()));

        baseStudyScheduleService.update(studySchedule, failMessage);
    }

    public Void delete(String id, String deleteFailed) {
        baseStudyScheduleService.delete(id, deleteFailed);
        return null;
    }

    public StudyScheduleResponse getDetailWithDetail(String id) {
        return StudyScheduleResponse.detailWithDetail(baseStudyScheduleService.get(id, IsDelete.NOT_DELETED, StudyScheduleMessage.NOT_FOUND));
    }

    public StudyScheduleResponse updateViewStatus(String studyScheduleId, String failMessage){

        StudySchedule studySchedule = baseStudyScheduleService.get(studyScheduleId, IsDelete.NOT_DELETED, failMessage);

        studySchedule.setStatus(studySchedule.getStatus().equals(StudyScheduleStatus.VIEW) ? StudyScheduleStatus.HIDDEN : StudyScheduleStatus.VIEW);

        studySchedule.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());

        baseStudyScheduleService.update(studySchedule, failMessage);
        return StudyScheduleResponse.detail(studySchedule);
    }
}
