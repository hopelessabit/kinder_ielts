package com.kinder.kinder_ielts.controller;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.service.ClassroomService;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class/")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> create(@RequestBody CreateClassroomRequest request){
        return ResponseUtil.getResponse(() -> classroomService.createClassroom(request, ClassroomMessage.CREATE_FAILED), ClassroomMessage.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ClassroomResponse>> getInfo(@PathVariable String id){
        return ResponseUtil.getResponse(() -> classroomService.getInfo(id), ClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/detail/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> getDetail(@PathVariable String id){
        return ResponseUtil.getResponse(() -> classroomService.getDetail(id), ClassroomMessage.FOUND_SUCCESSFULLY);
    }


}
