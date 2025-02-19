package com.kinder.kinder_ielts.controller;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomRequest;
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
@RequestMapping("/api/v1/class")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomServiceImpl classroomService;

    @GetMapping("/")
    public ResponseEntity<ResponseData<Page<ClassroomResponse>>> search(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String tutorId,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false, defaultValue = "NOT_DELETED") IsDelete isDelete,
            @RequestParam(required = false, defaultValue = "false") boolean includeDetail,
            @RequestParam(required = false, defaultValue = "true") boolean includeCourse,
            @RequestParam(required = false, defaultValue = "true") boolean includeTutor
            ){
        return ResponseUtil.getResponse(() -> classroomService.get(search, courseId, tutorId, studentId, isDelete, includeDetail, includeCourse, includeTutor, pageable), ClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> create(@PathVariable String courseId, @RequestBody CreateClassroomRequest request){
        return ResponseUtil.getResponse(() -> classroomService.createClassroom(courseId, request, ClassroomMessage.CREATE_FAILED), ClassroomMessage.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> getInfo(@PathVariable String id){
        return ResponseUtil.getResponse(() -> classroomService.getInfo(id), ClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> getDetail(@PathVariable String id){
        return ResponseUtil.getResponse(() -> classroomService.getDetail(id), ClassroomMessage.FOUND_SUCCESSFULLY);
    }

    @PatchMapping("/{id}/tutors")
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> update(@PathVariable String id, @RequestBody UpdateClassroomRequest request){
        return ResponseUtil.getResponse(() -> classroomService.updateInfo(id, request, ClassroomMessage.INFO_UPDATE_FAILED), ClassroomMessage.INFO_UPDATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String id){
        return ResponseUtil.getResponse(() -> classroomService.deleteCourse(id), ClassroomMessage.CLASS_IS_DELETED);
    }

    @GetMapping("/test/1")
    public ResponseEntity<ResponseData<ClassroomResponse>> test(){
        return ResponseUtil.getResponse(classroomService::test, ClassroomMessage.FOUND_SUCCESSFULLY);
    }
}
