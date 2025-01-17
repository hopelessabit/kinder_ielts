package com.kinder.kinder_ielts.service.implement.base.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.TemplateClassroomRepository;
import com.kinder.kinder_ielts.service.base.BaseTemplateClassroomService;
import com.kinder.kinder_ielts.service.implement.base.BaseEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseTemplateClassroomServiceImpl extends BaseEntityServiceImpl<TemplateClassroom, String> implements BaseTemplateClassroomService {
    private final TemplateClassroomRepository repository;

    @Override
    protected BaseEntityRepository<TemplateClassroom, String> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Template Classroom";
    }

    @Override
    protected String getEntityId(TemplateClassroom entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(TemplateClassroom entity) {
        entity.setIsDeleted(IsDelete.DELETED);
    }
}
