package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Service interface for managing {@link Classroom} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseClassroomService extends BaseEntityService<Classroom, String> {
    Page<Classroom> findAll(Specification<Classroom> classroomSpecification, Pageable unsortedPageable);

    Page<Classroom> findAll(Specification<Classroom> classroomSpecification);

    Classroom getByStudyMaterialId(String studyMaterialId, Boolean require);

    Classroom getByStudyMaterialId(String studyMaterialId);
}