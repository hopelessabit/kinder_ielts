package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.classroom.link.CreateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.response.classroom_link.ClassroomLinkResponse;
import com.kinder.kinder_ielts.response_message.ClassroomLinkMessage;
import com.kinder.kinder_ielts.service.implement.ClassroomLinkServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/classroom-link/")
@RequiredArgsConstructor
public class ClassroomLinkController {
    private final ClassroomLinkServiceImpl classroomLinkService;

    @PostMapping("/schedule-id/{scheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomLinkResponse>> create(@PathVariable String scheduleId, @RequestBody CreateClassroomLinkRequest request){
        return ResponseUtil.getResponse(() -> classroomLinkService.createClassroomLink(scheduleId, request, ClassroomLinkMessage.CREATE_FAILED), ClassroomLinkMessage.CREATED);
    }
}
