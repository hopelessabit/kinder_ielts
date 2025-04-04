package com.kinder.kinder_ielts.service.implement.base.template;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TemplateStudyScheduleRepository;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.service.implement.base.BaseEntityServiceImpl;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseTemplateStudyScheduleServiceImpl extends BaseEntityServiceImpl<TemplateStudySchedule, String> implements BaseTemplateStudyScheduleService {
    private final TemplateStudyScheduleRepository templateStudyScheduleRepository;

    @Override
    protected BaseEntityRepository<TemplateStudySchedule, String> getRepository() {
        return templateStudyScheduleRepository;
    }

    @Override
    protected String getEntityName() {
        return "Template Study Schedule";
    }

    @Override
    protected String getEntityId(TemplateStudySchedule entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(TemplateStudySchedule entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<TemplateStudySchedule> entity, Account modifier, ZonedDateTime currentTime) {
        for (TemplateStudySchedule templateStudySchedule : entity) {
            templateStudySchedule.setIsDeleted(IsDelete.DELETED);
            templateStudySchedule.updateAudit(modifier, currentTime);
        }
    }
}
