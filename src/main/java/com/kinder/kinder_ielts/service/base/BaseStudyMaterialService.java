package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.StudyMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing {@link StudyMaterial} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseStudyMaterialService extends BaseEntityService<StudyMaterial, String> {
    Page<StudyMaterial> getByStudyScheduleId(String studyScheduleId, Pageable pageable, IsDelete isDelete);
}