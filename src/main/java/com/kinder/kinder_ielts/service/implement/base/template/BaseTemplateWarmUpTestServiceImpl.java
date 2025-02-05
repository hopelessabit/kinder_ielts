package com.kinder.kinder_ielts.service.implement.base.template;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.course_template.TemplateWarmUpTest;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TemplateWarmUpTestRepository;
import com.kinder.kinder_ielts.service.base.BaseTemplateWarmUpTestService;
import com.kinder.kinder_ielts.service.implement.base.BaseEntityServiceImpl;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseTemplateWarmUpTestServiceImpl extends BaseEntityServiceImpl<TemplateWarmUpTest, String> implements BaseTemplateWarmUpTestService {
    private final TemplateWarmUpTestRepository templateWarmUpTestRepository;
    @Override
    protected BaseEntityRepository<TemplateWarmUpTest, String> getRepository() {
        return templateWarmUpTestRepository;
    }

    @Override
    protected String getEntityName() {
        return "Template Warm Up Test";
    }

    @Override
    protected String getEntityId(TemplateWarmUpTest entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(TemplateWarmUpTest entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<TemplateWarmUpTest> entity, Account modifier, ZonedDateTime currentTime) {
        for (TemplateWarmUpTest templateWarmUpTest : entity) {
            templateWarmUpTest.setIsDeleted(IsDelete.DELETED);
            templateWarmUpTest.updateAudit(modifier, currentTime);
        }
    }
}
