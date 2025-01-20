package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.template.classroom.CreateTemplateClassroomRequest;
import com.kinder.kinder_ielts.dto.response.template.classroom.TemplateClassroomResponse;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import com.kinder.kinder_ielts.repository.TemplateClassroomRepository;
import com.kinder.kinder_ielts.response_message.TemplateClassroomMessage;
import com.kinder.kinder_ielts.service.TemplateClassroomService;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.base.BaseTemplateClassroomService;
import com.kinder.kinder_ielts.util.IdUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateClassroomServiceImpl implements TemplateClassroomService {
    private final BaseTemplateClassroomService baseTemplateClassroomService;
    private final BaseCourseService baseCourseService;
    private final TemplateClassroomRepository templateClassroomRepository;

    @Override
    public TemplateClassroomResponse create(String courseId, CreateTemplateClassroomRequest request, String failMessage) {
        log.info("[CREATE TEMPLATE CLASSROOM] Initiating creation for course ID: {}", courseId);

        validateCreateRequest(request);

        TemplateClassroom templateClassroom = initializeTemplateClassroom(request);

        log.debug("[CREATE TEMPLATE CLASSROOM] Fetching course details for ID: {}", courseId);
        Course course = baseCourseService.get(courseId, IsDelete.NOT_DELETED, failMessage);
        templateClassroom.setCourse(course);

        log.debug("[CREATE TEMPLATE CLASSROOM] Saving template classroom entity");
        TemplateClassroomResponse response = TemplateClassroomResponse.info(baseTemplateClassroomService.create(templateClassroom, failMessage));
        log.info("[CREATE TEMPLATE CLASSROOM] Successfully created template classroom for course ID: {}", courseId);

        return response;
    }

    @Override
    public TemplateClassroomResponse updateStatus(String templateClassroomId, IsDelete isDelete, String failMessage) {
        log.info("[UPDATE TEMPLATE CLASSROOM STATUS] Updating status for template classroom ID: {}", templateClassroomId);

        TemplateClassroom templateClassroom = fetchTemplateClassroom(templateClassroomId, failMessage);

        log.debug("[UPDATE TEMPLATE CLASSROOM STATUS] Changing status to: {}", isDelete);
        templateClassroom.setIsDeleted(isDelete);
        updateAuditInfo(templateClassroom);

        log.debug("[UPDATE TEMPLATE CLASSROOM STATUS] Persisting updated template classroom");
        TemplateClassroomResponse response = TemplateClassroomResponse.info(baseTemplateClassroomService.update(templateClassroom, failMessage));
        log.info("[UPDATE TEMPLATE CLASSROOM STATUS] Successfully updated status for template classroom ID: {}", templateClassroomId);

        return response;
    }

    @Override
    public Page<TemplateClassroomResponse> getByCourseId(String courseId, Pageable pageable) {
        log.info("[GET TEMPLATE CLASSROOMS BY COURSE] Fetching template classrooms for course ID: {}", courseId);

        log.debug("[GET TEMPLATE CLASSROOMS BY COURSE] Querying repository with course ID: {}", courseId);
        Page<TemplateClassroom> templateClassrooms = templateClassroomRepository.findByCourse_Id(courseId, pageable);

        log.debug("[GET TEMPLATE CLASSROOMS BY COURSE] Mapping entities to response objects");
        Page<TemplateClassroomResponse> response = templateClassrooms.map(TemplateClassroomResponse::info);
        log.info("[GET TEMPLATE CLASSROOMS BY COURSE] Fetched {} classrooms for course ID: {}", response.getTotalElements(), courseId);

        return response;
    }

    @Override
    public TemplateClassroomResponse get(String templateClassroomId) {
        log.info("[GET TEMPLATE CLASSROOM] Fetching template classroom with ID: {}", templateClassroomId);

        TemplateClassroomResponse response = TemplateClassroomResponse.info(
                baseTemplateClassroomService.get(templateClassroomId, IsDelete.NOT_DELETED, TemplateClassroomMessage.NOT_FOUND));

        log.info("[GET TEMPLATE CLASSROOM] Successfully fetched template classroom with ID: {}", templateClassroomId);
        return response;
    }

    @Override
    public void delete(String templateClassroomId) {
        log.info("[DELETE TEMPLATE CLASSROOM] Deleting template classroom with ID: {}", templateClassroomId);

        baseTemplateClassroomService.delete(templateClassroomId, TemplateClassroomMessage.DELETE_FAILED);

        log.info("[DELETE TEMPLATE CLASSROOM] Successfully deleted template classroom with ID: {}", templateClassroomId);
    }

    /**
     * Initializes a TemplateClassroom object with basic details from the request.
     */
    private TemplateClassroom initializeTemplateClassroom(CreateTemplateClassroomRequest request) {
        TemplateClassroom templateClassroom = new TemplateClassroom();
        templateClassroom.setId(IdUtil.generateId());
        templateClassroom.setName(request.getName().trim());
        templateClassroom.setIsDeleted(IsDelete.NOT_DELETED);
        templateClassroom.setCreateTime(ZonedDateTime.now());
        templateClassroom.setCreateBy(SecurityContextHolderUtil.getAccount());
        log.debug("[INITIALIZE TEMPLATE CLASSROOM] Initialized template classroom entity: {}", templateClassroom);
        return templateClassroom;
    }

    /**
     * Fetches a TemplateClassroom by ID and validates that it is not deleted.
     */
    private TemplateClassroom fetchTemplateClassroom(String templateClassroomId, String failMessage) {
        log.debug("[FETCH TEMPLATE CLASSROOM] Fetching template classroom with ID: {}", templateClassroomId);
        return baseTemplateClassroomService.get(templateClassroomId, IsDelete.NOT_DELETED, failMessage);
    }

    /**
     * Updates audit fields (modifiedBy and modifiedTime) for a TemplateClassroom entity.
     */
    private void updateAuditInfo(TemplateClassroom templateClassroom) {
        templateClassroom.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateClassroom.setModifyTime(ZonedDateTime.now());
        log.debug("[UPDATE AUDIT INFO] Updated audit info for template classroom ID: {}", templateClassroom.getId());
    }

    /**
     * Validates the create request for necessary fields.
     */
    private void validateCreateRequest(CreateTemplateClassroomRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            log.error("[VALIDATE CREATE REQUEST] Name cannot be null or empty");
            throw new IllegalArgumentException("Template classroom name cannot be null or empty");
        }
        log.debug("[VALIDATE CREATE REQUEST] Validation passed for request: {}", request);
    }
}
