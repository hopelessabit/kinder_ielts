package com.kinder.kinder_ielts.service.implement.base.template;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudyMaterial;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TemplateStudyMaterialRepository;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyMaterialService;

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
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<TemplateStudyMaterial> entity, Account modifier, ZonedDateTime currentTime) {
        for (TemplateStudyMaterial templateStudyMaterial : entity) {
            templateStudyMaterial.setIsDeleted(IsDelete.DELETED);
            templateStudyMaterial.updateAudit(modifier, currentTime);
        }
    }
}
