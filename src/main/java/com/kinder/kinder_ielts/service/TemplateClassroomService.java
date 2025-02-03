package com.kinder.kinder_ielts.service;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.template.classroom.CreateTemplateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.template.classroom.UpdateTemplateClassroomRequest;
import com.kinder.kinder_ielts.dto.response.template.classroom.TemplateClassroomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TemplateClassroomService {
    TemplateClassroomResponse create(String courseId, CreateTemplateClassroomRequest request, String failMessage);
    TemplateClassroomResponse updateStatus(String templateClassroomId, IsDelete isDelete, String failMessage);
    Page<TemplateClassroomResponse> getByCourseId(String courseId, Pageable pageable);
    TemplateClassroomResponse get(String templateClassroomId);
    void delete(String templateClassroomId);

    TemplateClassroomResponse updateInfo(String templateClassroomId, UpdateTemplateClassroomRequest request, String infoUpdateFailed);
}
