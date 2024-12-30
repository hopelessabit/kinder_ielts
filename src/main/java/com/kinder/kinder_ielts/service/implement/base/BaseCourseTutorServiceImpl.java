package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.CourseTutorId;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import com.kinder.kinder_ielts.repository.CourseTutorRepository;
import com.kinder.kinder_ielts.service.base.BaseCourseTutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseCourseTutorServiceImpl extends BaseEntityServiceImpl<CourseTutor, CourseTutorId> implements BaseCourseTutorService{

    private final CourseTutorRepository courseTutorRepository;

    @Override
    protected CourseTutorRepository getRepository() {
        return courseTutorRepository;
    }

    @Override
    protected String getEntityName() {
        return "CourseTutor";
    }

    @Override
    protected CourseTutorId getEntityId(CourseTutor entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(CourseTutor account) {
        account.setIsDeleted(IsDelete.DELETED);
    }

    @Override
    public List<CourseTutor> getByCourseId(String courseId, IsDelete isDelete) {
        return courseTutorRepository.findById_CourseIdAndIsDeleted(courseId, isDelete);
    }
}
