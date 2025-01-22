package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.Role;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseInfoRequest;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseStudent;
import com.kinder.kinder_ielts.dto.request.course.UpdateCourseTutors;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.entity.id.CourseStudentId;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import com.kinder.kinder_ielts.exception.BadRequestException;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.repository.CourseRepository;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.CourseService;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.service.base.BaseCourseTutorService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
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

    /**
     * Update course tutors.
     */
    public Integer updateCourseTutor(String id, UpdateCourseTutors request, String failMessage) {
        log.info("[UPDATE COURSE TUTORS] Start updating tutors for Course ID: {}", id);
        Account actor = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        log.info("[UPDATE COURSE TUTORS] Actor: {} | Time: {}", actor.getUsername(), currentTime);

        // Fetch course
        Course course = baseCourseService.get(id, IsDelete.NOT_DELETED, failMessage);
        log.info("[UPDATE COURSE TUTORS] Fetched course details successfully for Course ID: {}", id);

        List<CourseTutor> courseTutors = course.getCourseTutors();
        log.info("[UPDATE COURSE TUTORS] Current number of tutors: {}", courseTutors.size());

        // Perform removal of tutors
        log.info("[UPDATE COURSE TUTORS] Performing tutor removal...");
        int removeCount = performRemoveTutor(courseTutors, request.getRemove(), actor, currentTime, failMessage);
        log.info("[UPDATE COURSE TUTORS] Tutor removal completed.");

        // Perform addition of tutors
        log.info("[UPDATE COURSE TUTORS] Performing tutor addition...");
        int addCount =performAddTutor(course, courseTutors, request.getAdd(), actor, currentTime, failMessage);
        log.info("[UPDATE COURSE TUTORS] Tutor addition completed.");

        course.setCourseTutors(courseTutors);
        baseCourseService.update(course, failMessage);
        log.info("[UPDATE COURSE TUTORS] Tutors updated successfully for Course ID: {}", id);

        return removeCount + addCount;
    }

    /**
     * Add tutors to the course.
     */
    public int performAddTutor(Course course, List<CourseTutor> courseTutors, List<String> tutorIds, Account actor, ZonedDateTime currentTime, String failMessage) {
        if (tutorIds == null || tutorIds.isEmpty()) {
            log.info("[ADD TUTORS] No tutors to add.");
            return 0;
        }

        log.info("[ADD TUTORS] Requested tutor IDs to add: {}", tutorIds);

        // Identify existing tutors in the course
        Set<String> existingTutorIds = courseTutors.stream()
                .map(courseTutor -> courseTutor.getId().getTutorId())
                .filter(tutorIds::contains)
                .collect(Collectors.toSet());

        if (!existingTutorIds.isEmpty()) {
            log.info("[ADD TUTORS] Found {} tutors already existing in the course. Updating their status.", existingTutorIds.size());

            courseTutors.stream()
                    .filter(courseTutor -> existingTutorIds.contains(courseTutor.getId().getTutorId()))
                    .forEach(courseTutor -> {
                        courseTutor.setIsDeleted(IsDelete.NOT_DELETED);
                        courseTutor.setModifyTime(currentTime);
                        courseTutor.setModifyBy(actor);
                    });
        }

        // Identify new tutor IDs
        List<String> newTutorIds = tutorIds.stream()
                .filter(tutorId -> !existingTutorIds.contains(tutorId))
                .toList();

        if (newTutorIds.isEmpty()) {
            log.info("[ADD TUTORS] No new tutors to add. All requested tutors were already present.");
            return existingTutorIds.size();
        }

        log.info("[ADD TUTORS] Fetching {} new active tutors.", newTutorIds.size());
        List<Tutor> newTutors = baseTutorService.get(newTutorIds, AccountStatus.ACTIVE, failMessage);
        log.info("[ADD TUTORS] Successfully fetched {} active tutors to add.", newTutors.size());

        List<CourseTutor> newCourseTutors = newTutors.stream()
                .map(tutor -> new CourseTutor(course, tutor, actor, currentTime))
                .toList();

        courseTutors.addAll(newCourseTutors);
        log.info("[ADD TUTORS] Successfully added {} new tutors.", newCourseTutors.size());

        return existingTutorIds.size() + newCourseTutors.size();
    }


    public int performRemoveTutor(List<CourseTutor> courseTutors, List<String> tutorIds, Account actor, ZonedDateTime currentTime, String failMessage) {
        if (tutorIds == null || tutorIds.isEmpty()) {
            log.info("[REMOVE TUTORS] No tutors to remove.");
            return 0;
        }

        log.info("[REMOVE TUTORS] Requested tutor IDs to remove: {}", tutorIds);

        List<CourseTutor> removeCourseTutors = courseTutors.stream()
                .filter(courseTutor -> courseTutor.getIsDeleted().equals(IsDelete.NOT_DELETED) && tutorIds.contains(courseTutor.getId().getTutorId()))
                .toList();

        if (removeCourseTutors.isEmpty()) {
            log.info("[REMOVE TUTORS] No matching tutors found to remove.");
            throw new BadRequestException(failMessage, Error.build("Tutors are not in this course", tutorIds));
        }

        log.info("[REMOVE TUTORS] Found {} tutors to remove. Proceeding...", removeCourseTutors.size());

        courseTutors.removeAll(removeCourseTutors);

        removeCourseTutors.forEach(courseTutor -> {
            courseTutor.setIsDeleted(IsDelete.DELETED);
            courseTutor.setModifyBy(actor);
            courseTutor.setModifyTime(currentTime);
        });

        courseTutors.addAll(removeCourseTutors);
        log.info("[REMOVE TUTORS] Successfully marked {} tutors as removed.", removeCourseTutors.size());
        return removeCourseTutors.size();
    }

    /**
     * Update course students.
     */
    public Integer updateCourseStudent(String id, UpdateCourseStudent request, String failMessage) {
        log.info("[UPDATE COURSE STUDENTS] Start updating students for Course ID: {}", id);
        Account actor = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        log.info("[UPDATE COURSE STUDENTS] Actor: {} | Time: {}", actor.getUsername(), currentTime);

        Course course = baseCourseService.get(id, IsDelete.NOT_DELETED, failMessage);
        List<CourseStudent> courseStudents = course.getCourseStudents();
        log.info("[UPDATE COURSE STUDENTS] Retrieved {} current students for Course ID: {}", courseStudents.size(), id);

        // Remove Students
        log.info("[UPDATE COURSE STUDENTS] Performing student removal...");
        int removeCount = performRemoveStudent(courseStudents, request.getRemove(), actor, currentTime, failMessage);
        log.info("[UPDATE COURSE STUDENTS] Student removal completed.");

        // Add Students
        log.info("[UPDATE COURSE STUDENTS] Performing student addition...");
        int addCount = performAddStudent(course, courseStudents, request.getAdd(), actor, currentTime, failMessage);
        log.info("[UPDATE COURSE STUDENTS] Student addition completed.");

        course.setCourseStudents(courseStudents);
        Course result = baseCourseService.update(course, CourseMessage.UPDATE_STUDENTS_SUCCESSFULLY);
        log.info("[UPDATE COURSE STUDENTS] Successfully updated students for Course ID: {}", id);

        return removeCount + addCount;
    }

    /**
     * Add students to the course.
     */
    public int performAddStudent(Course course, List<CourseStudent> courseStudents, List<String> studentIds, Account actor, ZonedDateTime currentTime, String failMessage) {
        if (studentIds == null || studentIds.isEmpty()) {
            log.info("[ADD STUDENTS] No students to add.");
            return 0;
        }

        log.info("[ADD STUDENTS] Requested student IDs to add: {}", studentIds);

        // Check existing students
        List<CourseStudent> existingStudents = courseStudents.stream()
                .filter(courseStudent -> studentIds.contains(courseStudent.getId().getStudentId()))
                .toList();

        if (!existingStudents.isEmpty()) {
            log.info("[ADD STUDENTS] Found {} students already existing in the course. Updating their status.", existingStudents.size());

            existingStudents.forEach(courseStudent -> {
                courseStudent.setIsDeleted(IsDelete.NOT_DELETED);
                courseStudent.setModifyTime(currentTime);
                courseStudent.setModifyBy(actor);
            });
        }

        List<String> existingStudentIds = existingStudents.stream()
                .map(CourseStudent::getId)
                .map(CourseStudentId::getStudentId)
                .toList();

        // Determine new student IDs to add
        List<String> newStudentIds = studentIds.stream()
                .filter(studentId -> !existingStudentIds.contains(studentId))
                .toList();

        if (newStudentIds.isEmpty()) {
            log.info("[ADD STUDENTS] No new students to add. All requested students were already present.");
            return existingStudentIds.size();
        }

        log.info("[ADD STUDENTS] Fetching {} new active students.", newStudentIds.size());
        List<Student> addStudents = baseStudentService.get(newStudentIds, AccountStatus.ACTIVE, failMessage);
        log.info("[ADD STUDENTS] Successfully fetched {} active students to add.", addStudents.size());

        List<CourseStudent> newCourseStudents = addStudents.stream()
                .map(student -> new CourseStudent(course, student, actor, currentTime))
                .toList();

        courseStudents.addAll(newCourseStudents);
        log.info("[ADD STUDENTS] Added {} new students to the course.", newCourseStudents.size());

        return existingStudentIds.size() + newCourseStudents.size();
    }

    /**
     * Remove students from the course.
     */
    public int performRemoveStudent(List<CourseStudent> courseStudents, List<String> studentIds, Account actor, ZonedDateTime currentTime, String failMessage) {
        if (studentIds == null || studentIds.isEmpty()) {
            log.info("[REMOVE STUDENTS] No student IDs provided for removal.");
            return 0;
        }

        log.info("[REMOVE STUDENTS] Requested student IDs to remove: {}", studentIds);

        // Filter students that are not deleted and match the given student IDs
        Map<String, CourseStudent> existingStudentsMap = courseStudents.stream()
                .filter(courseStudent -> courseStudent.getIsDeleted().equals(IsDelete.NOT_DELETED))
                .filter(courseStudent -> studentIds.contains(courseStudent.getId().getStudentId()))
                .collect(Collectors.toMap(
                        courseStudent -> courseStudent.getId().getStudentId(),
                        courseStudent -> courseStudent
                ));
        if (existingStudentsMap.isEmpty()) {
            log.error("[REMOVE STUDENTS] No matching students found in the course.");
            throw new BadRequestException(failMessage, Error.build("Students are not in this course", studentIds));
        }

        if (existingStudentsMap.size() != studentIds.size()) {
            // Identify student IDs that do not exist in the course
            List<String> notFoundStudentIds = studentIds.stream()
                    .filter(id -> !existingStudentsMap.containsKey(id))
                    .toList();
            log.error("[REMOVE STUDENTS] Some student IDs do not exist in the course: {}", notFoundStudentIds);
            throw new BadRequestException(failMessage, Error.build("Students are not in this course", notFoundStudentIds));
        }

        log.info("[REMOVE STUDENTS] Found {} students to remove. Proceeding...", existingStudentsMap.size());

        // Perform removal operations
        existingStudentsMap.values().forEach(courseStudent -> {
            courseStudent.setIsDeleted(IsDelete.DELETED);
            courseStudent.setModifyBy(actor);
            courseStudent.setModifyTime(currentTime);
        });

        // Remove students from the list
        courseStudents.removeIf(courseStudent -> studentIds.contains(courseStudent.getId().getStudentId()));

        // Add updated students back
        courseStudents.addAll(existingStudentsMap.values());

        log.info("[REMOVE STUDENTS] Successfully marked {} students as removed.", existingStudentsMap.size());

        return existingStudentsMap.size();
    }

    public List<CourseResponse> getAll (IsDelete isDelete, String failMessage){
        return baseCourseService.get(isDelete, failMessage).stream().map(CourseResponse::infoWithDetail).toList();
    }

    public Page<CourseResponse> get(String courseName, Float minPrice, Float maxPrice, String levelId, String tutorId, String studentId, IsDelete isDelete, Pageable pageable) {
        Role role = SecurityContextHolderUtil.getRole();
        if (isDelete != null && isDelete.isDeleted() && !role.equals(Role.ADMIN))
            throw new BadRequestException(CourseMessage.NOT_ALLOWED, Error.build("Không có quyền tìm kiếm khóa học đã xóa"));
        else
            isDelete = IsDelete.NOT_DELETED;
        // Create the Specification with the custom sorting logic
        Specification<Course> spec = createCourseSpecification(courseName, minPrice, maxPrice, levelId, tutorId, studentId, pageable);

        // Create a new Pageable with unsorted to avoid conflicts with custom sorting logic
        Pageable unsortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());

        // Execute the query with the Specification and the unmodified Pageable (with unsorted)
        Page<Course> coursePage = courseRepository.findAll(spec, unsortedPageable);

        if (role.equals(Role.ADMIN))
            return coursePage.map(CourseResponse::detailWithDetails);
        else
            return coursePage.map(CourseResponse::infoWithDetail);
    }

    private Specification<Course> createCourseSpecification(String courseName, Float minPrice, Float maxPrice, String levelId, String tutorId, String studentId, Pageable pageable) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add dynamic predicates
            addCourseNamePredicate(courseName, root, cb, predicates);
            addPriceRangePredicate(minPrice, maxPrice, root, cb, predicates);
            addLevelPredicate(levelId, root, cb, predicates);
            addTutorPredicate(tutorId, root, cb, predicates, query);
            addStudentPredicate(studentId, root, cb, predicates, query);
            addIsDeletePredicate(IsDelete.NOT_DELETED, root, cb, predicates);

            // Handle sorting logic
            handleSorting(pageable, root, cb, query);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addIsDeletePredicate(IsDelete isDelete, Root<Course> root, CriteriaBuilder cb, List<Predicate> predicates) {
        predicates.add(cb.equal(root.get("isDeleted"), isDelete));
    }

    private void addTutorPredicate(String tutorId, Root<Course> root, CriteriaBuilder cb, List<Predicate> predicates, CriteriaQuery query) {
        if (tutorId != null && !tutorId.isEmpty()) {
            query.distinct(true);
            predicates.add(cb.equal(root.join("courseTutors").get("tutor").get("id"), tutorId));
            predicates.add(cb.equal(root.join("courseTutors").get("isDeleted"), IsDelete.NOT_DELETED));
        }
    }

    private void addStudentPredicate(String studentId, Root<Course> root, CriteriaBuilder cb, List<Predicate> predicates, CriteriaQuery query) {
        if (studentId != null && !studentId.isEmpty()) {
            query.distinct(true);
            predicates.add(cb.equal(root.join("courseStudents").get("student").get("id"), studentId));
            predicates.add(cb.equal(root.join("courseStudents").get("isDeleted"), IsDelete.NOT_DELETED));
        }
    }

    private void addCourseNamePredicate(String courseName, Root<Course> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (courseName != null && !courseName.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%" + courseName.toLowerCase() + "%"));
        }
    }

    private void addPriceRangePredicate(Float minPrice, Float maxPrice, Root<Course> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(cb.coalesce(root.get("sale"), root.get("price")), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(cb.coalesce(root.get("sale"), root.get("price")), maxPrice));
        }
    }

    private void addLevelPredicate(String levelId, Root<Course> root, CriteriaBuilder cb, List<Predicate> predicates) {
        if (levelId != null && !levelId.isEmpty()) {
            predicates.add(cb.equal(root.get("level").get("id"), levelId));
        }
    }

    private void handleSorting(Pageable pageable, Root<Course> root, CriteriaBuilder cb, CriteriaQuery<?> query) {
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : sort) {
                if ("price".equals(order.getProperty())) {
                    // Use COALESCE to sort by final price (sale or price)
                    Order orderByPrice = order.isAscending() ? cb.asc(cb.coalesce(root.get("sale"), root.get("price"))) : cb.desc(cb.coalesce(root.get("sale"), root.get("price")));
                    orders.add(orderByPrice);
                } else {
                    // Sort by other properties normally
                    Order orderByOtherProperty = order.isAscending() ? cb.asc(root.get(order.getProperty())) : cb.desc(root.get(order.getProperty()));
                    orders.add(orderByOtherProperty);
                }
            }
            query.orderBy(orders);
        }
    }

}
