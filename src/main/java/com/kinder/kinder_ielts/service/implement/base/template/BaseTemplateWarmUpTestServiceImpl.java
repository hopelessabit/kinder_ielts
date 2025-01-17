package com.kinder.kinder_ielts.service.implement.base.template;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.course_template.TemplateWarmUpTest;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TemplateWarmUpTestRepository;
import com.kinder.kinder_ielts.service.base.BaseTemplateWarmUpTestService;
import com.kinder.kinder_ielts.service.implement.base.BaseEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    }
}
