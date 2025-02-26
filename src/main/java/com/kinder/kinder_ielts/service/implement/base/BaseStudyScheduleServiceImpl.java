package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.repository.StudyScheduleRepository;
import com.kinder.kinder_ielts.service.base.BaseStudyScheduleService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseStudyScheduleServiceImpl extends BaseEntityServiceImpl<StudySchedule, String> implements BaseStudyScheduleService {

    private final StudyScheduleRepository studyScheduleRepository;

    @Override
    protected StudyScheduleRepository getRepository() {
        return studyScheduleRepository;
    }

    @Override
    protected String getEntityName() {
        return "StudySchedule";
    }

    @Override
    protected String getEntityId(StudySchedule entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(StudySchedule entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<StudySchedule> entity, Account modifier, ZonedDateTime currentTime) {
        for (StudySchedule studySchedule : entity) {
            studySchedule.setIsDeleted(IsDelete.DELETED);
            studySchedule.updateAudit(modifier, currentTime);
        }
    }

    @Override
    public Set<StudySchedule> findByClassIdWithViewStatus(String classId, IsDelete isDelete, ViewStatus status) {
        return studyScheduleRepository.findByClassroom_IdAndIsDeletedAndStatus(classId, isDelete, status);
    }

    @Override
    public Set<StudySchedule> findByClassId(String classId, IsDelete isDelete, List<ViewStatus> statuses) {
        if (statuses == null || statuses.isEmpty())
            return studyScheduleRepository.findByClassroom_IdAndIsDeletedOrderByFromTimeAsc(classId, isDelete);
        return studyScheduleRepository.findByClassroom_IdAndIsDeletedAndStatusInOrderByFromTimeAsc(classId, isDelete, statuses);
    }

}