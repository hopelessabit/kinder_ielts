package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.CourseStudentId;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.CourseStudentRepository;
import com.kinder.kinder_ielts.service.base.BaseCourseStudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseCourseStudentServiceImpl extends BaseEntityServiceImpl<CourseStudent, CourseStudentId> implements BaseCourseStudentService {
    private final CourseStudentRepository courseStudentRepository;
    @Override
    protected BaseEntityRepository<CourseStudent, CourseStudentId> getRepository() {
        return courseStudentRepository;
    }

    @Override
    protected String getEntityName() {
        return "Course Student";
    }

    @Override
    protected CourseStudentId getEntityId(CourseStudent entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(CourseStudent entity) {
        entity.setIsDeleted(IsDelete.DELETED);
    }
}
