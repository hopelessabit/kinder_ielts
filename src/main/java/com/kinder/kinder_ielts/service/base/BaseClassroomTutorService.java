package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.ClassroomTutorId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;

import java.util.List;

public interface BaseClassroomTutorService extends BaseEntityService<ClassroomTutor, ClassroomTutorId> {
    List<ClassroomTutor> getByClassroomId(String id, IsDelete isDelete);
}
