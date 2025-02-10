package com.kinder.kinder_ielts.controller;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.student.CreateStudentRequest;
import com.kinder.kinder_ielts.dto.response.student.StudentResponse;
import com.kinder.kinder_ielts.response_message.StudentMessage;
import com.kinder.kinder_ielts.service.implement.StudentServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class StudentController {
    private final StudentServiceImpl studentService;

    @PostMapping("/create")
    @Operation(summary = "Create a new student")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<StudentResponse>> createStudent(@RequestBody CreateStudentRequest request){
        return ResponseUtil.getResponse(() -> studentService.createStudent(request, StudentMessage.CREATE_FAILED), StudentMessage.CREATED);
    }
}
