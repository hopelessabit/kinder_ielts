package com.kinder.kinder_ielts.controller.template;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.template.warmup_test.CreateTemplateWarmupTestRequest;
import com.kinder.kinder_ielts.dto.request.template.warmup_test.UpdateTemplateWarmupTestRequest;
import com.kinder.kinder_ielts.dto.response.template.warm_up_test.TemplateWarmUpTestResponse;
import com.kinder.kinder_ielts.response_message.TemplateWarmupTestMessage;
import com.kinder.kinder_ielts.service.implement.template.TemplateWarmupTestServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/template/warmup-test")
@RequiredArgsConstructor
public class TemplateWarmupTestController {
    private final TemplateWarmupTestServiceImpl templateWarmupTestService;

    @PostMapping("/study-schedule/{templateStudyScheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateWarmUpTestResponse>> create(@PathVariable String templateStudyScheduleId, @RequestBody CreateTemplateWarmupTestRequest request){
        return ResponseUtil.getResponse(() -> templateWarmupTestService.create(templateStudyScheduleId, request, TemplateWarmupTestMessage.CREATE_FAILED), TemplateWarmupTestMessage.CREATED);
    }

    @GetMapping("/{templateWarmupTestId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateWarmUpTestResponse>> get(@PathVariable String templateWarmupTestId){
        return ResponseUtil.getResponse(() -> templateWarmupTestService.get(templateWarmupTestId, TemplateWarmupTestMessage.FOUND_SUCCESSFULLY), TemplateWarmupTestMessage.NOT_FOUND);
    }

    @GetMapping("/study-schedule/{templateStudyScheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<List<TemplateWarmUpTestResponse>>> getByTemplateStudyScheduleId(@PathVariable String templateStudyScheduleId){
        return ResponseUtil.getResponse(() -> templateWarmupTestService.getByTemplateStudyScheduleId(templateStudyScheduleId, TemplateWarmupTestMessage.FOUND_SUCCESSFULLY), TemplateWarmupTestMessage.NOT_FOUND);
    }

    @PutMapping("/{templateWarmupTestId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateWarmUpTestResponse>> updateInfo(@PathVariable String templateWarmupTestId, @RequestBody UpdateTemplateWarmupTestRequest request){
        return ResponseUtil.getResponse(() -> templateWarmupTestService.updateInfo(templateWarmupTestId, request, TemplateWarmupTestMessage.UPDATE_INFO_FAILED), TemplateWarmupTestMessage.INFO_UPDATED);
    }

    @PatchMapping("/{templateWarmupTestId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateWarmUpTestResponse>> updateStatus(@PathVariable String templateWarmupTestId, @RequestParam ViewStatus status){
        return ResponseUtil.getResponse(() -> templateWarmupTestService.updateStatus(templateWarmupTestId, status, TemplateWarmupTestMessage.UPDATE_STATUS_FAILED), TemplateWarmupTestMessage.STATUS_UPDATED);
    }

    @DeleteMapping("/{templateWarmupTestId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String templateWarmupTestId){
        return ResponseUtil.getResponse(() -> templateWarmupTestService.delete(templateWarmupTestId, TemplateWarmupTestMessage.DELETE_FAILED), TemplateWarmupTestMessage.DELETED);
    }
}

