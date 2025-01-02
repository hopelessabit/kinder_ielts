package com.kinder.kinder_ielts.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.repository.CourseRepository;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.implement.CourseServiceImpl;
import com.kinder.kinder_ielts.service.implement.StudyScheduleServiceImpl;
import com.kinder.kinder_ielts.util.IdUtil;
import com.kinder.kinder_ielts.util.ResponseUtil;
import com.kinder.kinder_ielts.util.TimeZoneUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController()
@Slf4j
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class TestController {
    private final PasswordEncoder passwordEncoder;
    private final BaseCourseService baseCourseService;
    private final CourseServiceImpl courseService;
    private final StudyScheduleServiceImpl studyScheduleService;
    private final CourseRepository courseRepository;

    @GetMapping("/course/info/{id}")
    public ResponseEntity<ResponseData<CourseResponse>> getInfo(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> courseService.getInfo(id), CourseMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/course/detail/{id}")
    public ResponseEntity<ResponseData<CourseResponse>> getDetail(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> courseService.getDetail(id), CourseMessage.FOUND_SUCCESSFULLY);
    }
    @GetMapping("/course/all")
    public ResponseEntity<ResponseData<List<CourseResponse>>> getAll() {
        return ResponseUtil.getResponse(() -> courseRepository.findAll().stream().map(CourseResponse::info).toList(), CourseMessage.NOT_FOUND);
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
        log.info("Received Time: " + clientTime);
        return "Received time: " + clientTime;
    }

    @GetMapping("/get-encrypted-password/{password}")
    public String getEncryptedPassword(@PathVariable String password) {
        return passwordEncoder.encode(password);
    }

    @GetMapping("/str")
    public String str() {
        String a = TimeZoneUtil.getCurrentDateTime();
        return IdUtil.generateId();
    }

    @GetMapping("/study-schedule/all")
    public ResponseEntity<ResponseData<List<StudyScheduleResponse>>> get() {
        return ResponseUtil.getResponse(studyScheduleService::getAllInfo, StudyScheduleMessage.CREATED);
    }
}
