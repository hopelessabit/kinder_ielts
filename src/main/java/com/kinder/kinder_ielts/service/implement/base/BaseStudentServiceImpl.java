package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.repository.StudentRepository;
import com.kinder.kinder_ielts.response_message.StudentMessage;
import com.kinder.kinder_ielts.service.base.BaseStudentService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Implementation of {@link BaseStudentService}.
 * Provides CRUD operations for {@link Student} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BaseStudentServiceImpl extends BaseEntityServiceImpl<Student, String> implements BaseStudentService {

    private final StudentRepository studentRepository;

    @Override
    protected StudentRepository getRepository() {
        return studentRepository;
    }

    @Override
    protected String getEntityName() {
        return "Student";
    }

    @Override
    protected String getEntityId(Student entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(Student entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
        if (entity.getAccount() != null) {
            entity.getAccount().setIsDeleted(IsDelete.DELETED);
            entity.getAccount().setStatus(AccountStatus.INACTIVE);
            entity.getAccount().updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
        }
    }

    @Override
    protected void markAsDeleted(List<Student> entity, Account modifier, ZonedDateTime currentTime) {
        for (Student student : entity) {
            student.setIsDeleted(IsDelete.DELETED);
            student.updateAudit(modifier, currentTime);
            if (student.getAccount() != null) {
                student.getAccount().setIsDeleted(IsDelete.DELETED);
                student.getAccount().setStatus(AccountStatus.INACTIVE);
                student.getAccount().updateAudit(modifier, currentTime);
            }
        }
    }

    @Override
    public List<Student> get(List<String> ids, AccountStatus accountStatus, String message) {
        log.info("Fetching {}s with IDs: {} and status: {}", getEntityName(), ids, accountStatus);
        List<Student> students = getRepository().findByIdInAndAccount_Status(ids, accountStatus);

        if (students.isEmpty()) {
            log.warn("No {} found for IDs: {}", getEntityName(), ids);
            throw new NotFoundException(message, Error.build(message, ids));
        }

        if (students.size() != ids.size()) {
            List<String> foundIds = students.stream()
                    .map(this::getEntityId)
                    .toList();
            List<String> idsNotFound = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            log.warn("Partial {} found. Missing IDs: {}", getEntityName(), idsNotFound);
            throw new NotFoundException(message, Error.build(message, idsNotFound));
        }

        log.info("Successfully fetched {}: {}", getEntityName(), students);
        return students;
    }

    @Override
    public List<Student> getByClassId(String classId, String failMessage) {
        List<Student> students = studentRepository.findAllByClassId(classId, IsDelete.NOT_DELETED);
        if (students.isEmpty()) {
            throw new NotFoundException(failMessage, Error.build(StudentMessage.NOT_FOUND_IN_CLASS, List.of(classId)));
        }
        return students;
    }

    @Override
    public List<Student> getByClassId(String classId) {
        return studentRepository.findAllByClassId(classId, IsDelete.NOT_DELETED);
    }

    public void saveStudent(Student student) {
        studentRepository.saveStudent(student.getId(), student.getCreateTime(), student.getIsDeleted(), student.getEmail(), student.getFirstName(), student.getLastName(), student.getMiddleName(), student.getFullName(), student.getPhone(), student.getCreateBy().getId());
    }
}