package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.id.ClassroomTutorId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.ClassroomTutorRepository;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseClassroomTutorService;

import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseClassroomTutorServiceImpl extends BaseEntityServiceImpl<ClassroomTutor, ClassroomTutorId> implements BaseClassroomTutorService {
    private final ClassroomTutorRepository classroomTutorRepository;
    private final BaseAccountService baseAccountService;

    @Override
    protected BaseEntityRepository<ClassroomTutor, ClassroomTutorId> getRepository() {
        return classroomTutorRepository;
    }

    @Override
    protected String getEntityName() {
        return "[Classroom Tutor]";
    }

    @Override
    protected ClassroomTutorId getEntityId(ClassroomTutor entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(ClassroomTutor entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.setModifyTime(ZonedDateTime.now());

        String modifier = SecurityContextHolderUtil.getAccountId();
        log.debug("Modifier [Account] ID fetched from security context: {}", modifier);

        log.debug("Fetching [account] for modifier ID: {}", modifier);
        Account account = baseAccountService.get(modifier, IsDelete.NOT_DELETED, ClassroomMessage.CREATE_FAILED);
        entity.setCreateBy(account);
        log.debug("[Account] fetched successfully and assigned to {} modifier.", getEntityName());
    }
}
