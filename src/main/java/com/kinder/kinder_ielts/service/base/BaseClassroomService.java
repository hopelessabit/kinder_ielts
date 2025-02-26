package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Classroom;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Service interface for managing {@link Classroom} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseClassroomService extends BaseEntityService<Classroom, String> {
    Page<Classroom> findAll(Specification<Classroom> classroomSpecification, Pageable unsortedPageable);

    Page<Classroom> findAll(Specification<Classroom> classroomSpecification);

    Classroom getByStudyMaterialId(String studyMaterialId, Boolean require);

    Classroom getByStudyMaterialId(String studyMaterialId);

    Classroom getByIdWithStudentId(String classroomId, String studentId, String failMessage);

    List<Classroom> getByCourseId(String id, IsDelete isDelete, String s);

    List<Classroom> getByStudentIds(List<String> studentIds);
}