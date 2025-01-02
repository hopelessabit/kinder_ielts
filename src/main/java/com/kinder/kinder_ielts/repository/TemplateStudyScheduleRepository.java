package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateStudyScheduleRepository extends BaseEntityRepository<TemplateStudySchedule, String> {
}
