package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.ClassroomStudentRepository;
import com.kinder.kinder_ielts.service.base.BaseClassroomStudentService;
import com.kinder.kinder_ielts.service.base.BaseEntityService;

import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseClassroomStudentServiceImpl extends BaseEntityServiceImpl<ClassroomStudent, ClassStudentId> implements BaseClassroomStudentService {
    private final ClassroomStudentRepository classroomStudentRepository;
    @Override
    protected BaseEntityRepository<ClassroomStudent, ClassStudentId> getRepository() {
        return classroomStudentRepository;
    }

    @Override
    protected String getEntityName() {
        return "[Classroom Student]";
    }

    @Override
    protected ClassStudentId getEntityId(ClassroomStudent entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(ClassroomStudent entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<ClassroomStudent> entity, Account modifier, ZonedDateTime currentTime) {
        for (ClassroomStudent classroomStudent : entity) {
            classroomStudent.setIsDeleted(IsDelete.DELETED);
            classroomStudent.updateAudit(modifier, currentTime);
        }
    }

    @Override
    public void updateStudent(String classroomId, String studentId, boolean isAdd) {

    }

    @Override
    public List<ClassroomStudent> getClassRoomStudentsByStudyMaterialId(String studyMaterialId, IsDelete isDelete, String notFound) {
        return classroomStudentRepository.findByStudyMaterialId(studyMaterialId, isDelete);
    }

    @Override
    public List<ClassroomStudent> getClassRoomStudentsByHomeworkId(String homeworkId, IsDelete isDelete, String notFound) {
        return classroomStudentRepository.findByHomeworkId(homeworkId, isDelete);
    }
}
