package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.homework.CreateHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.homework.UpdateHomeworkRequest;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.response_message.HomeworkMessage;
import com.kinder.kinder_ielts.service.implement.HomeworkServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/homework/")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class HomeworkController {
    private final HomeworkServiceImpl homeworkService;

    @PostMapping("/schedule-id/{scheduleId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<HomeworkResponse>> create(@PathVariable String scheduleId ,@RequestBody CreateHomeworkRequest request){
        return ResponseUtil.getResponse(() -> homeworkService.createHomework(scheduleId ,request, HomeworkMessage.CREATE_FAILED), HomeworkMessage.CREATED);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<HomeworkResponse>> getHomework(@PathVariable String id){
        return ResponseUtil.getResponse(() -> homeworkService.getInfo(id), HomeworkMessage.FOUND_SUCCESSFULLY);
    }

    @PutMapping("/{homeworkId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<HomeworkResponse>> update(@PathVariable String homeworkId, @RequestBody UpdateHomeworkRequest request){
        return ResponseUtil.getResponse(() -> homeworkService.updateHomeworkInfo(homeworkId, request, HomeworkMessage.INFO_UPDATE_FAILED), HomeworkMessage.INFO_UPDATED);
    }

    @DeleteMapping("/{homeworkId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String homeworkId){
        return ResponseUtil.getResponse(() -> homeworkService.deleteHomework(homeworkId, HomeworkMessage.DELETE_FAILED), HomeworkMessage.DELETED);
    }
}
