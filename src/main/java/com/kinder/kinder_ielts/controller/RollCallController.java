package com.kinder.kinder_ielts.controller;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.roll_call.CreateRollCallsRequest;
import com.kinder.kinder_ielts.dto.request.roll_call.SearchRollCallRequest;
import com.kinder.kinder_ielts.dto.request.roll_call.RollCallRequest;
import com.kinder.kinder_ielts.dto.request.roll_call.UpdateRollCallsRequest;
import com.kinder.kinder_ielts.dto.response.roll_call.RollCallResponse;
import com.kinder.kinder_ielts.response_message.RollCallMessage;
import com.kinder.kinder_ielts.service.implement.RollCallServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roll-call")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
public class RollCallController {
    private final RollCallServiceImpl rollCallService;

    @GetMapping("/study-schedule/{studyScheduleId}")
    public ResponseEntity<ResponseData<List<RollCallResponse>>> getByStudyScheduleId(String studyScheduleId,
                                                                                     @RequestParam(required = false, defaultValue = "false") Boolean includeForAdmin,
                                                                                     @RequestParam(required = false, defaultValue = "NOT_DELETED") IsDelete isDelete,
                                                                                     @RequestParam(required = false, defaultValue = "false") Boolean includeStudyScheduleInfo) {
        return ResponseUtil.getResponse(() -> rollCallService.getAllByStudyScheduleId(studyScheduleId, includeForAdmin, isDelete, includeStudyScheduleInfo, RollCallMessage.STUDY_SCHEDULE_NOT_FOUND), RollCallMessage.FOUND_SUCCESSFULLY);
    }

    @PutMapping("/study-schedule/{studyScheduleId}/multiple")
    @PreAuthorize("hasAnyAuthority('ADMIN','TUTOR','MODERATOR')")
    @Operation(summary = "Update multiple roll calls")
    public ResponseEntity<ResponseData<List<RollCallResponse>>> updateRollCalls(@PathVariable String studyScheduleId,
                                                                               @RequestBody UpdateRollCallsRequest request){
        return ResponseUtil.getResponse(() -> rollCallService.updateRollCalls(studyScheduleId, request, RollCallMessage.UPDATE_FAILED), RollCallMessage.UPDATE_SUCCESSFULLY);
    }

    @PostMapping("/study-schedule/{studyScheduleId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR', 'TUTOR')")
    @Operation(summary = "Create roll call")
    public ResponseEntity<ResponseData<List<RollCallResponse>>> createRollCalls(@PathVariable String studyScheduleId,
                                                                               @RequestBody CreateRollCallsRequest request){
        return ResponseUtil.getResponse(() -> rollCallService.createRollCalls(studyScheduleId, request, RollCallMessage.CREATE_FAILED), RollCallMessage.CREATE_SUCCESSFULLY);
    }

    @PutMapping("/study-schedule/{studyScheduleId}")
    @Operation(summary = "Update a roll call")
    public ResponseEntity<ResponseData<RollCallResponse>> updateRollCall(@PathVariable String studyScheduleId,
                                                                        @RequestBody RollCallRequest request){
        return ResponseUtil.getResponse(() -> rollCallService.updateRollCall(studyScheduleId, request, RollCallMessage.UPDATE_FAILED), RollCallMessage.UPDATE_SUCCESSFULLY);
    }

    @GetMapping("/")
    @Operation(summary = "Search roll calls")
    public ResponseEntity<ResponseData<Page<RollCallResponse>>> search(Pageable pageable, @ModelAttribute SearchRollCallRequest request){
        return ResponseUtil.getResponse(() -> rollCallService.search(pageable, request), RollCallMessage.MULTIPLE_FOUND_SUCCESSFULLY);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<ResponseData<List<RollCallResponse>>> getByClassId(String classId,
                                                                             @RequestParam(required = false, defaultValue = "false") Boolean includeForAdmin) {
        return ResponseUtil.getResponse(() -> rollCallService.getAllByClassId(classId, includeForAdmin, RollCallMessage.STUDY_SCHEDULE_NOT_FOUND), RollCallMessage.FOUND_SUCCESSFULLY);
    }

}
