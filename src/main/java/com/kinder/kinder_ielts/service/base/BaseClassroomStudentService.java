package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;

import java.util.List;

public interface BaseClassroomStudentService extends BaseEntityService<ClassroomStudent, ClassStudentId> {
    void updateStudent(String classroomId, String studentId, boolean isAdd);

    List<ClassroomStudent> getClassRoomStudentsByStudyMaterialId(String studyMaterialId, IsDelete isDelete, String notFound);
}
