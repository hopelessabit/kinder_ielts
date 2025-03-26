package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.student_homework.GradingStudentHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.student_homework.UpdateStudentHomeworkRequest;
import com.kinder.kinder_ielts.dto.response.student_homework.StudentHomeworkResponse;
import com.kinder.kinder_ielts.exception.BadRequestException;
import com.kinder.kinder_ielts.response_message.StudentHomeworkMessage;
import com.kinder.kinder_ielts.service.StudentHomeworkService;
import com.kinder.kinder_ielts.service.implement.StudentHomeworkServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student-homework/")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class StudentHomeworkController {
    private final StudentHomeworkServiceImpl studentHomeworkService;

    @GetMapping("/{homeworkId}/student/{studentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'TUTOR', 'STUDENT')")
    public ResponseEntity<ResponseData<StudentHomeworkResponse>> getStudentHomework(@PathVariable String homeworkId, @PathVariable String studentId, @RequestParam(required = false) boolean includeForAdmin, @RequestParam(required = false) boolean includeHomeworkDetail) {
        if (SecurityContextHolderUtil.getAccount().getRole().isStudent() && includeForAdmin)
            throw new BadRequestException(StudentHomeworkMessage.NOT_ALLOWED);
        return ResponseUtil.getResponse(() -> studentHomeworkService.getStudentHomework(homeworkId, studentId, includeForAdmin, includeHomeworkDetail, StudentHomeworkMessage.NOT_FOUND), StudentHomeworkMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/{homeworkId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'TUTOR')")
    public ResponseEntity<ResponseData<Page<StudentHomeworkResponse>>> getStudentHomeworks(@PathVariable String homeworkId, Pageable pageable) {
        return ResponseUtil.getResponse(() -> studentHomeworkService.getStudentHomeworks(homeworkId, pageable, StudentHomeworkMessage.NOT_FOUND), StudentHomeworkMessage.FOUND_SUCCESSFULLY);
    }

    @PutMapping("/{homeworkId}/submit")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
    public ResponseEntity<ResponseData<StudentHomeworkResponse>> studentSubmit(@PathVariable String homeworkId, @RequestBody UpdateStudentHomeworkRequest request) {
        return ResponseUtil.getResponse(() -> studentHomeworkService.studentSubmit(homeworkId, request, StudentHomeworkMessage.SUBMIT_FAILED), StudentHomeworkMessage.STUDENT_SUBMITTED);
    }

    @PutMapping("/{homeworkId}/grade")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'TUTOR')")
    public ResponseEntity<ResponseData<StudentHomeworkResponse>> gradingStudentHomework(@PathVariable String homeworkId, @RequestBody GradingStudentHomeworkRequest request) {
        return ResponseUtil.getResponse(() -> studentHomeworkService.gradingSubmit(homeworkId, request, StudentHomeworkMessage.GRADE_FAILED), StudentHomeworkMessage.GRADED);
    }

    @PatchMapping("/{homeworkId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'SYSTEM')")
    public ResponseEntity<ResponseData<Integer>> updateStudentHomework(@PathVariable String homeworkId){
        return ResponseUtil.getResponse(() -> studentHomeworkService.updateStudentHomeworkStatus(homeworkId, StudentHomeworkMessage.BATCH_UPDATE_STATUS_FAILED), StudentHomeworkMessage.BATCH_UPDATE_STATUS_SUCCESS);
    }
}
