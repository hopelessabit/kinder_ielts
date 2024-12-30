package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.implement.CourseServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseServiceImpl courseService;
    private final BaseCourseService baseCourseService;

    @GetMapping("/info/{id}")
    public ResponseEntity<ResponseData<CourseResponse>> getInfo(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> courseService.getInfo(id), CourseMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseData<CourseResponse>> getDetail(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> courseService.getDetail(id), CourseMessage.FOUND_SUCCESSFULLY);
    }

    @PostMapping("/course")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<CourseResponse>> createCourse(@RequestBody CreateCourseRequest request){
        return ResponseUtil.getResponse(() -> courseService.createCourse(request), CourseMessage.CREATED);
    }
}
