package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.ClassroomLinkStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.ClassroomLink;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.ClassroomLinkRepository;
import com.kinder.kinder_ielts.service.base.BaseClassroomLinkService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseClassroomLinkServiceImpl extends BaseEntityServiceImpl<ClassroomLink, String> implements BaseClassroomLinkService {
    private final ClassroomLinkRepository classroomLinkrepository;

    @Override
    protected BaseEntityRepository<ClassroomLink, String> getRepository() {
        return classroomLinkrepository;
    }

    @Override
    protected String getEntityName() {
        return "[Classroom Link]";
    }

    @Override
    protected String getEntityId(ClassroomLink entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(ClassroomLink entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.setStatus(ClassroomLinkStatus.HIDDEN);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<ClassroomLink> entity, Account modifier, ZonedDateTime currentTime) {
        for (ClassroomLink classroomLink : entity) {
            classroomLink.setIsDeleted(IsDelete.DELETED);
            classroomLink.setStatus(ClassroomLinkStatus.HIDDEN);
            classroomLink.updateAudit(modifier, currentTime);
        }
    }
}
