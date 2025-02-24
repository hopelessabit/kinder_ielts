package com.kinder.kinder_ielts.service.template;

import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.dto.request.template.classroom_link.CreateTemplateClassroomLink;
import com.kinder.kinder_ielts.dto.request.template.classroom_link.UpdateTemplateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.response.template.classroom_link.TemplateClassroomLinkResponse;

import java.util.List;

public interface TemplateClassroomLinkService {
    TemplateClassroomLinkResponse get(String templateClassroomLinkId, String failMessage);
    TemplateClassroomLinkResponse create(String templateStudyScheduleId, CreateTemplateClassroomLink request, String failMessage);
    List<TemplateClassroomLinkResponse> getByTemplateStudyScheduleId(String templateStudyScheduleId, String failMessage);
    void delete(String templateClassroomLinkId, String failMessage);
    TemplateClassroomLinkResponse updateInfo(String templateClassroomLinkId, UpdateTemplateClassroomLinkRequest request, String failMessage);
    TemplateClassroomLinkResponse updateStatus(String templateClassroomLinkId, ViewStatus status, String failMessage);
}
