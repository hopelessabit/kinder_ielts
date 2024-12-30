package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.repository.ClassroomRepository;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseClassroomService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

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
        return "Class";
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
        String modifierId = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for modifier ID: {}", modifierId);
        Account modifier = baseAccountService.get(modifierId, IsDelete.NOT_DELETED, CourseMessage.DELETE_FAILED);

        classroom.setModifyBy(modifier);
        classroom.setModifyTime(ZonedDateTime.now());
        classroom.setIsDeleted(IsDelete.DELETED);
    }
}