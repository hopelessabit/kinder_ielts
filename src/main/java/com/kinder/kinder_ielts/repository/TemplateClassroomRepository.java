package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateClassroomRepository extends BaseEntityRepository<TemplateClassroom, String> {
    Page<TemplateClassroom> findByCourse_Id(String id, Pageable pageable);
}
