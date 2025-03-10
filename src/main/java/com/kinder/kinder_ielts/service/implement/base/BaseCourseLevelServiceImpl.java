package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.CourseLevel;
import com.kinder.kinder_ielts.repository.CourseLevelRepository;
import com.kinder.kinder_ielts.service.base.BaseCourseLevelService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Implementation of {@link BaseCourseLevelService}.
 * Provides CRUD operations for {@link CourseLevel} entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BaseCourseLevelServiceImpl extends BaseEntityServiceImpl<CourseLevel, String> implements BaseCourseLevelService {

    private final CourseLevelRepository courseLevelRepository;

    @Override
    protected CourseLevelRepository getRepository() {
        return courseLevelRepository;
    }

    @Override
    protected String getEntityName() {
        return "[Course Level]";
    }

    @Override
    protected String getEntityId(CourseLevel entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(CourseLevel entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<CourseLevel> entity, Account modifier, ZonedDateTime currentTime) {
        for (CourseLevel courseLevel : entity) {
            courseLevel.setIsDeleted(IsDelete.DELETED);
            courseLevel.updateAudit(modifier, currentTime);
        }
    }
}
