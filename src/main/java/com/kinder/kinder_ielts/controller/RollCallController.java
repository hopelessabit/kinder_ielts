package com.kinder.kinder_ielts.controller;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.response.roll_call.RollCallResponse;
import com.kinder.kinder_ielts.entity.id.RollCallId;
import com.kinder.kinder_ielts.response_message.RollCallMessage;
import com.kinder.kinder_ielts.service.RollCallService;
import com.kinder.kinder_ielts.service.implement.RollCallServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roll-call")
@RequiredArgsConstructor
public class RollCallController {
    private final RollCallServiceImpl rollCallService;

//    @GetMapping("/study-schedule/{studyScheduleId}")
//    public ResponseEntity<ResponseData<List<RollCallResponse>>> getByStudyScheduleId(String studyScheduleId) {
//        return ResponseUtil.getResponse(() -> rollCallService.getByStudyScheduleId(studyScheduleId, RollCallMessage.STUDY_SCHEDULE_NOT_FOUND), RollCallMessage.FOUND_SUCCESSFULLY);
//    }
}
