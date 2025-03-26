package com.kinder.kinder_ielts.service.implement.base;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.id.StudentHomeworkId;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.StudentHomeworkRepository;
import com.kinder.kinder_ielts.response_message.StudentHomeworkMessage;
import com.kinder.kinder_ielts.service.base.BaseStudentHomeworkService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<StudentHomework> getByHomeworkId(String homeworkId, IsDelete isDelete, String failMessage) {
        List<StudentHomework> studentHomeworks = studentHomeworkRepository.findById_HomeworkIdAndIsDeleted(homeworkId, isDelete);
        if (studentHomeworks.isEmpty())
            throw new NotFoundException(failMessage, Error.build(StudentHomeworkMessage.NOT_FOUND, Map.of("homeworkId", homeworkId)));
        return studentHomeworks;
    }

    @Override
    public Page<StudentHomework> getByHomeworkId(String homeworkId, Pageable pageable, IsDelete isDelete, String failMessage) {
        return studentHomeworkRepository.findByHomeworkIdAndIsDeleted(homeworkId, isDelete, pageable);
    }

    @Override
    public List<StudentHomework> getByHomeworkId(String id, String failMessage) {
        return studentHomeworkRepository.findById_HomeworkId(id);
    }
}
