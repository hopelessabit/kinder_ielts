package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.ClassroomStudentRepository;
import com.kinder.kinder_ielts.response_message.ClassroomStudentMessage;
import com.kinder.kinder_ielts.service.base.BaseClassroomStudentService;

import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    @Override
    public List<ClassroomStudent> findByClassroomId(String classId, IsDelete isDelete) {
        return classroomStudentRepository.findById_ClassIdAndIsDeleted(classId, isDelete);
    }

    @Override
    public void create(Student student, Classroom classroom, Account creator, ZonedDateTime currentTime) {
        create(new ClassroomStudent(classroom, student, creator, currentTime), ClassroomStudentMessage.CREATE_FAILED);
    }

    @Override
    public List<ClassroomStudent> create(List<Student> students, Classroom classroom, Account creator, ZonedDateTime currentTime, String failMessage) {
        List<ClassroomStudent> classroomStudents = new ArrayList<>();

        for (Student student : students) {
            classroomStudents.add(new ClassroomStudent(classroom, student, creator, currentTime));
        }

        return create(classroomStudents, failMessage);
    }

    @Override
    public List<ClassroomStudent> getByStudentIds(List<String> studentIds) {
        return classroomStudentRepository.findById_StudentIdInAndIsDeleted(studentIds, IsDelete.NOT_DELETED);
    }

    @Override
    public List<ClassroomStudent> getByStudentIdsAndClassIds(List<String> studentIds, List<String> classIds) {
        return classroomStudentRepository.findById_StudentIdInAndId_ClassIdInAndIsDeleted(studentIds, classIds, IsDelete.NOT_DELETED);
    }

    @Override
    public List<ClassroomStudent> getByStudentIdsAndCourseIds(List<String> studentIds, List<String> courseIds) {
        return classroomStudentRepository.findDistinctById_StudentIdInAndClassroom_Course_IdInAndIsDeleted(studentIds, courseIds, IsDelete.NOT_DELETED);
    }

    @Override
    public List<ClassroomStudent> getByStudentIdsAndCourseId(List<String> studentIds, String courseId) {
        return classroomStudentRepository.findDistinctById_StudentIdInAndClassroom_CourseIdAndIsDeleted(studentIds, courseId, IsDelete.NOT_DELETED);
    }
}
