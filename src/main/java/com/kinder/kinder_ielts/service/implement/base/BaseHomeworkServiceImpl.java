package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Homework;
import com.kinder.kinder_ielts.repository.HomeworkRepository;
import com.kinder.kinder_ielts.service.base.BaseHomeworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link BaseHomeworkService}.
 * Provides CRUD operations for {@link Homework} entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BaseHomeworkServiceImpl extends BaseEntityServiceImpl<Homework, String> implements BaseHomeworkService {

    private final HomeworkRepository homeworkRepository;

    @Override
    protected HomeworkRepository getRepository() {
        return homeworkRepository;
    }

    @Override
    protected String getEntityName() {
        return "Homework";
    }

    @Override
    protected String getEntityId(Homework entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(Homework entity) {
        entity.setIsDeleted(IsDelete.DELETED);
    }
}