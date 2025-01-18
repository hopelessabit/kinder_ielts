package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.WarmUpTestStatus;
import com.kinder.kinder_ielts.dto.request.template.warmup_test.CreateTemplateWarmupTestRequest;
import com.kinder.kinder_ielts.dto.request.template.warmup_test.UpdateTemplateWarmupTestRequest;
import com.kinder.kinder_ielts.dto.response.template.warm_up_test.TemplateWarmUpTestResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.entity.course_template.TemplateWarmUpTest;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.service.base.BaseTemplateWarmUpTestService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateWarmupTestServiceImpl {
    private BaseTemplateWarmUpTestService baseTemplateWarmUpTestService;
    private BaseTemplateStudyScheduleService baseTemplateStudyScheduleService;

    public TemplateWarmUpTestResponse create (String templateStudyScheduleId, CreateTemplateWarmupTestRequest request, String failMessage) {
        TemplateStudySchedule templateStudySchedule = baseTemplateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);
        TemplateWarmUpTest templateWarmUpTest = ModelMapper.map(request);
        templateWarmUpTest.setTemplateStudySchedule(templateStudySchedule);

        return TemplateWarmUpTestResponse.detailWithStudySchedule(baseTemplateWarmUpTestService.create(templateWarmUpTest, failMessage));
    }

    public TemplateWarmUpTestResponse get(String templateWarmUpTestId, String failMessage) {
        return TemplateWarmUpTestResponse.infoWithStudySchedule(baseTemplateWarmUpTestService.get(templateWarmUpTestId, IsDelete.NOT_DELETED, failMessage));
    }

    public List<TemplateWarmUpTestResponse> getByTemplateStudyScheduleId(String templateStudyScheduleId, String failMessage) {
        TemplateStudySchedule templateStudySchedule = baseTemplateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);
        List<TemplateWarmUpTest> templateWarmUpTests = templateStudySchedule.getWarmUpTests();

        if (templateWarmUpTests == null || templateWarmUpTests.isEmpty()) {
            return List.of();
        }

        return templateWarmUpTests.stream().filter(a -> a.getIsDeleted().equals(IsDelete.NOT_DELETED)).map(TemplateWarmUpTestResponse::info).toList();
    }

    public TemplateWarmUpTestResponse updateInfo(String templateWarmupTestId, UpdateTemplateWarmupTestRequest request, String failMessage) {
        TemplateWarmUpTest templateWarmUpTest = baseTemplateWarmUpTestService.get(templateWarmupTestId, IsDelete.NOT_DELETED, failMessage);

        templateWarmUpTest.setTitle(CompareUtil.compare(request.getTitle(), templateWarmUpTest.getTitle()));
        templateWarmUpTest.setDescription(CompareUtil.compare(request.getDescription(), templateWarmUpTest.getDescription()));
        templateWarmUpTest.setLink(CompareUtil.compare(request.getLink(), templateWarmUpTest.getLink()));

        templateWarmUpTest.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateWarmUpTest.setModifyTime(ZonedDateTime.now());

        return TemplateWarmUpTestResponse.detailWithStudySchedule(baseTemplateWarmUpTestService.update(templateWarmUpTest, failMessage));
    }

    public TemplateWarmUpTestResponse updateStatus(String templateWarmupTestId, WarmUpTestStatus warmUpTestStatus, String failMessage) {
        TemplateWarmUpTest templateWarmUpTest = baseTemplateWarmUpTestService.get(templateWarmupTestId, IsDelete.NOT_DELETED, failMessage);
        templateWarmUpTest.setStatus(warmUpTestStatus);

        templateWarmUpTest.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateWarmUpTest.setModifyTime(ZonedDateTime.now());

        return TemplateWarmUpTestResponse.detailWithStudySchedule(baseTemplateWarmUpTestService.update(templateWarmUpTest, failMessage));
    }

    public void delete(String templateWarmupTestId, String failMessage) {
        baseTemplateWarmUpTestService.delete(templateWarmupTestId, failMessage);
    }
}
