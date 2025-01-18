package com.kinder.kinder_ielts.controller.template;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.template.CreateTemplateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.template.study_material.UpdateTemplateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.response.template.study_material.TemplateStudyMaterialResponse;
import com.kinder.kinder_ielts.response_message.TemplateStudyMaterialMessage;
import com.kinder.kinder_ielts.service.template.TemplateStudyMaterialService;
import com.kinder.kinder_ielts.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/template/study-material")
@RequiredArgsConstructor
public class TemplateStudyMaterialController {
    private final TemplateStudyMaterialService templateStudyMaterialService;

    @GetMapping("/{templateStudyMaterialId}")
    public ResponseEntity<ResponseData<TemplateStudyMaterialResponse>> get(@PathVariable String templateStudyMaterialId) {
        return ResponseUtil.getResponse(() -> templateStudyMaterialService.get(templateStudyMaterialId, TemplateStudyMaterialMessage.CREATE_FAILED), TemplateStudyMaterialMessage.CREATED);
    }

    @GetMapping("/study-schedule/{templateStudyScheduleId}")
    public ResponseEntity<ResponseData<List<TemplateStudyMaterialResponse>>> getByStudyScheduleId(@PathVariable String templateStudyScheduleId) {
        return ResponseUtil.getResponse(() -> templateStudyMaterialService.getByTemplateStudyScheduleId(templateStudyScheduleId, TemplateStudyMaterialMessage.CREATE_FAILED), TemplateStudyMaterialMessage.CREATED);
    }

    @PostMapping("/study-schedule/{templateStudyScheduleId}")
    public ResponseEntity<ResponseData<TemplateStudyMaterialResponse>> create(@PathVariable String templateStudyScheduleId, @RequestBody CreateTemplateStudyMaterialRequest request) {
        return ResponseUtil.getResponse(() -> templateStudyMaterialService.create(templateStudyScheduleId, request, TemplateStudyMaterialMessage.CREATE_FAILED), TemplateStudyMaterialMessage.CREATED);
    }

    @PutMapping("/{templateStudyMaterialId}")
    public ResponseEntity<ResponseData<TemplateStudyMaterialResponse>> updateInfo(@PathVariable String templateStudyMaterialId, @RequestBody UpdateTemplateStudyMaterialRequest request) {
        return ResponseUtil.getResponse(() -> templateStudyMaterialService.updateInfo(templateStudyMaterialId, request, TemplateStudyMaterialMessage.UPDATE_INFO_FAILED), TemplateStudyMaterialMessage.INFO_UPDATED);
    }

    @PatchMapping("/{templateStudyMaterialId}")
    public ResponseEntity<ResponseData<TemplateStudyMaterialResponse>> updatePrivacyStatus(@PathVariable String templateStudyMaterialId, @RequestParam StudyMaterialStatus privacyStatus) {
        return ResponseUtil.getResponse(() -> templateStudyMaterialService.updatePrivacyStatus(templateStudyMaterialId, privacyStatus, TemplateStudyMaterialMessage.UPDATE_STATUS_FAILED), TemplateStudyMaterialMessage.STATUS_UPDATED);
    }

    @DeleteMapping("/{templateStudyMaterialId}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String templateStudyMaterialId) {
        return ResponseUtil.getResponse(() -> templateStudyMaterialService.delete(templateStudyMaterialId, TemplateStudyMaterialMessage.DELETE_FAILED), TemplateStudyMaterialMessage.DELETED);
    }
}
