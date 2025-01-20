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

    @Override
    public TemplateClassroomLinkResponse create(String templateStudyScheduleId, CreateTemplateClassroomLink request, String failMessage) {
        log.info("[CREATE CLASSROOM LINK] Initiating creation for study schedule ID: {}", templateStudyScheduleId);

        TemplateStudySchedule templateStudySchedule = fetchTemplateStudySchedule(templateStudyScheduleId, failMessage);

        TemplateClassroomLink templateClassroomLink = ModelMapper.map(request);
        templateClassroomLink.setTemplateStudySchedule(templateStudySchedule);

        log.debug("[CREATE CLASSROOM LINK] Persisting classroom link");
        TemplateClassroomLinkResponse response = TemplateClassroomLinkResponse.detailWithStudySchedule(
                templateClassroomLinkService.create(templateClassroomLink, failMessage));

        log.info("[CREATE CLASSROOM LINK] Successfully created classroom link for study schedule ID: {}", templateStudyScheduleId);
        return response;
    }

    @Override
    public TemplateClassroomLinkResponse get(String templateClassroomLinkId, String failMessage) {
        log.info("[GET CLASSROOM LINK] Fetching classroom link with ID: {}", templateClassroomLinkId);

        TemplateClassroomLinkResponse response = TemplateClassroomLinkResponse.infoWithStudySchedule(
                templateClassroomLinkService.get(templateClassroomLinkId, IsDelete.NOT_DELETED, failMessage));

        log.info("[GET CLASSROOM LINK] Successfully fetched classroom link with ID: {}", templateClassroomLinkId);
        return response;
    }

    @Override
    public List<TemplateClassroomLinkResponse> getByTemplateStudyScheduleId(String templateStudyScheduleId, String failMessage) {
        log.info("[GET CLASSROOM LINKS BY STUDY SCHEDULE] Fetching links for study schedule ID: {}", templateStudyScheduleId);

        TemplateStudySchedule templateStudySchedule = fetchTemplateStudySchedule(templateStudyScheduleId, failMessage);
        List<TemplateClassroomLink> links = templateStudySchedule.getClassroomLinks();

        log.debug("[GET CLASSROOM LINKS BY STUDY SCHEDULE] Found {} links, filtering non-deleted ones", links != null ? links.size() : 0);

        List<TemplateClassroomLinkResponse> response = links == null ?
                List.of() :
                links.stream()
                        .filter(link -> IsDelete.NOT_DELETED.equals(link.getIsDeleted()))
                        .map(TemplateClassroomLinkResponse::info)
                        .toList();

        log.info("[GET CLASSROOM LINKS BY STUDY SCHEDULE] Returning {} active links", response.size());
        return response;
    }

    @Override
    public void delete(String templateClassroomLinkId, String failMessage) {
        log.info("[DELETE CLASSROOM LINK] Deleting classroom link with ID: {}", templateClassroomLinkId);

        templateClassroomLinkService.delete(templateClassroomLinkId, failMessage);

        log.info("[DELETE CLASSROOM LINK] Successfully deleted classroom link with ID: {}", templateClassroomLinkId);
    }

    @Override
    public TemplateClassroomLinkResponse updateInfo(String templateClassroomLinkId, UpdateTemplateClassroomLinkRequest request, String failMessage) {
        log.info("[UPDATE CLASSROOM LINK INFO] Updating info for classroom link ID: {}", templateClassroomLinkId);

        TemplateClassroomLink templateClassroomLink = fetchTemplateClassroomLink(templateClassroomLinkId, failMessage);

        log.debug("[UPDATE CLASSROOM LINK INFO] Comparing and updating fields");
        templateClassroomLink.setLink(CompareUtil.compare(request.getLink(), templateClassroomLink.getLink()));
        templateClassroomLink.setDescription(CompareUtil.compare(request.getDescription(), templateClassroomLink.getDescription()));

        updateAuditInfo(templateClassroomLink);

        log.debug("[UPDATE CLASSROOM LINK INFO] Persisting updated classroom link");
        TemplateClassroomLinkResponse response = TemplateClassroomLinkResponse.detailWithStudySchedule(
                templateClassroomLinkService.update(templateClassroomLink, failMessage));

        log.info("[UPDATE CLASSROOM LINK INFO] Successfully updated classroom link ID: {}", templateClassroomLinkId);
        return response;
    }

    @Override
    public TemplateClassroomLinkResponse updateStatus(String templateClassroomLinkId, ClassroomLinkStatus status, String failMessage) {
        log.info("[UPDATE CLASSROOM LINK STATUS] Updating status for classroom link ID: {}", templateClassroomLinkId);

        TemplateClassroomLink templateClassroomLink = fetchTemplateClassroomLink(templateClassroomLinkId, failMessage);

        log.debug("[UPDATE CLASSROOM LINK STATUS] Changing status to: {}", status);
        templateClassroomLink.setStatus(status);

        updateAuditInfo(templateClassroomLink);

        log.debug("[UPDATE CLASSROOM LINK STATUS] Persisting updated classroom link");
        TemplateClassroomLinkResponse response = TemplateClassroomLinkResponse.detailWithStudySchedule(
                templateClassroomLinkService.update(templateClassroomLink, failMessage));

        log.info("[UPDATE CLASSROOM LINK STATUS] Successfully updated status for classroom link ID: {}", templateClassroomLinkId);
        return response;
    }

    /**
     * Fetches a TemplateStudySchedule by ID and ensures it is not deleted.
     */
    private TemplateStudySchedule fetchTemplateStudySchedule(String id, String failMessage) {
        log.debug("[FETCH STUDY SCHEDULE] Fetching study schedule with ID: {}", id);
        return templateStudyScheduleService.get(id, IsDelete.NOT_DELETED, failMessage);
    }

    /**
     * Fetches a TemplateClassroomLink by ID and ensures it is not deleted.
     */
    private TemplateClassroomLink fetchTemplateClassroomLink(String id, String failMessage) {
        log.debug("[FETCH CLASSROOM LINK] Fetching classroom link with ID: {}", id);
        return templateClassroomLinkService.get(id, IsDelete.NOT_DELETED, failMessage);
    }

    /**
     * Updates audit fields (modifiedBy and modifiedTime) for an entity.
     */
    private void updateAuditInfo(TemplateClassroomLink templateClassroomLink) {
        templateClassroomLink.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateClassroomLink.setModifyTime(ZonedDateTime.now());
        log.debug("[UPDATE AUDIT INFO] Updated audit info for classroom link ID: {}", templateClassroomLink.getId());
    }
}

