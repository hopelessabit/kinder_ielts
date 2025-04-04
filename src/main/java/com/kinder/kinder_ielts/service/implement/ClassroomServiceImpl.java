package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.*;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomStudentRequest;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomTutorRequest;
import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.entity.id.CourseTutorId;
import com.kinder.kinder_ielts.entity.join_entity.*;
import com.kinder.kinder_ielts.exception.BadRequestException;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.exception.UnAuthorizeException;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.ClassroomService;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.service.base.BaseCourseTutorService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import com.kinder.kinder_ielts.util.Time;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
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
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseClassroomStudentService baseClassroomStudentService;
    private final BaseStudentService baseStudentService;


    /**
     * Create a new Classroom.
     */
    @Transactional
    public ClassroomResponse createClassroom(String courseId, CreateClassroomRequest request, String message) {
        log.info("Starting process to create a new Classroom.");
        ZonedDateTime currentTime = ZonedDateTime.now();
        Classroom classroom = ModelMapper.map(request, currentTime);
        String createById = SecurityContextHolderUtil.getAccountId();
        log.debug("Creator Account ID fetched from security context: {}", createById);

        log.debug("Fetching account for creator ID: {}", createById);
        Account account = baseAccountService.get(createById, IsDelete.NOT_DELETED, message);
        classroom.setCreateBy(account);
        log.debug("Account fetched successfully and assigned to classroom creator.");

        log.debug("Fetching course with ID: {}", courseId);
        Course belongToCourse = baseCourseService.get(courseId, IsDelete.NOT_DELETED, message);
        classroom.setCourse(belongToCourse);
        log.debug("Course fetched successfully and assigned to the classroom.");

        baseClassroomService.create(classroom, message);
        log.info("Classroom creation completed successfully.");

        if (request.getTutorIds() != null && !request.getTutorIds().isEmpty()) {
            log.debug("Fetching tutors with IDs: {}", request.getTutorIds());
            List<Tutor> tutors = baseTutorService.get(request.getTutorIds(), AccountStatus.ACTIVE, message);

            List<ClassroomTutor> classroomTutors = new ArrayList<>();
            tutors.forEach(tutor -> {
                classroomTutors.add(new ClassroomTutor(classroom, tutor, account, currentTime));
                log.debug("Added tutor with ID: {} to classroom tutors list.", tutor.getId());
            });

            log.debug("Add [Classroom Tutor]");
            baseClassroomTutorService.create(classroomTutors, message);

            classroom.setClassroomTutors(classroomTutors);
        }

        List<ClassroomWeeklySchedule> classroomWeeklySchedules = new ArrayList<>();
        addWeeklySchedule(classroom, request.getSchedules(), classroomWeeklySchedules, account, currentTime);
        classroom.setWeeklySchedule(classroomWeeklySchedules);

        Set<StudySchedule> studySchedules = generateAllStudySchedule(request.getStartDate(),
                request.getSchedules(),
                request.getSlots() == null ? belongToCourse.getSlots() : request.getSlots(),
                request.getFromTime(),
                request.getToTime(),
                account,
                currentTime,
                classroom);

        if (request.getTemplateClassroomId() == null) {
            classroom.setStudySchedules(studySchedules);

            return ClassroomResponse.detailWithDetails(baseClassroomService.update(classroom, message));
        }
        TemplateClassroom templateClassroom = belongToCourse.getTemplates().stream().filter(tc -> tc.getId().equals(request.getTemplateClassroomId())).findAny().orElseThrow(() -> new NotFoundException(message, Error.build("", List.of(request.getTemplateClassroomId()))));
        List<TemplateStudySchedule> templateStudySchedules = templateClassroom.getStudySchedules().stream().filter(templateStudySchedule -> !templateStudySchedule.getIsDeleted().isDeleted()).sorted(Comparator.comparing(TemplateStudySchedule::getPlace)).toList();
        addStudyScheduleHaveTemplate(studySchedules, templateStudySchedules, account, currentTime);
        classroom.setStudySchedules(studySchedules);

        return ClassroomResponse.detailWithDetails(baseClassroomService.update(classroom, message));
    }

    private void addWeeklySchedule(Classroom classroom, List<DateOfWeek> schedules, List<ClassroomWeeklySchedule> classroomWeeklySchedules, Account account, ZonedDateTime currentTime) {
        for (DateOfWeek schedule : schedules) {
            classroomWeeklySchedules.add(new ClassroomWeeklySchedule(classroom, schedule, account, currentTime));
        }
    }

    protected Set<StudySchedule> generateAllStudySchedule(ZonedDateTime startDate,
                                                           List<DateOfWeek> schedules,
                                                           int slots,
                                                           Time fromTime,
                                                           Time toTime,
                                                           Account account,
                                                           ZonedDateTime currentTime,
                                                           Classroom classroom) {
        ZonedDateTime currentDate = startDate;
        List<ZonedDateTime> zonedDateTimes = new ArrayList<>();
        List<DayOfWeek> mappedSchedules = schedules.stream().map(schedule -> DayOfWeek.of(schedule.getValue())).toList();

        while (zonedDateTimes.size() < slots) {
            DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
            if (mappedSchedules.contains(currentDayOfWeek)) {
                zonedDateTimes.add(currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }

        classroom.setEndDate(zonedDateTimes.get(zonedDateTimes.size() - 1));

        Set<StudySchedule> studySchedules = new HashSet<>();

        int place = 0;
        for (ZonedDateTime zonedDateTime : zonedDateTimes) {
            place++;
            // Adjust fromDateTime using OffsetTime fromTime
            ZonedDateTime fromDateTime = zonedDateTime
                    .withHour(fromTime.getTime().getHour())
                    .withMinute(fromTime.getTime().getMinute())
                    .withSecond(fromTime.getTime().getSecond())
                    .withNano(fromTime.getTime().getNano());

            // Adjust toDateTime using OffsetTime toTime
            ZonedDateTime toDateTime = zonedDateTime
                    .withHour(toTime.getTime().getHour())
                    .withMinute(toTime.getTime().getMinute())
                    .withSecond(toTime.getTime().getSecond())
                    .withNano(toTime.getTime().getNano());

            studySchedules.add(new StudySchedule(fromDateTime, toDateTime, "Schedule", account, currentTime , classroom, place));
        }
        return studySchedules;
    }

    public void addStudyScheduleHaveTemplate(Set<StudySchedule> studySchedules,
                                             List<TemplateStudySchedule> templateStudySchedules,
                                             Account account,
                                             ZonedDateTime currentTime) {
        Set<StudySchedule> updatedSchedules = new HashSet<>();

        for (TemplateStudySchedule templateStudySchedule : templateStudySchedules) {
            StudySchedule studySchedule = studySchedules.stream()
                    .filter(s -> s.getPlace().equals(templateStudySchedule.getPlace()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("",
                            Error.build("", List.of(templateStudySchedule.getPlace()))));

            studySchedule.setTitle(templateStudySchedule.getTitle());
            studySchedule.setDescription(templateStudySchedule.getDescription());

            List<ClassroomLink> classroomLinks = templateStudySchedule.getClassroomLinks().stream()
                    .map(c -> ClassroomLink.from(c, studySchedule, account, currentTime))
                    .toList();
            List<WarmUpTest> warmUpTests = templateStudySchedule.getWarmUpTests().stream()
                    .map(w -> WarmUpTest.from(w, studySchedule, account, currentTime))
                    .toList();
            List<StudyMaterial> studyMaterials = templateStudySchedule.getStudyMaterials().stream()
                    .map(sm -> StudyMaterial.from(sm, studySchedule, account, currentTime))
                    .toList();
            List<Homework> homeworks = templateStudySchedule.getHomework().stream()
                    .map(hw -> Homework.from(hw, studySchedule, account, currentTime))
                    .toList();

            studySchedule.setClassroomLinks(classroomLinks);
            studySchedule.setWarmUpTests(warmUpTests);
            studySchedule.setStudyMaterials(studyMaterials);
            studySchedule.setHomework(homeworks);
            studySchedule.setStatus(templateStudySchedule.getStatus());

            // Remove the old schedule and add the updated one
            updatedSchedules.remove(studySchedule);
            updatedSchedules.add(studySchedule);
        }

        // Replace the original set with the updated set
        for (StudySchedule studySchedule: studySchedules){
            if (!updatedSchedules.contains(studySchedule))
                updatedSchedules.add(studySchedule);
        }

        studySchedules.clear();
        studySchedules.addAll(updatedSchedules);
    }


    /**
     * Get basic information about a classroom.
     */
    public ClassroomResponse getInfo(String id) {
        log.info("Fetching basic info for Classroom ID: {}", id);
        Classroom classroom = baseClassroomService.get(id, IsDelete.NOT_DELETED, ClassroomMessage.NOT_FOUND);
        classroom.setStudySchedules(baseStudyScheduleService.findByClassIdWithViewStatus(id, IsDelete.NOT_DELETED, ViewStatus.VIEW));
        classroom.setClassroomTutors(baseClassroomTutorService.getByClassroomId(id, IsDelete.NOT_DELETED));
        ClassroomResponse response = ClassroomResponse.infoWithDetails(classroom);
        log.info("Successfully fetched classroom info for ID: {}", id);
        return response;
    }

    /**
     * Get detailed information about a classroom.
     */
    public ClassroomResponse getDetail(String id, IsDelete isDelete, List<ViewStatus> studyScheduleStatus) {
        log.info("Fetching detailed info for Classroom ID: {}", id);
        Classroom classroom = baseClassroomService.get(id, IsDelete.NOT_DELETED, ClassroomMessage.NOT_FOUND);
        classroom.setStudySchedules(baseStudyScheduleService.findByClassId(id, isDelete, studyScheduleStatus));
        classroom.setClassroomTutors(baseClassroomTutorService.getByClassroomId(id, IsDelete.NOT_DELETED));
        ClassroomResponse response = ClassroomResponse.detailWithDetails(
                classroom
        );

        log.info("Successfully fetched detailed classroom info for ID: {}", id);
        return response;
    }

    /**
     * Delete a classroom by ID.
     */
    public Void deleteCourse(String id) {
        log.info("Starting deletion process for Classroom with ID: {}", id);
        baseClassroomService.delete(id, CourseMessage.DELETE_FAILED);
        log.info("Classroom successfully deleted with ID: {}", id);
        return null;
    }

    /**
     * Perform partial updates on classroom details.
     */
    public void performUpdateInfo(Classroom classroom, UpdateClassroomRequest request) {
        log.debug("Performing updates on Classroom fields.");
        classroom.setDescription(CompareUtil.compare(request.getDescription(), classroom.getDescription()));
        classroom.setCode(CompareUtil.compare(request.getCode(), classroom.getCode()));
        classroom.setFromTime(CompareUtil.compare(request.getFromTime(), classroom.getFromTime()));
        classroom.setToTime(CompareUtil.compare(request.getToTime(), classroom.getToTime()));
        classroom.setStartDate(CompareUtil.compare(request.getStartDate(), classroom.getStartDate()));

        List<ClassroomWeeklySchedule> classroomWeeklySchedules = classroom.getWeeklySchedule();
        if (request.getSchedules() != null) {
            classroomWeeklySchedules.clear();
            addWeeklySchedule(classroom, request.getSchedules(), classroomWeeklySchedules, SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
        }
        log.debug("Classroom fields updated successfully.");
    }

    /**
     * Update classroom information.
     */
    public ClassroomResponse updateInfo(String id, UpdateClassroomRequest request, String failMessage) {
        log.info("Updating Classroom information for ID: {}", id);
        Classroom classroom = baseClassroomService.get(id, IsDelete.NOT_DELETED, failMessage);
        performUpdateInfo(classroom, request);
        Classroom updatedClassroom = baseClassroomService.update(classroom, failMessage);
        log.info("Classroom information updated successfully for ID: {}", id);
        return ClassroomResponse.detail(updatedClassroom);
    }

    @Override
    public Page<ClassroomResponse> get(String search,
                                       String courseId,
                                       String tutorId,
                                       String studentId,
                                       IsDelete isDelete,
                                       Boolean includeDetail,
                                       Boolean includeCourse,
                                       Boolean includeTutor,
                                       Pageable pageable) {
        Role role = null;

        try {
            role = SecurityContextHolderUtil.getRole();
        } catch (Exception e) {
            log.info("Không phân quyền. Mặc định là USER");
        }

        if (role != null && !role.equals(Role.ADMIN) && isDelete != null && isDelete.isDeleted()){
            throw new BadRequestException(ClassroomMessage.NOT_ALLOWED, Error.build("Không có quyền tìm kiếm lớp học đã xóa"));
        }
        else
            isDelete = IsDelete.NOT_DELETED;

        Specification<Classroom> classroomSpecification = createClassroomSpecification(search, courseId, tutorId, studentId, isDelete);

        Page<Classroom> classrooms = baseClassroomService.findAll(classroomSpecification, pageable);

        if (role != null && (role.equals(Role.ADMIN) || role.equals(Role.TUTOR)))
            return classrooms.map(classroom -> new ClassroomResponse(classroom, true, includeCourse, includeTutor, includeDetail));
        return classrooms.map(classroom -> new ClassroomResponse(classroom, false, includeCourse, includeTutor, includeDetail));
    }

    private Specification<Classroom> createClassroomSpecification(String search, String courseId, String tutorId, String studentId, IsDelete isDelete) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            addClassroomCodeAndDescriptionPredicate(search, cb, root, predicates);
            addCourseIdPredicate(courseId, cb, root, predicates);
            addTutorIdPredicate(tutorId, cb, root, predicates, query);
            addStudentIdPredicate(studentId, cb, root, predicates, query);
            addIsDeletePredicate(isDelete, cb, root, predicates);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addIsDeletePredicate(IsDelete isDelete, CriteriaBuilder cb, Root<Classroom> root, List<Predicate> predicates) {
        predicates.add(cb.equal(root.get("isDeleted"), isDelete));
    }

    private void addTutorIdPredicate(String tutorId, CriteriaBuilder cb, Root<Classroom> root, List<Predicate> predicates, CriteriaQuery<?> query) {
        if (tutorId != null && !tutorId.isEmpty()) {
            query.distinct(true);
            predicates.add(cb.equal(root.join("classroomTutors").get("tutor").get("id"), tutorId));
        }
    }

    private void addStudentIdPredicate(String studentId, CriteriaBuilder cb, Root<Classroom> root, List<Predicate> predicates, CriteriaQuery<?> query) {
        if (studentId != null && !studentId.isEmpty()) {
            query.distinct(true);
            predicates.add(cb.equal(root.join("classroomStudents").get("student").get("id"), studentId));
        }
    }

    private void addCourseIdPredicate(String courseId, CriteriaBuilder cb, Root<Classroom> root, List<Predicate> predicates) {
        if (courseId != null) {
            predicates.add(cb.equal(root.get("course").get("id"), courseId));
        }
    }

    private void addClassroomCodeAndDescriptionPredicate(String search, CriteriaBuilder cb, Root<Classroom> root, List<Predicate> predicates) {
        if (search != null)
            predicates.add(
                    cb.or(cb.like(cb.lower(root.get("code")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + search.toLowerCase() + "%")
            ));
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
     * Update classroom tutors.
     */
    public Integer updateClassroomTutor(String id, UpdateClassroomTutorRequest request, String failMessage) {
        log.info("[UPDATE CLASSROOM TUTORS] Start updating tutors for Classroom ID: {}", id);

        Account actor = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        log.info("[UPDATE CLASSROOM TUTORS] Actor: {} | Time: {}", actor.getUsername(), currentTime);

        // Fetch Classroom and Course details
        Classroom classroom = baseClassroomService.get(id, IsDelete.NOT_DELETED, failMessage);
        Course course = classroom.getCourse();
        List<ClassroomTutor> classroomTutors = classroom.getClassroomTutors();
        List<CourseTutor> courseTutors = course.getCourseTutors();

        log.info("[UPDATE CLASSROOM TUTORS] Current Classroom Tutors: {}", classroomTutors.size());
        log.info("[UPDATE CLASSROOM TUTORS] Current Course Tutors: {}", courseTutors.size());

        // Perform removal of tutors
        log.info("[UPDATE CLASSROOM TUTORS] Performing tutor removal...");
        int removeCount = performRemoveTutor(classroomTutors, request.getRemove(), failMessage);
        log.info("[UPDATE CLASSROOM TUTORS] Tutor removal completed. Removed: {}", removeCount);

        // Perform addition of tutors
        log.info("[UPDATE CLASSROOM TUTORS] Performing tutor addition...");
        int addCount = performAddTutor(classroom, courseTutors, classroomTutors, request.getAdd(), actor, currentTime, failMessage);
        log.info("[UPDATE CLASSROOM TUTORS] Tutor addition completed. Added: {}", addCount);

        log.info("[UPDATE CLASSROOM TUTORS] Updating course tutor list...");
        course.setCourseTutors(courseTutors);
        baseCourseService.update(course, failMessage);

        baseCourseService.update(course, CourseMessage.UPDATE_TUTORS_SUCCESSFULLY);
        log.info("[UPDATE CLASSROOM TUTORS] Successfully updated tutors for Classroom ID: {}", id);

        return addCount + removeCount;
    }

    /**
     * Add tutors to the classroom.
     */
    protected int performAddTutor(Classroom classroom, List<CourseTutor> courseTutors, List<ClassroomTutor> classroomTutors,
                                  List<String> tutorIds, Account actor, ZonedDateTime currentTime, String failMessage) {
        if (tutorIds == null || tutorIds.isEmpty()) {
            log.info("[ADD CLASSROOM TUTORS] No tutors to add.");
            return 0;
        }

        log.info("[ADD CLASSROOM TUTORS] Requested tutor IDs to add: {}", tutorIds);

        // Validate Tutor Availability in Course
        Set<String> courseTutorIds = courseTutors.stream()
                .map(courseTutor -> courseTutor.getId().getTutorId())
                .collect(Collectors.toSet());

        List<String> unavailableTutorIds = tutorIds.stream()
                .filter(tutorId -> !courseTutorIds.contains(tutorId))
                .toList();

        if (!unavailableTutorIds.isEmpty()) {
            log.error("[ADD CLASSROOM TUTORS] Some tutors are not available in the course: {}", unavailableTutorIds);
            throw new BadRequestException(failMessage, Error.build(
                    "Some tutors are not available in this course. Tutor IDs: " + unavailableTutorIds, unavailableTutorIds));
        }

        // Validate Deleted Tutors
        List<String> deletedTutorIds = courseTutors.stream()
                .filter(courseTutor -> tutorIds.contains(courseTutor.getId().getTutorId()))
                .filter(courseTutor -> courseTutor.getIsDeleted().equals(IsDelete.DELETED))
                .map(courseTutor -> courseTutor.getId().getTutorId())
                .toList();

        if (!deletedTutorIds.isEmpty()) {
            log.error("[ADD CLASSROOM TUTORS] Some tutors are marked as deleted: {}", deletedTutorIds);
            throw new BadRequestException(failMessage, Error.build(
                    "Some tutors are marked as deleted. Tutor IDs: " + deletedTutorIds, deletedTutorIds));
        }

        // Separate already existing and new tutors in the classroom
        Set<String> existingClassroomTutorIds = classroomTutors.stream()
                .map(classroomTutor -> classroomTutor.getId().getTutorId())
                .collect(Collectors.toSet());

        List<String> newTutorIds = tutorIds.stream()
                .filter(tutorId -> !existingClassroomTutorIds.contains(tutorId))
                .toList();

        // Add New Tutors
        List<ClassroomTutor> newClassroomTutors = newTutorIds.stream()
                .map(tutorId -> new ClassroomTutor(classroom,
                                courseTutors.stream()
                                        .filter(courseTutor -> courseTutor.getId().getTutorId().equals(tutorId))
                                        .findFirst()
                                        .orElseThrow(() -> new BadRequestException(failMessage, Error.build("Tutor not found: " + tutorId)))
                                .getTutor(),
                        actor, currentTime))
            .toList();

        classroomTutors.addAll(newClassroomTutors);
        log.info("[ADD CLASSROOM TUTORS] Successfully added {} new tutors.", newClassroomTutors.size());

        return newClassroomTutors.size();
    }

    /**
     * Remove tutors from the classroom.
     */
    protected int performRemoveTutor(List<ClassroomTutor> classroomTutors, List<String> tutorIds, String failMessage) {
        if (tutorIds == null || tutorIds.isEmpty()) {
            log.info("[REMOVE CLASSROOM TUTORS] No tutors to remove.");
            return 0;
        }

        log.info("[REMOVE CLASSROOM TUTORS] Requested tutor IDs to remove: {}", tutorIds);

        List<ClassroomTutor> removeClassroomTutors = classroomTutors.stream()
                .filter(classroomTutor -> tutorIds.contains(classroomTutor.getId().getTutorId()))
                .toList();

        if (removeClassroomTutors.isEmpty()) {
            log.warn("[REMOVE CLASSROOM TUTORS] No matching tutors found to remove.");
            return 0;
        }

        baseClassroomTutorService.remove(removeClassroomTutors.stream()
                .map(ClassroomTutor::getId)
                .toList(), failMessage);

        classroomTutors.removeAll(removeClassroomTutors);
        log.info("[REMOVE CLASSROOM TUTORS] Successfully removed {} tutors.", removeClassroomTutors.size());

        return removeClassroomTutors.size();
    }

    public Integer updateClassroomStudent(String id, UpdateClassroomStudentRequest request, String failMessage) {
        log.info("[UPDATE CLASSROOM STUDENTS] Start updating students for Classroom ID: {}", id);

        Account actor = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        log.info("[UPDATE CLASSROOM STUDENTS] Actor: {} | Time: {}", actor.getUsername(), currentTime);

        // Fetch Classroom and Course details
        Classroom classroom = baseClassroomService.get(id, IsDelete.NOT_DELETED, failMessage);
        Course course = classroom.getCourse();
        List<ClassroomStudent> classroomStudents = classroom.getClassroomStudents();
        List<CourseStudent> courseStudents = course.getCourseStudents();

        log.info("[UPDATE CLASSROOM STUDENTS] Current Classroom Students: {}", classroomStudents.size());
        log.info("[UPDATE CLASSROOM STUDENTS] Current Course Students: {}", courseStudents.size());

        // Perform removal of students
        log.info("[UPDATE CLASSROOM STUDENTS] Performing student removal...");
        int removeCount = performRemoveStudent(classroomStudents, request.getRemove(),actor, currentTime, failMessage);
        log.info("[UPDATE CLASSROOM STUDENTS] Student removal completed. Removed: {}", removeCount);

        // Perform addition of students
        log.info("[UPDATE CLASSROOM STUDENTS] Performing student addition...");
        int addCount = performAddStudent(classroom, courseStudents, classroomStudents, request.getAdd(), actor, currentTime, failMessage);
        log.info("[UPDATE CLASSROOM STUDENTS] Student addition completed. Added: {}", addCount);

        log.info("[UPDATE CLASSROOM STUDENTS] Successfully updated students for Classroom ID: {}", id);

        return addCount + removeCount;
    }

    /**
     * Add students to the classroom.
     */
    protected int performAddStudent(Classroom classroom, List<CourseStudent> courseStudents, List<ClassroomStudent> classroomStudents,
                                    List<String> studentIds, Account actor, ZonedDateTime currentTime, String failMessage) {
        int result = 0;
        if (studentIds == null || studentIds.isEmpty()) {
            log.info("[ADD CLASSROOM STUDENTS] No students to add.");
            return 0;
        }

        log.info("[ADD CLASSROOM STUDENTS] Requested student IDs to add: {}", studentIds);

        List<CourseStudent> isExistedCourseStudent = courseStudents.stream()
                .filter(courseStudent -> studentIds.contains(courseStudent.getId().getStudentId()))
                .toList();

        // Validate Student Availability in Course
        Set<String> courseStudentIds = isExistedCourseStudent.stream()
                .filter(courseStudent -> courseStudent.getIsDeleted().equals(IsDelete.NOT_DELETED))
                .map(courseStudent -> courseStudent.getId().getStudentId())
                .collect(Collectors.toSet());

        List<String> unavailableStudentIds = studentIds.stream()
                .filter(studentId -> !courseStudentIds.contains(studentId))
                .toList();

        if (!unavailableStudentIds.isEmpty()) {
            log.error("[ADD CLASSROOM STUDENTS] Some students are not available in the course: {}", unavailableStudentIds);
            throw new BadRequestException(failMessage, Error.build(
                    "Some of the students are not in course: ",
                    unavailableStudentIds));
        }

        List<ClassroomStudent> existedClassroomStudents = classroomStudents.stream()
                .filter(classroomStudent -> studentIds.contains(classroomStudent.getId().getStudentId()))
                .toList();

        if (!existedClassroomStudents.isEmpty()){
            List<ClassroomStudent> isDeletedClassroomStudents = existedClassroomStudents.stream()
                    .filter(classroomStudent -> classroomStudent.getIsDeleted().equals(IsDelete.DELETED))
                    .toList();

            isDeletedClassroomStudents.forEach(classroomStudent -> {
                classroomStudent.setIsDeleted(IsDelete.NOT_DELETED);
                classroomStudent.updateAudit(actor, currentTime);
            });

            result += baseClassroomStudentService.update(isDeletedClassroomStudents, failMessage).size();
        }

        // Separate already existing and new students in the classroom
        Set<String> existingClassroomStudentIds = existedClassroomStudents.stream()
                .map(classroomStudent -> classroomStudent.getId().getStudentId())
                .collect(Collectors.toSet());

        List<String> newStudentIds = studentIds.stream()
                .filter(studentId -> !existingClassroomStudentIds.contains(studentId))
                .toList();
        if (!newStudentIds.isEmpty()) {
            List<Student> students = baseStudentService.get(newStudentIds, AccountStatus.ACTIVE, failMessage);

            List<ClassroomStudent> newClassroomStudents =  baseClassroomStudentService.create(students, classroom, actor, currentTime, failMessage);

            result += newClassroomStudents.size();
            log.info("[ADD CLASSROOM STUDENTS] Successfully added {} new students.", newClassroomStudents.size());

        }
        return result;
    }

    /**
     * Remove students from the classroom.
     */
    protected int performRemoveStudent(List<ClassroomStudent> classroomStudents, List<String> studentIds,
                                       Account actor, ZonedDateTime currentTime, String failMessage) {
        if (studentIds == null || studentIds.isEmpty()) {
            log.info("[REMOVE CLASSROOM STUDENTS] No students to remove.");
            return 0;
        }

        log.info("[REMOVE CLASSROOM STUDENTS] Requested student IDs to remove: {}", studentIds);

        List<ClassroomStudent> removeClassroomStudents = classroomStudents.stream()
                .filter(classroomStudent -> studentIds.contains(classroomStudent.getId().getStudentId()))
                .toList();

        if (removeClassroomStudents.isEmpty()) {
            log.warn("[REMOVE CLASSROOM STUDENTS] No matching students found to remove.");
            return 0;
        } else if (removeClassroomStudents.size() != studentIds.size()) {
            List<String> notFoundStudentIds = studentIds.stream()
                    .filter(studentId -> removeClassroomStudents.stream()
                            .noneMatch(classroomStudent -> classroomStudent.getId().getStudentId().equals(studentId)))
                    .toList();

            log.error("[REMOVE CLASSROOM STUDENTS] Some students are not found: {}", notFoundStudentIds);
            throw new BadRequestException(failMessage, Error.build(
                    "Some students are not found in the classroom: ",
                    notFoundStudentIds));
        }

        removeClassroomStudents.forEach(classroomStudent -> {
            classroomStudent.setIsDeleted(IsDelete.DELETED);
            classroomStudent.updateAudit(actor, currentTime);
        });

        baseClassroomStudentService.update(removeClassroomStudents, failMessage);

        classroomStudents.removeAll(removeClassroomStudents);
        log.info("[REMOVE CLASSROOM STUDENTS] Successfully removed {} students.", removeClassroomStudents.size());

        return removeClassroomStudents.size();
    }

    public ClassroomResponse test(){
//        return ClassroomResponse.detailWithDetails(baseClassroomService.getByIdWithStudentId("8RdF-205013", "4", "Not found"));
        return ClassroomResponse.detailWithDetails(baseClassroomService.getByIdWithStudentId("Fuz3-124601", "0", "Not found"));
    }

    public ClassroomResponse getInfoForStudent(String classroomId, String studentId, String failMessage) {
        Account actor = SecurityContextHolderUtil.getAccount();
        if (actor.getRole().equals(Role.STUDENT) && !studentId.equals(actor.getId()))
            throw UnAuthorizeException.unAuthorize();

        Classroom classroom = baseClassroomService.get(classroomId, IsDelete.NOT_DELETED, failMessage);
        List<StudySchedule> studySchedules = baseStudyScheduleService.findByClassIdWithViewStatusForStudent(classroomId, studentId, IsDelete.NOT_DELETED, ViewStatus.VIEW);

        return null;
    }
}