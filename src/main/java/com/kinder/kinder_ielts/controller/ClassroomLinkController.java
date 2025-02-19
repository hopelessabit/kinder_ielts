package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.classroom.link.CreateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.request.classroom.link.UpdateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.response.classroom_link.ClassroomLinkResponse;
import com.kinder.kinder_ielts.response_message.ClassroomLinkMessage;
import com.kinder.kinder_ielts.service.implement.ClassroomLinkServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/classroom-link/")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class ClassroomLinkController {
    private final ClassroomLinkServiceImpl classroomLinkService;

    @PostMapping("/schedule-id/{scheduleId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomLinkResponse>> create(@PathVariable String scheduleId, @RequestBody CreateClassroomLinkRequest request){
        return ResponseUtil.getResponse(() -> classroomLinkService.createClassroomLink(scheduleId, request, ClassroomLinkMessage.CREATE_FAILED), ClassroomLinkMessage.CREATED);
    }

    @GetMapping("/schedule-id/{scheduleId}")
    public ResponseEntity<ResponseData<Page<ClassroomLinkResponse>>> get(@PathVariable String scheduleId,
                                                                         Page page,
                                                                         @RequestParam(required = false) String title,
                                                                         @RequestParam(required = false) String description,
                                                                         @RequestParam(required = false, defaultValue = "true") boolean includeStudySchedule){
        return ResponseUtil.getResponse(() -> classroomLinkService.get(scheduleId, title, description, includeStudySchedule, page, ClassroomLinkMessage.FOUND_SUCCESSFULLY), ClassroomLinkMessage.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<ClassroomLinkResponse>> update(@PathVariable String id, @RequestBody UpdateClassroomLinkRequest request){
        return ResponseUtil.getResponse(() -> classroomLinkService.updateInfo(id, request, ClassroomLinkMessage.INFO_UPDATE_FAILED), ClassroomLinkMessage.INFO_UPDATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String id){
        return ResponseUtil.getResponse(() -> classroomLinkService.delete(id, ClassroomLinkMessage.DELETE_FAILED), ClassroomLinkMessage.DELETED);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update a classroom link's status")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<ClassroomLinkResponse>> updateStatus(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> classroomLinkService.updateViewStatus(id, ClassroomLinkMessage.VIEW_STATUS_UPDATE_FAILED), ClassroomLinkMessage.VIEW_STATUS_UPDATED);
    }
}
