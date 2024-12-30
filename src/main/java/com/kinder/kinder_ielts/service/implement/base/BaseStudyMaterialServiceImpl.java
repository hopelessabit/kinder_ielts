package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.StudyMaterial;
import com.kinder.kinder_ielts.repository.StudyMaterialRepository;
import com.kinder.kinder_ielts.service.base.BaseStudyMaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link BaseStudyMaterialService}.
 * Provides CRUD operations for {@link StudyMaterial} entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BaseStudyMaterialServiceImpl extends BaseEntityServiceImpl<StudyMaterial, String> implements BaseStudyMaterialService {

    private final StudyMaterialRepository studyMaterialRepository;

    @Override
    protected StudyMaterialRepository getRepository() {
        return studyMaterialRepository;
    }

    @Override
    protected String getEntityName() {
        return "StudyMaterial";
    }

    @Override
    protected String getEntityId(StudyMaterial entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(StudyMaterial entity) {
        entity.setIsDeleted(IsDelete.DELETED);
    }
}