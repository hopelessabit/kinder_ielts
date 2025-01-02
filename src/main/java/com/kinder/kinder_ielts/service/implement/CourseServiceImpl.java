package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.add_student.AddStudentRequest;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseInfoRequest;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseStudent;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseTutors;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.entity.id.CourseTutorId;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.CourseService;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.service.base.BaseCourseTutorService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        log.info("[CREATE COURSE] Start creating course with request: {}", request);

        Course course = ModelMapper.map(request);
        String createById = SecurityContextHolderUtil.getAccountId();

        log.debug("[CREATE COURSE] Fetching account details for creator ID: {}", createById);
        Account account = baseAccountService.get(createById, IsDelete.NOT_DELETED, CourseMessage.CREATE_FAILED);
        course.setCreateBy(account);
        log.debug("[CREATE COURSE] Account successfully assigned to course.");

        baseCourseService.create(course, CourseMessage.CREATE_FAILED);
        if (!request.getTutorIds().isEmpty()) {
            log.debug("[CREATE COURSE] Fetching tutors with IDs: {}", request.getTutorIds());
            List<Tutor> tutors = baseTutorService.get(request.getTutorIds(), AccountStatus.ACTIVE, CourseMessage.CREATE_FAILED);
            List<CourseTutor> courseTutorList = new ArrayList<>();
            ZonedDateTime currentTime = ZonedDateTime.now();

            tutors.forEach(tutor -> {
                courseTutorList.add(new CourseTutor(course, tutor, account, currentTime));
                log.debug("[CREATE COURSE] Added tutor with ID: {} to course.", tutor.getId());
            });
            baseCourseTutorService.create(courseTutorList, CourseMessage.CREATE_FAILED);
            course.setCourseTutors(courseTutorList);
        }

        log.debug("[CREATE COURSE] Fetching course level with ID: {}", request.getLevelId());
        CourseLevel courseLevel = baseCourseLevelService.get(request.getLevelId(), IsDelete.NOT_DELETED, CourseMessage.CREATE_FAILED);
        course.setLevel(courseLevel);
        log.info("[CREATE COURSE] Course successfully created.");

        return CourseResponse.detail(baseCourseService.update(course, CourseMessage.CREATE_FAILED));
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
        CourseResponse response = CourseResponse.detailWithDetails(baseCourseService.get(id, IsDelete.NOT_DELETED, CourseMessage.NOT_FOUND));
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

    /**
     * Update course tutors.
     */
    public CourseResponse updateCourseTutor(String id, UpdateCourseTutors request, String failMessage) {
        log.info("[UPDATE COURSE TUTORS] Updating tutors for Course ID: {}", id);
        Account actor = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();

        Course course = baseCourseService.get(id, IsDelete.NOT_DELETED, failMessage);
        List<CourseTutor> courseTutors = course.getCourseTutors();

        performRemoveTutor(courseTutors, request.getRemove(), actor, currentTime, failMessage);

        performAddTutor(course, courseTutors, request.getAdd(), actor, currentTime, failMessage);

        course.setCourseTutors(courseTutors);
        log.info("[UPDATE COURSE TUTORS] Successfully updated tutors for Course ID: {}", id);
        return CourseResponse.detail(baseCourseService.update(course, CourseMessage.UPDATE_TUTORS_SUCCESSFULLY));
    }

    public void performAddTutor(Course course, List<CourseTutor> courseTutors, List<String> tutorIds, Account actor, ZonedDateTime currentTime, String failMessage) {
        if (tutorIds == null || tutorIds.isEmpty()) {
            return;
        }
        List<Tutor> addTutors = baseTutorService.get(tutorIds, AccountStatus.ACTIVE, failMessage);


        List<CourseTutor> existed = courseTutors.stream()
                .filter(courseTutor -> tutorIds.contains(courseTutor.getId().getTutorId()))
                .toList();
        courseTutors.removeAll(existed);
        List<String> exitedTutorIds = existed.stream()
                .map(CourseTutor::getId)
                .map(CourseTutorId::getTutorId)
                .toList();
        if (!exitedTutorIds.isEmpty()) {
            existed.forEach(courseTutor -> {
                courseTutor.setIsDeleted(IsDelete.NOT_DELETED);
                courseTutor.setModifyTime(currentTime);
                courseTutor.setModifyBy(actor);
            });
            courseTutors.addAll(existed);
        }


        List<CourseTutor> addCourseTutor = new ArrayList<>(tutorIds.stream()
                .filter(tutorId -> !exitedTutorIds.contains(tutorId))
                .map(tutorId -> new CourseTutor(course, addTutors.stream().filter(tutor -> tutor.getId().equals(tutorId)).findFirst().get(), actor, currentTime))
                .toList());
        addCourseTutor.addAll(existed);
        baseCourseTutorService.create(addCourseTutor, failMessage);
        courseTutors.addAll(addCourseTutor);
    }

    public void performRemoveTutor(List<CourseTutor> courseTutors, List<String> tutorIds, Account actor, ZonedDateTime currentTime, String failMessage) {
        if (tutorIds == null || tutorIds.isEmpty()) {
            return;
        }
        List<CourseTutor> removeCourseTutors = courseTutors.stream()
                .filter(courseTutor -> courseTutor.getIsDeleted().equals(IsDelete.NOT_DELETED) && tutorIds.contains(courseTutor.getId().getTutorId()))
                .toList();

        if (removeCourseTutors.isEmpty()) {
            return;
        }

        baseCourseTutorService.removeEntities(removeCourseTutors, failMessage);
        courseTutors.removeAll(removeCourseTutors);
        courseTutors.removeAll(removeCourseTutors);
        removeCourseTutors.forEach(courseTutor -> {
            courseTutor.setIsDeleted(IsDelete.DELETED);
            courseTutor.setModifyBy(actor);
            courseTutor.setModifyTime(currentTime);
        });
        courseTutors.addAll(removeCourseTutors);
    }

    /**
     * Update course students.
     */
    public CourseResponse updateCourseStudent(String id, UpdateCourseStudent request, String failMessage) {
        log.info("[UPDATE COURSE STUDENTS] Updating students for Course ID: {}", id);

        Course course = baseCourseService.get(id, IsDelete.NOT_DELETED, failMessage);
        List<CourseStudent> courseStudents = course.getCourseStudents();

        performRemoveStudent(courseStudents, request.getRemove(), failMessage);

        performAddStudent(course, courseStudents, request.getAdd(), failMessage);

        course.setCourseStudents(courseStudents);
        log.info("[UPDATE COURSE STUDENTS] Successfully updated students for Course ID: {}", id);
        return CourseResponse.detail(baseCourseService.update(course, CourseMessage.UPDATE_TUTORS_SUCCESSFULLY));
    }

    public void performAddStudent(Course course, List<CourseStudent> courseStudents, List<String> studentIds, String failMessage) {
        if (studentIds == null || studentIds.isEmpty()) {
            return;
        }
        ZonedDateTime currentTime = ZonedDateTime.now();
        List<Student> addStudents = baseStudentService.get(studentIds, AccountStatus.ACTIVE, failMessage);
        List<CourseStudent> addCourseStudent = studentIds.stream()
                .map(tutorId -> new CourseStudent(course, addStudents.stream().filter(tutor -> tutor.getId().equals(tutorId)).findFirst().get(), currentTime))
                .toList();
        baseCourseStudentService.create(addCourseStudent, failMessage);
        courseStudents.addAll(addCourseStudent);
    }

    public void performRemoveStudent(List<CourseStudent> courseStudents, List<String> studentIds, String failMessage) {
        if (studentIds == null || studentIds.isEmpty()) {
            return;
        }
        List<CourseStudent> removeCourseStudents = courseStudents.stream().filter(courseStudent -> studentIds.contains(courseStudent.getId().getStudentId())).toList();
        baseCourseStudentService.remove(removeCourseStudents.stream().map(CourseStudent::getId).toList(), failMessage);
        courseStudents.removeAll(removeCourseStudents);
    }
}
