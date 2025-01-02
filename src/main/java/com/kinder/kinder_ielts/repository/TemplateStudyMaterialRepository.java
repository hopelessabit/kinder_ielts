package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.course_template.TemplateStudyMaterial;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateStudyMaterialRepository extends BaseEntityRepository<TemplateStudyMaterial, String> {
}
