package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;

public interface BaseClassroomStudentService extends BaseEntityService<ClassroomStudent, ClassStudentId> {
    void updateStudent(String classroomId, String studentId, boolean isAdd);
}
