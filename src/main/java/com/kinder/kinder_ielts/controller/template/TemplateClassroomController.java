package com.kinder.kinder_ielts.controller.template;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.template.classroom.CreateTemplateClassroomRequest;
import com.kinder.kinder_ielts.dto.response.template.classroom.TemplateClassroomResponse;
import com.kinder.kinder_ielts.response_message.TemplateClassroomMessage;
import com.kinder.kinder_ielts.service.TemplateClassroomService;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/template/classroom")
@RequiredArgsConstructor
public class TemplateClassroomController {
    private final TemplateClassroomService templateClassroomService;
//TODO: Add update info api
    @PostMapping("/course/{courseId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR','STUDENT')")
    public ResponseEntity<ResponseData<TemplateClassroomResponse>> create(@PathVariable String courseId, @RequestBody CreateTemplateClassroomRequest request) {
        return ResponseUtil.getResponse(() -> templateClassroomService.create(courseId, request, TemplateClassroomMessage.CREATE_FAILED), TemplateClassroomMessage.CREATED);
    }

    @GetMapping("/{templateClassroomId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR','STUDENT')")
    public ResponseEntity<ResponseData<TemplateClassroomResponse>> getById(@PathVariable String templateClassroomId) {
        return ResponseUtil.getResponse(() -> templateClassroomService.get(templateClassroomId), TemplateClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/course/{courseId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Page<TemplateClassroomResponse>>> getByCourseId(@PathVariable String courseId, Pageable pageable) {
        return ResponseUtil.getResponse(() -> templateClassroomService.getByCourseId(courseId, pageable), TemplateClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @DeleteMapping("/{templateClassroomId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String templateClassroomId) {
        return ResponseUtil.getResponse(() -> templateClassroomService.delete(templateClassroomId), TemplateClassroomMessage.CLASS_IS_DELETED);
    }
}
