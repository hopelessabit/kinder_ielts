package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.id.StudentHomeworkId;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseStudentHomeworkService extends BaseEntityService<StudentHomework, StudentHomeworkId> {
    List<StudentHomework> getByHomeworkId(String homeworkId, IsDelete isDelete, String failMessage);

    Page<StudentHomework> getByHomeworkId(String homeworkId, Pageable pageable, IsDelete isDelete, String failMessage);

    List<StudentHomework> getByHomeworkId(String id, String failMessage);
}
