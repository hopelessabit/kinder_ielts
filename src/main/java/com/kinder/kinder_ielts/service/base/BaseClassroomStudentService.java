package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;

import java.time.ZonedDateTime;
import java.util.List;

public interface BaseClassroomStudentService extends BaseEntityService<ClassroomStudent, ClassStudentId> {
    void updateStudent(String classroomId, String studentId, boolean isAdd);

    List<ClassroomStudent> getClassRoomStudentsByStudyMaterialId(String studyMaterialId, IsDelete isDelete, String notFound);
    List<ClassroomStudent> getClassRoomStudentsByHomeworkId(String homeworkId, IsDelete isDelete, String notFound);

    List<ClassroomStudent> findByClassroomId(String classId, IsDelete isDelete);

    void create(Student student, Classroom classroom, Account creator, ZonedDateTime currentTime);

    List<ClassroomStudent> create(List<Student> students, Classroom classroom, Account creator, ZonedDateTime currentTime, String failMessage);

    List<ClassroomStudent> getByStudentIds(List<String> studentIds);

    List<ClassroomStudent> getByStudentIdsAndClassIds(List<String> studentIds, List<String> classIds);

    List<ClassroomStudent> getByStudentIdsAndCourseIds(List<String> studentIds, List<String> courseIds);
}
