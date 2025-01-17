package com.kinder.kinder_ielts.service.implement.base.template;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudyMaterial;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TemplateStudyMaterialRepository;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyMaterialService;

import com.kinder.kinder_ielts.service.implement.base.BaseEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseTemplateStudyMaterialServiceImpl extends BaseEntityServiceImpl<TemplateStudyMaterial, String> implements BaseTemplateStudyMaterialService {
    private final TemplateStudyMaterialRepository templateStudyMaterialRepository;

    @Override
    protected BaseEntityRepository<TemplateStudyMaterial, String> getRepository() {
        return templateStudyMaterialRepository;
    }

    @Override
    protected String getEntityName() {
        return "Template Study Material";
    }

    @Override
    protected String getEntityId(TemplateStudyMaterial entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(TemplateStudyMaterial entity) {
        entity.setIsDeleted(IsDelete.DELETED);
    }
}
