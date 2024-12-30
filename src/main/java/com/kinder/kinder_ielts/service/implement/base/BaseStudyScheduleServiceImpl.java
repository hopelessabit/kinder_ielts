package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.repository.StudyScheduleRepository;
import com.kinder.kinder_ielts.service.base.BaseStudyScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    }
}