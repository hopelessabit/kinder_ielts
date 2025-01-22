package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.study_schedule.CreateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.implement.StudyScheduleServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/study-schedule/")
@RequiredArgsConstructor
public class StudyScheduleController {
    private final StudyScheduleServiceImpl studyScheduleService;

    @PostMapping("/classroom/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<StudyScheduleResponse>> create(@PathVariable String id ,@RequestBody CreateStudyScheduleRequest request) {
        return ResponseUtil.getResponse(() -> studyScheduleService.createStudySchedule(id, request, StudyScheduleMessage.CREATE_FAILED), StudyScheduleMessage.CREATED);
    }

    @GetMapping("/info/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<StudyScheduleResponse>> get(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> studyScheduleService.getInfoWithDetail(id), StudyScheduleMessage.FOUND_SUCCESSFULLY);
    }
}
