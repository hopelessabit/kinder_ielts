package com.kinder.kinder_ielts.controller.template;
import com.kinder.kinder_ielts.constant.ClassroomLinkStatus;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.template.classroom_link.CreateTemplateClassroomLink;
import com.kinder.kinder_ielts.dto.request.template.classroom_link.UpdateTemplateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.response.template.classroom_link.TemplateClassroomLinkResponse;
import com.kinder.kinder_ielts.response_message.TemplateClassroomLinkMessage;
import com.kinder.kinder_ielts.service.implement.template.TemplateClassroomLinkServiceImpl;
import com.kinder.kinder_ielts.service.template.TemplateClassroomLinkService;
import com.kinder.kinder_ielts.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/template/classroom-link")
@RequiredArgsConstructor
public class TemplateClassroomLinkController {
    private final TemplateClassroomLinkService templateClassroomLinkService;

    @GetMapping("/{templateClassroomLinkId}")
    public ResponseEntity<ResponseData<TemplateClassroomLinkResponse>> get(@PathVariable String templateClassroomLinkId) {
        return ResponseUtil.getResponse(() -> templateClassroomLinkService.get(templateClassroomLinkId, TemplateClassroomLinkMessage.NOT_FOUND), TemplateClassroomLinkMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/study-schedule/{templateStudyScheduleId}")
    public ResponseEntity<ResponseData<List<TemplateClassroomLinkResponse>>> getByTemplateStudyScheduleId(@PathVariable String templateStudyScheduleId) {
        return ResponseUtil.getResponse(() -> templateClassroomLinkService.getByTemplateStudyScheduleId(templateStudyScheduleId, TemplateClassroomLinkMessage.FOUND_FAILED), TemplateClassroomLinkMessage.FOUND_SUCCESSFULLY);
    }

    @PostMapping("/study-schedule/{templateStudyScheduleId}")
    public ResponseEntity<ResponseData<TemplateClassroomLinkResponse>> create(@PathVariable String templateStudyScheduleId, @RequestBody CreateTemplateClassroomLink request) {
        return ResponseUtil.getResponse(() -> templateClassroomLinkService.create(templateStudyScheduleId, request, TemplateClassroomLinkMessage.CREATE_FAILED), TemplateClassroomLinkMessage.CREATED);
    }

    @PutMapping("/{templateClassroomLinkId}")
    public ResponseEntity<ResponseData<TemplateClassroomLinkResponse>> updateInfo(@PathVariable String templateClassroomLinkId, @RequestBody UpdateTemplateClassroomLinkRequest request) {
        return ResponseUtil.getResponse(() -> templateClassroomLinkService.updateInfo(templateClassroomLinkId, request, TemplateClassroomLinkMessage.UPDATE_INFO_FAILED), TemplateClassroomLinkMessage.INFO_UPDATED);
    }

    @PatchMapping("/{templateClassroomLinkId}")
    public ResponseEntity<ResponseData<TemplateClassroomLinkResponse>> updateStatus(@PathVariable String templateClassroomLinkId, @RequestParam ClassroomLinkStatus status) {
        return ResponseUtil.getResponse(() -> templateClassroomLinkService.updateStatus(templateClassroomLinkId, status, TemplateClassroomLinkMessage.UPDATE_STATUS_FAILED), TemplateClassroomLinkMessage.STATUS_UPDATED);
    }
}
