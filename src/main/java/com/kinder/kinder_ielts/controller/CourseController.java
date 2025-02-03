package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseStudent;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseTutors;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.implement.CourseServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseServiceImpl courseService;

    @GetMapping("")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<Page<CourseResponse>>> search(
        Pageable pageable,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Float minPrice,
        @RequestParam(required = false) Float maxPrice,
        @RequestParam(required = false) String levelId,
        @RequestParam(required = false) String tutorId,
        @RequestParam(required = false) String studentId,
        @RequestParam(required = false) IsDelete isDelete
    ){
        return ResponseUtil.getResponse(() -> courseService.get(name, minPrice, maxPrice, levelId, tutorId, studentId, isDelete, pageable), CourseMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<ResponseData<CourseResponse>> getInfo(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> courseService.getInfo(id), CourseMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/detail/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<CourseResponse>> getDetail(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> courseService.getDetail(id), CourseMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/all")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<List<CourseResponse>>> getAll(@RequestParam(required = false) IsDelete isDelete) {
        if (isDelete == null){
            isDelete = IsDelete.NOT_DELETED;
        }

        IsDelete finalIsDelete = isDelete;

        return ResponseUtil.getResponse(() -> courseService.getAll(finalIsDelete, CourseMessage.NOT_FOUND), CourseMessage.FOUND_SUCCESSFULLY);
    }

    @PostMapping("/course")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<CourseResponse>> createCourse(@RequestBody CreateCourseRequest request){
        return ResponseUtil.getResponse(() -> courseService.createCourse(request), CourseMessage.CREATED);
    }

    @PatchMapping("/{id}/tutors")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<Integer>> modifyTutors(@PathVariable String id, @RequestBody UpdateCourseTutors request){
        return ResponseUtil.getResponse(() -> courseService.updateCourseTutor(id, request, CourseMessage.UPDATE_TUTORS_FAILED), CourseMessage.UPDATE_TUTORS_SUCCESSFULLY);
    }

    @PatchMapping("/{id}/students")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<Integer>> modifyStudents(@PathVariable String id, @RequestBody UpdateCourseStudent request){
        return ResponseUtil.getResponse(() -> courseService.updateCourseStudent(id, request, CourseMessage.UPDATE_STUDENTS_FAILED), CourseMessage.UPDATE_STUDENTS_SUCCESSFULLY);
    }
}
