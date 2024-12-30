package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.warm_up_test.CreateWarmUpTestRequest;
import com.kinder.kinder_ielts.dto.response.warm_up_test.WarmUpTestResponse;
import com.kinder.kinder_ielts.response_message.WarmUpMessage;
import com.kinder.kinder_ielts.service.implement.WarmUpTestServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warmup-test/")
@RequiredArgsConstructor
public class WarmUpTestController {
    private final WarmUpTestServiceImpl warmUpTestService;

    @PostMapping("/schedule-id/{scheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<WarmUpTestResponse>> create(@PathVariable String scheduleId, @RequestBody CreateWarmUpTestRequest request) {
        return ResponseUtil.getResponse(() -> warmUpTestService.createWarmUpTest(scheduleId, request, WarmUpMessage.CREATE_FAILED), WarmUpMessage.CREATED);
    }

    @PostMapping("/info/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<WarmUpTestResponse>> getInfo(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> warmUpTestService.getInfo(id), WarmUpMessage.NOT_FOUND);
    }
}
