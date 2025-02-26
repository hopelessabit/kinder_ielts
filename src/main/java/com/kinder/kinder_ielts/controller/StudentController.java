package com.kinder.kinder_ielts.controller;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.student.CreateStudentRequest;
import com.kinder.kinder_ielts.dto.request.student.UpdateStudentInfoRequest;
import com.kinder.kinder_ielts.dto.response.student.StudentResponse;
import com.kinder.kinder_ielts.response_message.StudentMessage;
import com.kinder.kinder_ielts.service.implement.StudentServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class StudentController {
    private final StudentServiceImpl studentService;

    @GetMapping("/")
    @Operation(summary = "Search student")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'TUTOR')")
    public ResponseEntity<ResponseData<Page<StudentResponse>>> searchStudent(@RequestParam(required = false) List<String> courseIds,
                                                                            @RequestParam(required = false) List<String> classIds,
                                                                            @RequestParam(required = false) String name,
                                                                            Pageable pageable) {
        return ResponseUtil.getResponse(() -> studentService.search(courseIds, classIds, name, pageable), StudentMessage.FOUND_SUCCESSFULLY);
    }


    @PostMapping("/create")
    @Operation(summary = "Create a new student")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<StudentResponse>> createStudent(@RequestBody CreateStudentRequest request){
        return ResponseUtil.getResponse(() -> studentService.createStudent(request, StudentMessage.CREATE_FAILED), StudentMessage.CREATED);
    }

    @PutMapping("/info/{studentId}")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'ADMIN', 'MODERATOR')")
    public ResponseEntity<ResponseData<StudentResponse>> updateInfo(@RequestParam String studentId, @RequestBody UpdateStudentInfoRequest request){
        return ResponseUtil.getResponse(() -> studentService.updateInfo(studentId, request, StudentMessage.INFO_UPDATE_FAILED), StudentMessage.INFO_UPDATED);
    }
}
