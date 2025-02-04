package com.kinder.kinder_ielts.service.implement.base;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.id.StudentHomeworkId;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.StudentHomeworkRepository;
import com.kinder.kinder_ielts.service.base.BaseStudentHomeworkService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseStudentHomeworkServiceImpl extends BaseEntityServiceImpl<StudentHomework, StudentHomeworkId> implements BaseStudentHomeworkService {
    private final StudentHomeworkRepository studentHomeworkRepository;

    @Override
    protected BaseEntityRepository<StudentHomework, StudentHomeworkId> getRepository() {
        return studentHomeworkRepository;
    }

    @Override
    protected String getEntityName() {
        return "Student homework";
    }

    @Override
    protected StudentHomeworkId getEntityId(StudentHomework entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(StudentHomework entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<StudentHomework> entity, Account modifier, ZonedDateTime currentTime) {
        for (StudentHomework studentHomework : entity) {
            studentHomework.setIsDeleted(IsDelete.DELETED);
            studentHomework.updateAudit(modifier, currentTime);
        }
    }
}
