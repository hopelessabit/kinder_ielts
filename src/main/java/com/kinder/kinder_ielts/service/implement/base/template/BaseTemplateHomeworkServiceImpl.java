package com.kinder.kinder_ielts.service.implement.base.template;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.course_template.TemplateHomework;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TemplateHomeworkRepository;
import com.kinder.kinder_ielts.service.base.BaseTemplateHomeworkService;
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
public class BaseTemplateHomeworkServiceImpl extends BaseEntityServiceImpl<TemplateHomework, String> implements BaseTemplateHomeworkService {
    private final TemplateHomeworkRepository templateHomeworkRepository;

    @Override
    protected BaseEntityRepository<TemplateHomework, String> getRepository() {
        return templateHomeworkRepository;
    }

    @Override
    protected String getEntityName() {
        return "Template Homework";
    }

    @Override
    protected String getEntityId(TemplateHomework entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(TemplateHomework entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<TemplateHomework> entity, Account modifier, ZonedDateTime currentTime) {
        for (TemplateHomework templateHomework : entity) {
            templateHomework.setIsDeleted(IsDelete.DELETED);
            templateHomework.updateAudit(modifier, currentTime);
        }
    }
}
