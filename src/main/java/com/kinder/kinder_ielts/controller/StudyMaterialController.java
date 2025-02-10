package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.study_material.CreateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.study_material.ModifyStudyMaterialStatusRequest;
import com.kinder.kinder_ielts.dto.request.study_material.UpdateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.response.study_material.StudyMaterialResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.response_message.HomeworkMessage;
import com.kinder.kinder_ielts.response_message.StudyMaterialMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.implement.StudyMaterialServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stuty-material")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class StudyMaterialController {
    private final StudyMaterialServiceImpl studyMaterialService;

    @PostMapping("/schedule-id/{scheduleId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<StudyMaterialResponse>> createStudyMaterial(@PathVariable("scheduleId") String scheduleId, @RequestBody CreateStudyMaterialRequest request) {
        return ResponseUtil.getResponse(() -> studyMaterialService.createStudyMaterial(scheduleId, request, StudyScheduleMessage.CREATE_FAILED), StudyScheduleMessage.CREATED);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<StudyMaterialResponse>> getHomework(@PathVariable String id){
        return ResponseUtil.getResponse(() -> studyMaterialService.getInfo(id), StudyMaterialMessage.FOUND_SUCCESSFULLY);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<StudyMaterialResponse>> update(@PathVariable String id, @RequestBody UpdateStudyMaterialRequest request){
        return ResponseUtil.getResponse(() -> studyMaterialService.update(id, request, StudyMaterialMessage.INFO_UPDATE_FAILED), StudyMaterialMessage.INFO_UPDATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String id){
        return ResponseUtil.getResponse(() -> studyMaterialService.delete(id, StudyMaterialMessage.DELETE_FAILED), StudyMaterialMessage.DELETED);
    }

//    @PutMapping("/{studyMaterialId}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
//    public ResponseEntity<ResponseData<StudyMaterialResponse>> updateStudyMaterialStatus(@PathVariable("studyMaterialId") String studyMaterialId, ModifyStudyMaterialStatusRequest request) {
//        return ResponseUtil.getResponse(() -> studyMaterialService.updateStudyMaterialStatus(studyMaterialId, request, StudyMaterialMessage.STATUS_UPDATE_FAILED), StudyMaterialMessage.STATUS_UPDATED);
//    }
}
