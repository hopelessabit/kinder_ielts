package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.RollCall;
import com.kinder.kinder_ielts.entity.id.RollCallId;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.RollCallRepository;
import com.kinder.kinder_ielts.response_message.RollCallMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.BaseRollCallService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseRollCallServiceImpl extends BaseEntityServiceImpl<RollCall, RollCallId> implements BaseRollCallService {
    private final RollCallRepository rollCallRepository;
    @Override
    protected BaseEntityRepository<RollCall, RollCallId> getRepository() {
        return rollCallRepository;
    }

    @Override
    protected String getEntityName() {
        return "[Roll call]";
    }

    @Override
    protected RollCallId getEntityId(RollCall entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(RollCall entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<RollCall> entity, Account modifier, ZonedDateTime currentTime) {
        for (RollCall rollCall : entity) {
            rollCall.setIsDeleted(IsDelete.DELETED);
            rollCall.updateAudit(modifier, currentTime);
        }
    }

    @Override
    public List<RollCall> findByStudyScheduleId(String studyScheduleId, IsDelete isDelete, String failMessage) {
        List<RollCall> rollCalls = rollCallRepository.findById_StudyScheduleIdAndIsDeleted(studyScheduleId, isDelete);
        if (rollCalls.isEmpty()) {
            log.error(failMessage);
            throw new NotFoundException(failMessage, Error.build(RollCallMessage.STUDY_SCHEDULE_NOT_FOUND, List.of(studyScheduleId)));
        }
        return rollCalls;
    }

    @Override
    public List<RollCall> findByStudentIdAndClassId(String studentId, String classId, IsDelete isDelete, String failMessage) {
        return rollCallRepository.findById_StudentIdAndId_ClassIdAndIsDeleted(studentId, classId, isDelete);
    }

    @Override
    public List<RollCall> findByStudentIdAndStudyScheduleIds(String studentId, List<String> studyScheduleIds, IsDelete isDelete, String failMessage) {
        return rollCallRepository.findById_StudentIdAndId_StudyScheduleIdInAndIsDeleted(studentId, studyScheduleIds, isDelete);
    }

    @Override
    public Page<RollCall> get(Specification<RollCall> rollCallSpecification, Pageable pageable) {
        return rollCallRepository.findAll(rollCallSpecification, pageable);
    }

}
