package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.TutorCertificate;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TutorCertificateRepository;
import com.kinder.kinder_ielts.service.base.BaseTutorCertificateService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseTutorCertificateServiceImpl extends BaseEntityServiceImpl<TutorCertificate, String> implements BaseTutorCertificateService {
    private final TutorCertificateRepository tutorCertificateRepository;
    @Override
    protected BaseEntityRepository<TutorCertificate, String> getRepository() {
        return tutorCertificateRepository;
    }

    @Override
    protected String getEntityName() {
        return "[Certificate]";
    }

    @Override
    protected String getEntityId(TutorCertificate entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(TutorCertificate entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<TutorCertificate> entity, Account modifier, ZonedDateTime currentTime) {
        for (TutorCertificate tutorCertificate : entity) {
            tutorCertificate.setIsDeleted(IsDelete.DELETED);
            tutorCertificate.updateAudit(modifier, currentTime);
        }
    }
}
