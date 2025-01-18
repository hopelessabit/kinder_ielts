package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.ClassroomLinkStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.template.classroom_link.CreateTemplateClassroomLink;
import com.kinder.kinder_ielts.dto.request.template.classroom_link.UpdateTemplateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.response.template.classroom_link.TemplateClassroomLinkResponse;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroomLink;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseTemplateClassroomLinkService;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.service.template.TemplateClassroomLinkService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateClassroomLinkServiceImpl implements TemplateClassroomLinkService {
    private final BaseTemplateClassroomLinkService templateClassroomLinkService;
    private final BaseTemplateStudyScheduleService templateStudyScheduleService;

    public TemplateClassroomLinkResponse create(String templateStudyScheduleId, CreateTemplateClassroomLink request, String failMessage) {
        TemplateStudySchedule templateStudySchedule = templateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);
        TemplateClassroomLink templateClassroomLink = ModelMapper.map(request);
        templateClassroomLink.setTemplateStudySchedule(templateStudySchedule);

        return TemplateClassroomLinkResponse.detailWithStudySchedule(templateClassroomLinkService.create(templateClassroomLink, failMessage));
    }

    public TemplateClassroomLinkResponse get(String templateClassroomLinkId, String failMessage) {
        return TemplateClassroomLinkResponse.infoWithStudySchedule(templateClassroomLinkService.get(templateClassroomLinkId, IsDelete.NOT_DELETED, failMessage));
    }

    public List<TemplateClassroomLinkResponse> getByTemplateStudyScheduleId(String templateStudyScheduleId, String failMessage) {
        TemplateStudySchedule templateStudySchedule = templateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);
        List<TemplateClassroomLink> templateClassroomLinks = templateStudySchedule.getClassroomLinks();

        if (templateClassroomLinks == null || templateClassroomLinks.isEmpty()) {
            return List.of();
        }

        return templateClassroomLinks.stream().filter(a -> a.getIsDeleted().equals(IsDelete.NOT_DELETED)).map(TemplateClassroomLinkResponse::info).toList();
    }

    public void delete(String templateClassroomLinkId, String failMessage) {
        templateClassroomLinkService.delete(templateClassroomLinkId, failMessage);
    }

    public TemplateClassroomLinkResponse updateInfo(String templateClassroomLinkId, UpdateTemplateClassroomLinkRequest request, String failMessage) {
        TemplateClassroomLink templateClassroomLink = templateClassroomLinkService.get(templateClassroomLinkId, IsDelete.NOT_DELETED, failMessage);

        templateClassroomLink.setLink(CompareUtil.compare(request.getLink(), templateClassroomLink.getLink()));
        templateClassroomLink.setDescription(CompareUtil.compare(request.getDescription(), templateClassroomLink.getDescription()));
        templateClassroomLink.setLink(CompareUtil.compare(request.getLink(), templateClassroomLink.getLink()));

        templateClassroomLink.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateClassroomLink.setModifyTime(ZonedDateTime.now());
        return TemplateClassroomLinkResponse.detailWithStudySchedule(templateClassroomLinkService.update(templateClassroomLink, failMessage));
    }

    public TemplateClassroomLinkResponse updateStatus(String templateClassroomLinkId, ClassroomLinkStatus status, String failMessage) {
        TemplateClassroomLink templateClassroomLink = templateClassroomLinkService.get(templateClassroomLinkId, IsDelete.NOT_DELETED, failMessage);
        templateClassroomLink.setStatus(status);

        templateClassroomLink.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateClassroomLink.setModifyTime(ZonedDateTime.now());

        return TemplateClassroomLinkResponse.detailWithStudySchedule(templateClassroomLinkService.update(templateClassroomLink, failMessage));
    }
}
