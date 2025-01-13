package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.study_schedule.CreateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.CreateTemplateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.dto.response.template.study_schedule.TemplateStudyScheduleResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.repository.TemplateStudyScheduleRepository;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateStudyScheduleServiceImpl {
    private final BaseTemplateStudyScheduleService baseTemplateStudyScheduleService;
    private final BaseCourseService baseCourseService;

    public TemplateStudyScheduleResponse createTemplateStudySchedule(String courseId, CreateTemplateStudyScheduleRequest request, String failMessage) {
        TemplateStudySchedule studySchedule = ModelMapper.map(request);

        String createBy = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for creator ID: {}", createBy);
        Account creator = SecurityContextHolderUtil.getAccount();
        studySchedule.setCreateBy(creator);

        Course course = baseCourseService.get(courseId, IsDelete.NOT_DELETED, failMessage);

        return TemplateStudyScheduleResponse.detailWithDetail(baseTemplateStudyScheduleService.create(studySchedule, failMessage));
    }
}
