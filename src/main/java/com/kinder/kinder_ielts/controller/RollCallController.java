package com.kinder.kinder_ielts.controller;
import com.azure.core.annotation.Put;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.roll_call.SearchRollCallRequest;
import com.kinder.kinder_ielts.dto.request.roll_call.UpdateRollCallRequest;
import com.kinder.kinder_ielts.dto.request.roll_call.UpdateRollCallsRequest;
import com.kinder.kinder_ielts.dto.response.roll_call.RollCallResponse;
import com.kinder.kinder_ielts.entity.RollCall;
import com.kinder.kinder_ielts.response_message.RollCallMessage;
import com.kinder.kinder_ielts.service.implement.RollCallServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roll-call")
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
    @Operation(summary = "Update multiple roll calls")
    public ResponseEntity<ResponseData<List<RollCallResponse>>> updateRollCalls(@PathVariable String studyScheduleId,
                                                                               @RequestBody UpdateRollCallsRequest request){
        return ResponseUtil.getResponse(() -> rollCallService.updateRollCalls(studyScheduleId, request, RollCallMessage.UPDATE_FAILED), RollCallMessage.UPDATE_SUCCESSFULLY);
    }

    @PutMapping("/study-schedule/{studyScheduleId}")
    @Operation(summary = "Update a roll call")
    public ResponseEntity<ResponseData<RollCallResponse>> updateRollCall(@PathVariable String studyScheduleId,
                                                                        @RequestBody UpdateRollCallRequest request){
        return ResponseUtil.getResponse(() -> rollCallService.updateRollCall(studyScheduleId, request, RollCallMessage.UPDATE_FAILED), RollCallMessage.UPDATE_SUCCESSFULLY);
    }

    @GetMapping("/")
    public ResponseEntity<ResponseData<Page<RollCallResponse>>> search(Pageable pageable, @ModelAttribute SearchRollCallRequest request){
        return ResponseUtil.getResponse(() -> rollCallService.search(pageable, request), RollCallMessage.MULTIPLE_FOUND_SUCCESSFULLY);
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseData<Page<RollCallResponse>>> test(){
        return ResponseUtil.getResponse(() -> rollCallService.test(), RollCallMessage.MULTIPLE_FOUND_SUCCESSFULLY);
    }

}
