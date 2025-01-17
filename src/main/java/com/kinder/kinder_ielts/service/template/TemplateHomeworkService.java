package com.kinder.kinder_ielts.service.template;

import com.kinder.kinder_ielts.dto.request.template.homework.CreateTemplateHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.template.homework.UpdateTemplateHomeworkRequest;
import com.kinder.kinder_ielts.dto.response.template.homework.TemplateHomeworkResponse;

import java.util.List;

public interface TemplateHomeworkService {
    List<TemplateHomeworkResponse> getByTemplateStudyScheduleId (String templateStudyScheduleId, String failMessage);
    TemplateHomeworkResponse get(String templateHomeworkId, String failMessage);
    TemplateHomeworkResponse create(String templateStudyScheduleId, CreateTemplateHomeworkRequest request, String failMessage);
    TemplateHomeworkResponse update(String templateHomeworkId, UpdateTemplateHomeworkRequest request, String failMessage);
    void delete(String templateHomeworkId, String failMessage);
}
