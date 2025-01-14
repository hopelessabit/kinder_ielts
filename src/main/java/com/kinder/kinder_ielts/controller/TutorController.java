package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.response_message.TutorMessage;
import com.kinder.kinder_ielts.service.implement.TutorServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tutor/")
@RequiredArgsConstructor
public class TutorController {
    private final TutorServiceImpl tutorService;

    @GetMapping("/info/{id}")
    public ResponseEntity<ResponseData<TutorResponse>> getInfoById(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> tutorService.getTutor(id, false, IsDelete.NOT_DELETED, TutorMessage.NOT_FOUND), TutorMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/detail/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<TutorResponse>> getDetailById(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> tutorService.getTutor(id, true, IsDelete.NOT_DELETED, TutorMessage.NOT_FOUND), TutorMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseData<List<TutorResponse>>> getAll(@RequestParam(required = false) Boolean includeExtendDetails, @RequestParam(required = false) IsDelete isDelete) {
        if (includeExtendDetails == null){
            includeExtendDetails = false;
        }

        if (isDelete == null){
            isDelete = IsDelete.NOT_DELETED;
        }

        Boolean finalIncludeExtendDetails = includeExtendDetails;
        IsDelete finalIsDelete = isDelete;
        return ResponseUtil.getResponse(() -> tutorService.getAllTutor(finalIncludeExtendDetails, finalIsDelete, TutorMessage.NOT_FOUND), TutorMessage.FOUND_SUCCESSFULLY);
    }
}
