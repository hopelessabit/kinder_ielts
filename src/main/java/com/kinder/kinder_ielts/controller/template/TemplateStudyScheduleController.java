package com.kinder.kinder_ielts.controller.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.CreateTemplateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.UpdateTemplateStudySchedulePlaceRequest;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.UpdateTemplateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.response.template.classroom.TemplateClassroomResponse;
import com.kinder.kinder_ielts.dto.response.template.study_schedule.TemplateStudyScheduleResponse;
import com.kinder.kinder_ielts.response_message.TemplateStudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.service.implement.template.TemplateStudyScheduleServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/template/study-schedule")
@RequiredArgsConstructor
public class TemplateStudyScheduleController {
    private final TemplateStudyScheduleServiceImpl templateStudyScheduleService;

    @GetMapping("/classroom/{classroomId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<List<TemplateStudyScheduleResponse>>> getByClassroomId(@PathVariable String classroomId) {
        return ResponseUtil.getResponse(() -> templateStudyScheduleService.getByTemplateClassroomId(classroomId, TemplateStudyScheduleMessage.FOUND_FAILED), TemplateStudyScheduleMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/{templateStudyScheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateStudyScheduleResponse>> get(@PathVariable String templateStudyScheduleId) {
        return ResponseUtil.getResponse(() -> templateStudyScheduleService.get(templateStudyScheduleId, TemplateStudyScheduleMessage.NOT_FOUND), TemplateStudyScheduleMessage.FOUND_SUCCESSFULLY);
    }

    @PostMapping("/classroom/{classroomId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateStudyScheduleResponse>> create(@PathVariable String classroomId, @RequestBody CreateTemplateStudyScheduleRequest request) {
        return ResponseUtil.getResponse(() -> templateStudyScheduleService.createTemplateStudySchedule(classroomId, request, TemplateStudyScheduleMessage.CREATE_FAILED), TemplateStudyScheduleMessage.CREATED);
    }

    @PutMapping("/{templateStudyScheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<TemplateStudyScheduleResponse>> update(@PathVariable String templateStudyScheduleId, @RequestBody UpdateTemplateStudyScheduleRequest request) {
        return ResponseUtil.getResponse(() -> templateStudyScheduleService.updateInfo(templateStudyScheduleId, request, TemplateStudyScheduleMessage.UPDATE_INFO_FAILED), TemplateStudyScheduleMessage.INFO_UPDATED);
    }


    @PatchMapping("/{templateStudyScheduleId}/classroom/{templateClassroomId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public  ResponseEntity<ResponseData<Void>> modifyPlace(@PathVariable String templateClassroomId, @PathVariable String templateStudyScheduleId, @RequestParam Integer place){
        return ResponseUtil.getResponse(() -> templateStudyScheduleService.modifyPlace(templateClassroomId, templateStudyScheduleId, place, "Modify place failed"), "Modify place success");
    }

    @PatchMapping("/{templateClassroomId}/places")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public  ResponseEntity<ResponseData<Void>> modifyPlaceV2(@PathVariable String templateClassroomId, @RequestBody UpdateTemplateStudySchedulePlaceRequest request){
        return ResponseUtil.getResponse(() -> templateStudyScheduleService.modifyPlaceV2(templateClassroomId, request, "Modify place failed"), "Modify place success");
    }

    @DeleteMapping("/{templateStudyScheduleId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete (@PathVariable String templateStudyScheduleId){
        return ResponseUtil.getResponse(() -> templateStudyScheduleService.delete(templateStudyScheduleId, TemplateStudyScheduleMessage.DELETE_FAILED), TemplateStudyScheduleMessage.DELETED);
    }
}
