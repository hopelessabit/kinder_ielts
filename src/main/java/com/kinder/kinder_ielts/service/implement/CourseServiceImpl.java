package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.add_student.AddStudentRequest;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseInfoRequest;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.entity.id.CourseStudentId;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.response_message.CourseStudentMessage;
import com.kinder.kinder_ielts.service.CourseService;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.service.base.BaseCourseTutorService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final BaseAccountService baseAccountService;
    private final BaseCourseService baseCourseService;
    private final BaseCourseTutorService baseCourseTutorService;
    private final BaseCourseStudentService baseCourseStudentService;
    private final BaseTutorService baseTutorService;
    private final BaseStudentService baseStudentService;
    private final BaseCourseLevelService baseCourseLevelService;

    /**
     * Create a new Course.
     */
    public CourseResponse createCourse(CreateCourseRequest request) {
        log.info("[CREATE COURSE] Start creating course with request: {}", request);

        Course course = ModelMapper.map(request);
        String createById = SecurityContextHolderUtil.getAccountId();

        log.debug("[CREATE COURSE] Fetching account details for creator ID: {}", createById);
        Account account = baseAccountService.get(createById, IsDelete.NOT_DELETED, CourseMessage.CREATE_FAILED);
        course.setCreateBy(account);
        log.debug("[CREATE COURSE] Account successfully assigned to course.");

        if (!request.getTutorIds().isEmpty()) {
            log.debug("[CREATE COURSE] Fetching tutors with IDs: {}", request.getTutorIds());
            List<Tutor> tutors = baseTutorService.get(request.getTutorIds(), AccountStatus.ACTIVE, CourseMessage.CREATE_FAILED);
            List<CourseTutor> courseTutorList = new ArrayList<>();
            ZonedDateTime currentTime = ZonedDateTime.now();

            tutors.forEach(tutor -> {
                courseTutorList.add(new CourseTutor(course.getId(), tutor.getId(), currentTime));
                log.debug("[CREATE COURSE] Added tutor with ID: {} to course.", tutor.getId());
            });
            course.setCourseTutors(courseTutorList);
        }

        log.debug("[CREATE COURSE] Fetching course level with ID: {}", request.getLevelId());
        CourseLevel courseLevel = baseCourseLevelService.get(request.getLevelId(), IsDelete.NOT_DELETED, CourseMessage.CREATE_FAILED);
        course.setLevel(courseLevel);
        log.info("[CREATE COURSE] Course successfully created.");

        return CourseResponse.detail(baseCourseService.create(course, "Failed to create course."));
    }

    /**
     * Fetch basic information about a Course.
     */
    public CourseResponse getInfo(String id) {
        log.info("[GET COURSE INFO] Fetching info for Course ID: {}", id);
        CourseResponse response = CourseResponse.info(baseCourseService.get(id, IsDelete.NOT_DELETED, CourseMessage.NOT_FOUND));
        log.info("[GET COURSE INFO] Successfully fetched info for Course ID: {}", id);
        return response;
    }

    /**
     * Fetch detailed information about a Course.
     */
    public CourseResponse getDetail(String id) {
        log.info("[GET COURSE DETAIL] Fetching detailed info for Course ID: {}", id);
        CourseResponse response = CourseResponse.detail(baseCourseService.get(id, IsDelete.NOT_DELETED, CourseMessage.NOT_FOUND));
        log.info("[GET COURSE DETAIL] Successfully fetched detailed info for Course ID: {}", id);
        return response;
    }

    /**
     * Fetch multiple courses.
     */
    public List<CourseResponse> getCoursesInfo(List<String> ids) {
        log.info("[GET COURSES INFO] Fetching courses with IDs: {}", ids);
        List<Course> courses = baseCourseService.get(ids, List.of(IsDelete.NOT_DELETED), CourseMessage.NOT_FOUND);
        log.info("[GET COURSES INFO] Successfully fetched {} courses.", courses.size());
        return courses.stream().map(CourseResponse::info).toList();
    }

    /**
     * Delete a Course by ID.
     */
    public void deleteCourse(String id) {
        log.info("[DELETE COURSE] Deleting Course with ID: {}", id);
        baseCourseService.delete(id, CourseMessage.DELETE_FAILED);
        log.info("[DELETE COURSE] Successfully deleted Course with ID: {}", id);
    }

    /**
     * Update fields of a Course.
     */
    public void updateCourseFields(Course course, UpdateCourseInfoRequest request) {
        log.info("[UPDATE COURSE FIELDS] Updating fields for Course ID: {}", course.getId());
        course.setDescription(CompareUtil.compare(request.getDescription(), course.getDescription()));
        course.setDetail(CompareUtil.compare(request.getDetail(), course.getDetail()));
        course.setPrice(CompareUtil.compare(request.getPrice(), course.getPrice()));
        course.setSale(CompareUtil.compare(request.getSale(), course.getSale()));

        if (request.getLevelId() != null) {
            log.debug("[UPDATE COURSE FIELDS] Updating level with ID: {}", request.getLevelId());
            course.setLevel(baseCourseLevelService.get(request.getLevelId(), IsDelete.NOT_DELETED, CourseMessage.UPDATE_INFO_FAILED));
        }
        log.info("[UPDATE COURSE FIELDS] Successfully updated fields for Course ID: {}", course.getId());
    }

    public CourseResponse updateCourseInfo(String id, UpdateCourseInfoRequest request){
        Course course = baseCourseService.get(id, IsDelete.NOT_DELETED, CourseMessage.NOT_FOUND);
        updateCourseFields(course, request);

        return CourseResponse.detailWithDetails(baseCourseService.update(course, CourseMessage.UPDATE_INFO_FAILED));
    }

    /**
     * Update course tutors.
     */
    public CourseResponse updateCourseTutor(String id, List<String> newTutorIds) {
        log.info("[UPDATE COURSE TUTORS] Updating tutors for Course ID: {}", id);

        Course course = baseCourseService.get(id, IsDelete.NOT_DELETED, CourseMessage.UPDATE_TUTORS_FAILED);
        List<CourseTutor> updatedTutors = newTutorIds.stream()
                .map(tutorId -> new CourseTutor(course.getId(), tutorId, ZonedDateTime.now()))
                .toList();
        course.setCourseTutors(updatedTutors);

        log.info("[UPDATE COURSE TUTORS] Successfully updated tutors for Course ID: {}", id);
        return CourseResponse.detail(baseCourseService.update(course, CourseMessage.UPDATE_TUTORS_SUCCESSFULLY));
    }

    public void addStudent(String courseId, AddStudentRequest request){
        ZonedDateTime now = ZonedDateTime.now();
        List<CourseStudent> courseStudents = request.getStudentIds().stream()
                .map(studentId -> new CourseStudent(courseId, studentId, now))
                .toList();
    }
}
