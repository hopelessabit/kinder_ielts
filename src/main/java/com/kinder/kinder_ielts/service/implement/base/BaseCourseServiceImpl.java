package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.CourseStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.repository.CourseRepository;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Implementation of {@link BaseCourseService}.
 * Provides CRUD operations for {@link Course} entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BaseCourseServiceImpl extends BaseEntityServiceImpl<Course, String> implements BaseCourseService {
    private final BaseAccountService baseAccountService;
    private final CourseRepository courseRepository;

    @Override
    protected CourseRepository getRepository() {
        return courseRepository;
    }

    @Override
    protected String getEntityName() {
        return "Course";
    }

    @Override
    protected String getEntityId(Course entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(Course entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.setStatus(CourseStatus.INACTIVE);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<Course> entity, Account modifier, ZonedDateTime currentTime) {
        for (Course course : entity) {
            course.setIsDeleted(IsDelete.DELETED);
            course.setStatus(CourseStatus.INACTIVE);
            course.updateAudit(modifier, currentTime);
        }
    }
}