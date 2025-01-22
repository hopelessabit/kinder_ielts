package com.kinder.kinder_ielts.controller;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomTutorRequest;
import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.implement.ClassroomServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class/")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomServiceImpl classroomService;


    @GetMapping("/")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<Page<ClassroomResponse>>> search(
            Pageable pageable,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String tutorId,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) IsDelete isDelete
            ){
        return ResponseUtil.getResponse(() -> classroomService.get(title, courseId, tutorId, studentId, isDelete, pageable), ClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @PostMapping("/course/{courseId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> create(@PathVariable String courseId, @RequestBody CreateClassroomRequest request){
        return ResponseUtil.getResponse(() -> classroomService.createClassroom(courseId, request, ClassroomMessage.CREATE_FAILED), ClassroomMessage.CREATED);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> getInfo(@PathVariable String id){
        return ResponseUtil.getResponse(() -> classroomService.getInfo(id), ClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/detail/{id}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> getDetail(@PathVariable String id){
        return ResponseUtil.getResponse(() -> classroomService.getDetail(id), ClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @PatchMapping("/{id}/tutors")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<Integer>> modifyTutors(@PathVariable String id, @RequestBody UpdateClassroomTutorRequest request){
        return ResponseUtil.getResponse(() -> classroomService.updateClassroomTutor(id, request, CourseMessage.UPDATE_TUTORS_FAILED), CourseMessage.UPDATE_TUTORS_SUCCESSFULLY);
    }

//    @PatchMapping("/{id}/students")
//    @SecurityRequirement(name = "Bearer")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
//    public ResponseEntity<ResponseData<Integer>> modifyStudents(@PathVariable String id, @RequestBody UpdateCourseStudent request){
//        return ResponseUtil.getResponse(() -> classroomService.updateCourseStudent(id, request, CourseMessage.UPDATE_STUDENTS_FAILED), CourseMessage.UPDATE_STUDENTS_SUCCESSFULLY);
//    }

}
