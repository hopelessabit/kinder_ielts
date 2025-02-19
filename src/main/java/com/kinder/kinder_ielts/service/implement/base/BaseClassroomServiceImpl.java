package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.repository.ClassroomRepository;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseClassroomService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseClassroomServiceImpl extends BaseEntityServiceImpl<Classroom, String> implements BaseClassroomService {
    private final BaseAccountService baseAccountService;
    private final ClassroomRepository classroomRepository;

    /**
     * Provides the repository instance for the base class.
     */
    @Override
    protected ClassroomRepository getRepository() {
        return classroomRepository;
    }

    /**
     * Returns the entity name for logging purposes.
     */
    @Override
    protected String getEntityName() {
        return "[Class]";
    }

    /**
     * Retrieves the unique identifier (ID) of the entity.
     */
    @Override
    protected String getEntityId(Classroom entity) {
        return entity.getId();
    }

    /**
     * Marks a Class entity as deleted.
     */
    @Override
    protected void markAsDeleted(Classroom classroom) {
        classroom.setIsDeleted(IsDelete.DELETED);
        classroom.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<Classroom> entity, Account modifier, ZonedDateTime currentTime) {
        for (Classroom classroom : entity) {
            classroom.setIsDeleted(IsDelete.DELETED);
            classroom.updateAudit(modifier, currentTime);
        }
    }

    @Override
    public Page<Classroom> findAll(Specification<Classroom> classroomSpecification, Pageable unsortedPageable) {
        return classroomRepository.findAll(classroomSpecification, unsortedPageable);
    }

    @Override
    public Page<Classroom> findAll(Specification<Classroom> classroomSpecification) {
        return classroomRepository.findAll(classroomSpecification);
    }

    @Override
    public Classroom getByStudyMaterialId(String studyMaterialId) {
        return getByStudyMaterialId(studyMaterialId, false);
    }

    @Override
    public Classroom getByStudyMaterialId(String studyMaterialId, Boolean require) {
        if (Boolean.FALSE.equals(require)) {
            return classroomRepository.findByStudyMaterialId(studyMaterialId).orElse(null);
        }
        return classroomRepository.findByStudyMaterialId(studyMaterialId)
                .orElseThrow(() -> new NotFoundException(ClassroomMessage.NOT_FOUND));
    }

    public Classroom getByIdWithStudentId(String classroomId, String studentId, String failMessage) {
        return classroomRepository.findByIdWithStudentId(classroomId, studentId)
                .orElseThrow(() -> new NotFoundException(failMessage));
    }
}