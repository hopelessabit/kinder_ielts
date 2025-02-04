package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.id.CourseTutorId;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import com.kinder.kinder_ielts.repository.CourseTutorRepository;
import com.kinder.kinder_ielts.service.base.BaseCourseTutorService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
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
    protected void markAsDeleted(CourseTutor entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<CourseTutor> entity, Account modifier, ZonedDateTime currentTime) {
        for (CourseTutor courseTutor : entity) {
            courseTutor.setIsDeleted(IsDelete.DELETED);
            courseTutor.updateAudit(modifier, currentTime);
        }
    }

    @Override
    public List<CourseTutor> getByCourseId(String courseId, IsDelete isDelete) {
        return courseTutorRepository.findById_CourseIdAndIsDeleted(courseId, isDelete);
    }
}
