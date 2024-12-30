package com.kinder.kinder_ielts.controller;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.implement.CourseServiceImpl;
import com.kinder.kinder_ielts.service.implement.StudyScheduleServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import com.microsoft.graph.models.*;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.graph.users.item.sendmail.SendMailPostRequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class TestController {
    private final PasswordEncoder passwordEncoder;
    private final BaseCourseService baseCourseService;
    private final CourseServiceImpl courseService;
    private final StudyScheduleServiceImpl studyScheduleService;

    @GetMapping("/info")
    public CourseResponse getInfo() {
        return CourseResponse.info(baseCourseService.get("0", IsDelete.NOT_DELETED, "hi"));
    }

    @GetMapping("/detail")
    public CourseResponse getDetail() {
        return CourseResponse.detail(baseCourseService.get("0", IsDelete.NOT_DELETED, "Hi"));
    }

    @PostMapping("/course")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<CourseResponse>> createCourse(@RequestBody CreateCourseRequest request){
        return ResponseUtil.getResponse(() -> courseService.createCourse(request), CourseMessage.CREATED);
    }

    @GetMapping("/api/current-time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC+7")
    public ZonedDateTime getCurrentTime() {
        return ZonedDateTime.now();
    }

    // Receive ZonedDateTime from Frontend
    @PostMapping("/api/submit-time")
    public String receiveTime(@RequestBody ZonedDateTime clientTime) {
        System.out.println("Received Time: " + clientTime);
        return "Received time: " + clientTime;
    }

    @GetMapping("/get-encrypted-password/{password}")
    public String getEncryptedPassword(@PathVariable String password) {
        return passwordEncoder.encode(password);
    }

    @GetMapping("/str")
    public String str() {
        return "Hello World";
    }

    @GetMapping("/study-schedule/all")
    public ResponseEntity<ResponseData<List<StudyScheduleResponse>>> get() {
        return ResponseUtil.getResponse(() -> studyScheduleService.getAllInfo(), StudyScheduleMessage.CREATED);
    }
}
