package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.id.CourseStudentId;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.CourseStudentRepository;
import com.kinder.kinder_ielts.response_message.CourseStudentMessage;
import com.kinder.kinder_ielts.service.base.BaseCourseStudentService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

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
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<CourseStudent> entity, Account modifier, ZonedDateTime currentTime) {
        for (CourseStudent courseStudent : entity) {
            courseStudent.setIsDeleted(IsDelete.DELETED);
            courseStudent.updateAudit(modifier, currentTime);
        }
    }

    @Override
    public void create(Student student, Course course, Account creator, ZonedDateTime currentTime) {
        create(new CourseStudent(course, student, creator, currentTime), CourseStudentMessage.CREATE_FAILED);
    }

    @Override
    public List<CourseStudent> getByStudentIds(List<String> studentIds) {
        return courseStudentRepository.findByStudentIdInAndIsDeleted(studentIds, IsDelete.NOT_DELETED);
    }
}
