package com.kinder.kinder_ielts.service.implement.base.template;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroomLink;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TemplateClassroomLinkRepository;
import com.kinder.kinder_ielts.service.base.BaseTemplateClassroomLinkService;

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
public class BaseTemplateClassroomLinkServiceImpl extends BaseEntityServiceImpl<TemplateClassroomLink, String> implements BaseTemplateClassroomLinkService {
    private final TemplateClassroomLinkRepository templateClassroomLinkRepository;

    @Override
    protected BaseEntityRepository<TemplateClassroomLink, String> getRepository() {
        return templateClassroomLinkRepository;
    }

    @Override
    protected String getEntityName() {
        return "Template Classroom Link";
    }

    @Override
    protected String getEntityId(TemplateClassroomLink entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(TemplateClassroomLink entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<TemplateClassroomLink> entity, Account modifier, ZonedDateTime currentTime) {
        for (TemplateClassroomLink templateClassroomLink : entity) {
            templateClassroomLink.setIsDeleted(IsDelete.DELETED);
            templateClassroomLink.updateAudit(modifier, currentTime);
        }
    }
}
