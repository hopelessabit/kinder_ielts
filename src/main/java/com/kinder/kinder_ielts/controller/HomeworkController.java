package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.homework.CreateHomeworkRequest;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.response_message.HomeworkMessage;
import com.kinder.kinder_ielts.service.implement.HomeworkServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/homework/")
@RequiredArgsConstructor
public class HomeworkController {
    private final HomeworkServiceImpl homeworkService;

    @PostMapping("/schedule-id/{scheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<HomeworkResponse>> create(@PathVariable String scheduleId ,@RequestBody CreateHomeworkRequest request){
        return ResponseUtil.getResponse(() -> homeworkService.createHomework(scheduleId ,request, HomeworkMessage.CREATE_FAILED), HomeworkMessage.CREATED);
    }

    @GetMapping("/info/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<HomeworkResponse>> getHomework(@PathVariable String id){
        return ResponseUtil.getResponse(() -> homeworkService.getInfo(id), HomeworkMessage.FOUND_SUCCESSFULLY);
    }
}
