package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.template.homework.CreateTemplateHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.template.homework.UpdateTemplateHomeworkRequest;
import com.kinder.kinder_ielts.dto.response.template.homework.TemplateHomeworkResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateHomework;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseTemplateHomeworkService;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.service.template.TemplateHomeworkService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateHomeworkServiceImpl implements TemplateHomeworkService {
    private final BaseTemplateHomeworkService baseTemplateHomeworkService;
    private final BaseTemplateStudyScheduleService baseTemplateStudyScheduleService;

    public List<TemplateHomeworkResponse> getByTemplateStudyScheduleId (String templateStudyScheduleId, String failMessage) {
        TemplateStudySchedule templateStudySchedule = baseTemplateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);
        List<TemplateHomework> templateHomeworks = templateStudySchedule.getHomework();

        if (templateHomeworks == null || templateHomeworks.isEmpty()) {
            return new ArrayList<>();
        }

        return templateHomeworks.stream().filter(a -> a.getIsDeleted().equals(IsDelete.NOT_DELETED)).map(TemplateHomeworkResponse::info).toList();
    }

    public TemplateHomeworkResponse get(String templateHomeworkId, String failMessage) {
        return TemplateHomeworkResponse.infoWithDetails(baseTemplateHomeworkService.get(templateHomeworkId, IsDelete.NOT_DELETED, failMessage));
    }

    public TemplateHomeworkResponse create(String templateStudyScheduleId, CreateTemplateHomeworkRequest request, String failMessage){
        TemplateStudySchedule templateStudySchedule = baseTemplateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);
        TemplateHomework templateHomework = ModelMapper.map(request);

        templateHomework.setTemplateStudySchedule(templateStudySchedule);
        return TemplateHomeworkResponse.infoWithDetails(baseTemplateHomeworkService.create(templateHomework, failMessage));
    }

    public TemplateHomeworkResponse update(String templateHomeworkId, UpdateTemplateHomeworkRequest request, String failMessage){
        TemplateHomework templateHomework = baseTemplateHomeworkService.get(templateHomeworkId, IsDelete.NOT_DELETED, failMessage);

        templateHomework.setTitle(CompareUtil.compare(request.getTitle(), templateHomework.getTitle()));
        templateHomework.setDescription(CompareUtil.compare(request.getDescription(), templateHomework.getDescription()));
        templateHomework.setLink(CompareUtil.compare(request.getLink(), templateHomework.getLink()));
        templateHomework.setDueDate(CompareUtil.compare(request.getDueDate(), templateHomework.getDueDate()));
        templateHomework.setStartDate(CompareUtil.compare(request.getStartDate(), templateHomework.getStartDate()));
        templateHomework.setStatus(CompareUtil.compare(request.getStatus(), templateHomework.getStatus()));

        templateHomework.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateHomework.setModifyTime(ZonedDateTime.now());

        return TemplateHomeworkResponse.infoWithDetails(baseTemplateHomeworkService.update(templateHomework, failMessage));
    }

    public void delete(String templateHomeworkId, String failMessage){
        baseTemplateHomeworkService.delete(templateHomeworkId, failMessage);
    }
}
