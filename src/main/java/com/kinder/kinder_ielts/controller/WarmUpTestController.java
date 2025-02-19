package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.warm_up_test.CreateWarmUpTestRequest;
import com.kinder.kinder_ielts.dto.request.warm_up_test.UpdateWarmUpTestInfoRequest;
import com.kinder.kinder_ielts.dto.response.warm_up_test.WarmUpTestResponse;
import com.kinder.kinder_ielts.response_message.WarmUpMessage;
import com.kinder.kinder_ielts.service.implement.WarmUpTestServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warmup-test/")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class WarmUpTestController {
    private final WarmUpTestServiceImpl warmUpTestService;

    @PostMapping("/schedule-id/{scheduleId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<WarmUpTestResponse>> create(@PathVariable String scheduleId, @RequestBody CreateWarmUpTestRequest request) {
        return ResponseUtil.getResponse(() -> warmUpTestService.createWarmUpTest(scheduleId, request, WarmUpMessage.CREATE_FAILED), WarmUpMessage.CREATED);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<WarmUpTestResponse>> getInfo(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> warmUpTestService.getInfo(id), WarmUpMessage.NOT_FOUND);
    }

    @PutMapping("/{warmUpTestId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<WarmUpTestResponse>> update(@PathVariable String warmUpTestId, @RequestBody UpdateWarmUpTestInfoRequest request) {
        return ResponseUtil.getResponse(() -> warmUpTestService.updateWarmUpTestInfo(warmUpTestId, request, WarmUpMessage.INFO_UPDATE_FAILED), WarmUpMessage.INFO_UPDATED);
    }

    @DeleteMapping("/{warmUpTestId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String warmUpTestId) {
        return ResponseUtil.getResponse(() -> warmUpTestService.deleteWarmUpTest(warmUpTestId, WarmUpMessage.DELETE_FAILED), WarmUpMessage.DELETED);
    }

    @PatchMapping("/{warmUpTestId}/view-status")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<WarmUpTestResponse>> updateViewStatus(@PathVariable String warmUpTestId) {
        return ResponseUtil.getResponse(() -> warmUpTestService.updateViewStatus(warmUpTestId, WarmUpMessage.VIEW_STATUS_UPDATE_FAILED), WarmUpMessage.VIEW_STATUS_UPDATED);
    }
}
