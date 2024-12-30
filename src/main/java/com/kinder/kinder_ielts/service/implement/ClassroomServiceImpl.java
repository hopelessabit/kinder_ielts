package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomTutorRequest;
import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.entity.id.ClassroomTutorId;
import com.kinder.kinder_ielts.entity.id.CourseTutorId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.ClassroomService;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.service.base.BaseCourseTutorService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomServiceImpl implements ClassroomService {
    private final BaseAccountService baseAccountService;
    private final BaseTutorService baseTutorService;
    private final BaseCourseService baseCourseService;
    private final BaseClassroomService baseClassroomService;
    private final BaseCourseTutorService baseCourseTutorService;
    private final BaseClassroomTutorService baseClassroomTutorService;

    /**
     * Create a new Classroom.
     */
    public ClassroomResponse createClassroom(CreateClassroomRequest request, String message) {
        log.info("Starting process to create a new Classroom.");
        ZonedDateTime createTime = ZonedDateTime.now();
        Classroom classroom = ModelMapper.map(request, createTime);
        String createById = SecurityContextHolderUtil.getAccountId();
        log.debug("Creator Account ID fetched from security context: {}", createById);

        log.debug("Fetching account for creator ID: {}", createById);
        Account account = baseAccountService.get(createById, IsDelete.NOT_DELETED, message);
        classroom.setCreateBy(account);
        log.debug("Account fetched successfully and assigned to classroom creator.");

        log.debug("Fetching course with ID: {}", request.getCourseId());
        Course belongToCourse = baseCourseService.get(request.getCourseId(), IsDelete.NOT_DELETED, message);
        classroom.setBelongToCourse(belongToCourse);
        log.debug("Course fetched successfully and assigned to the classroom.");

        Classroom result = baseClassroomService.create(classroom, message);
        log.info("Classroom creation completed successfully.");



        if (request.getTutorIds() != null && !request.getTutorIds().isEmpty()) {
            log.debug("Fetching tutors with IDs: {}", request.getTutorIds());
            List<Tutor> tutors = baseTutorService.get(request.getTutorIds(), AccountStatus.ACTIVE, message);

            List<ClassroomTutor> classroomTutors = new ArrayList<>();
            ZonedDateTime currentTime = ZonedDateTime.now();
            tutors.forEach(tutor -> {
                classroomTutors.add(new ClassroomTutor(classroom, tutor, currentTime, account, createTime));
                log.debug("Added tutor with ID: {} to classroom tutors list.", tutor.getId());
            });

            log.debug("Add [Classroom Tutor]");
            baseClassroomTutorService.create(classroomTutors, message);

            classroom.setClassroomTutors(classroomTutors);
        }

        return ClassroomResponse.detailWithDetails(baseClassroomService.update(classroom, message));
    }

    /**
     * Get basic information about a classroom.
     */
    public ClassroomResponse getInfo(String id) {
        log.info("Fetching basic info for Classroom ID: {}", id);
        ClassroomResponse response = ClassroomResponse.infoWithDetails(
                baseClassroomService.get(id, IsDelete.NOT_DELETED, ClassroomMessage.NOT_FOUND)
        );
        log.info("Successfully fetched classroom info for ID: {}", id);
        return response;
    }

    /**
     * Get detailed information about a classroom.
     */
    public ClassroomResponse getDetail(String id) {
        log.info("Fetching detailed info for Classroom ID: {}", id);
        ClassroomResponse response = ClassroomResponse.detailWithDetails(
                baseClassroomService.get(id, IsDelete.NOT_DELETED, ClassroomMessage.NOT_FOUND)
        );
        log.info("Successfully fetched detailed classroom info for ID: {}", id);
        return response;
    }

    /**
     * Delete a classroom by ID.
     */
    public ResponseData deleteCourse(String id) {
        log.info("Starting deletion process for Classroom with ID: {}", id);
        baseClassroomService.delete(id, CourseMessage.DELETE_FAILED);
        log.info("Classroom successfully deleted with ID: {}", id);
        return ResponseData.ok(CourseMessage.DELETED);
    }

    /**
     * Perform partial updates on classroom details.
     */
    public void performUpdateInfo(Classroom classroom, UpdateClassroomRequest request) {
        log.debug("Performing updates on Classroom fields.");
        classroom.setDescription(CompareUtil.compare(request.getDescription(), classroom.getDescription()));
        classroom.setTimeDescription(CompareUtil.compare(request.getTimeDescription(), classroom.getTimeDescription()));
        log.debug("Classroom fields updated successfully.");
    }

    /**
     * Update classroom information.
     */
    public ClassroomResponse updateInfo(String id, UpdateClassroomRequest request) {
        log.info("Updating Classroom information for ID: {}", id);
        Classroom classroom = baseClassroomService.get(id, IsDelete.NOT_DELETED, ClassroomMessage.CLASS_IS_DELETED);
        performUpdateInfo(classroom, request);
        Classroom updatedClassroom = baseClassroomService.update(classroom, ClassroomMessage.CLASS_IS_DELETED);
        log.info("Classroom information updated successfully for ID: {}", id);
        return ClassroomResponse.detail(updatedClassroom);
    }

    /**
     * Validate if tutors exist in the course.
     */
    public void checkNotExistTutorInCourse(String courseId, List<String> tutorIds, String message) {
        log.debug("Validating if tutors exist in course with ID: {}", courseId);
        baseCourseTutorService.get(
                tutorIds.stream().map(tutorId -> new CourseTutorId(courseId, tutorId)).toList(),
                List.of(IsDelete.NOT_DELETED),
                message
        );
        log.debug("Tutor validation completed successfully.");
    }

    /**
     * Update tutors in the classroom.
     */
    public ClassroomResponse updateTutors(String id, UpdateClassroomTutorRequest request) {
        log.info("Starting tutor update process for Classroom ID: {}", id);
        Classroom classroom = baseClassroomService.get(id, IsDelete.NOT_DELETED, ClassroomMessage.CLASS_IS_DELETED);

        checkNotExistTutorInCourse(id, request.getTutorIds(), ClassroomMessage.CLASS_IS_DELETED);

        List<ClassroomTutor> currentClassroomTutors = classroom.getClassroomTutors();
        Set<String> currentTutorIds = currentClassroomTutors.stream()
                .map(ClassroomTutor::getId)
                .map(ClassroomTutorId::getTutorId)
                .collect(Collectors.toSet());

        List<String> addNewTutorIds = request.getTutorIds().stream()
                .filter(tutorId -> !currentTutorIds.contains(tutorId))
                .toList();

        ZonedDateTime currentTime = ZonedDateTime.now();
        List<ClassroomTutor> newClassroomTutors = addNewTutorIds.stream()
                .map(tutorId -> new ClassroomTutor(id, tutorId, currentTime))
                .toList();

        currentClassroomTutors.addAll(newClassroomTutors);
        classroom.setClassroomTutors(currentClassroomTutors);

        Classroom updatedClassroom = baseClassroomService.update(classroom, ClassroomMessage.CLASS_IS_DELETED);
        log.info("Classroom tutors updated successfully for ID: {}", id);
        return ClassroomResponse.detail(updatedClassroom);
    }
}