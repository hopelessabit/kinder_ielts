package com.kinder.kinder_ielts.controller.template;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.template.homework.CreateTemplateHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.template.homework.UpdateTemplateHomeworkRequest;
import com.kinder.kinder_ielts.dto.response.template.homework.TemplateHomeworkResponse;
import com.kinder.kinder_ielts.response_message.TemplateHomeworkMessage;
import com.kinder.kinder_ielts.service.template.TemplateHomeworkService;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/template/homework")
@RequiredArgsConstructor
public class TemplateHomeworkController {
    private final TemplateHomeworkService templateHomeworkService;

    @GetMapping("/{templateHomeworkId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateHomeworkResponse>> get(@PathVariable String templateHomeworkId){
        return ResponseUtil.getResponse(() -> templateHomeworkService.get(templateHomeworkId, TemplateHomeworkMessage.NOT_FOUND), TemplateHomeworkMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/study-schedule/{templateStudyScheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<List<TemplateHomeworkResponse>>> getByStudyScheduleId(@PathVariable String templateStudyScheduleId){
        return ResponseUtil.getResponse(() -> templateHomeworkService.getByTemplateStudyScheduleId(templateStudyScheduleId, TemplateHomeworkMessage.NOT_FOUND), TemplateHomeworkMessage.FOUND_SUCCESSFULLY);
    }

    @PostMapping("/study-schedule/{templateStudyScheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateHomeworkResponse>> create(@PathVariable String templateStudyScheduleId, @RequestBody CreateTemplateHomeworkRequest request){
        return ResponseUtil.getResponse(() -> templateHomeworkService.create(templateStudyScheduleId, request, TemplateHomeworkMessage.CREATE_FAILED), TemplateHomeworkMessage.CREATED);
    }

    @PutMapping("/{templateHomeworkId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateHomeworkResponse>> update(@PathVariable String templateHomeworkId, @RequestBody UpdateTemplateHomeworkRequest request){
        return ResponseUtil.getResponse(() -> templateHomeworkService.update(templateHomeworkId, request, TemplateHomeworkMessage.INFO_UPDATED), TemplateHomeworkMessage.INFO_UPDATE_FAILED);
    }

    @DeleteMapping("/{templateHomeworkId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String templateHomeworkId){
        return ResponseUtil.getResponse(() -> templateHomeworkService.delete(templateHomeworkId, TemplateHomeworkMessage.DELETE_FAILED), TemplateHomeworkMessage.DELETED);
    }
}
