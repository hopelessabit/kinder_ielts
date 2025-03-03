package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.*;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.dto.request.roll_call.CreateRollCallsRequest;
import com.kinder.kinder_ielts.dto.request.roll_call.SearchRollCallRequest;
import com.kinder.kinder_ielts.dto.request.roll_call.RollCallRequest;
import com.kinder.kinder_ielts.dto.request.roll_call.UpdateRollCallsRequest;
import com.kinder.kinder_ielts.dto.response.roll_call.RollCallResponse;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.entity.id.RollCallId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.exception.BadRequestException;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.RollCallMessage;
import com.kinder.kinder_ielts.response_message.StudentMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.*;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RollCallServiceImpl {
    private final BaseClassroomStudentService baseClassroomStudentService;
    private final BaseClassroomService baseClassroomService;
    private final BaseRollCallService baseRollCallService;
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseStudentService baseStudentService;

    public Page<RollCallResponse> test(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        ZonedDateTime now = ZonedDateTime.now();
        Specification<RollCall> specification = getSpec("zggL-124713", now);
        return baseRollCallService.get(specification, pageRequest).map(RollCallResponse::detailWithExtendDetail);
    }

    public Specification<RollCall> getSpec(String classId, ZonedDateTime now){
        return (root, query, criteriaBuilder) -> {


            // Create a join to Class through StudySchedule and then a join to Classroom through StudySchedule
            Join<RollCall, StudySchedule> studyScheduleJoin = root.join("studySchedule", JoinType.RIGHT);
            Join<StudySchedule, Classroom> classroomJoin = studyScheduleJoin.join("classroom");
            // Create a join to the student
            Join<RollCall, Student> studentJoin = root.join("student");

            //Specify the class id condition
            Predicate classPredicate = criteriaBuilder.equal(classroomJoin.get("id"), classId);
            //Specify the future schedule condition
            Predicate futureSchedulesPredicate = criteriaBuilder.greaterThanOrEqualTo(studyScheduleJoin.get("fromTime"), now);
            // Specify that deleted records will be ignored
            Predicate isNotDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), 0);

            // Return query that applies all predicate requirements
            return criteriaBuilder.and(classPredicate, futureSchedulesPredicate, isNotDeletedPredicate);
        };
    }

    public Page<RollCallResponse> search(Pageable pageable, SearchRollCallRequest request) {
        Role role = null;

        try {
            role = SecurityContextHolderUtil.getRole();
        } catch (Exception e) {
            log.info("Không phân quyền. Mặc định là USER");
        }

        if (role != null && !role.equals(Role.ADMIN) && request.getIsDelete() != null && request.getIsDelete().isDeleted()){
            throw new BadRequestException(ClassroomMessage.NOT_ALLOWED, Error.build("Không có quyền tìm kiếm lớp học đã xóa"));
        } else
            request.setIsDelete(IsDelete.NOT_DELETED);

        Specification<RollCall> rollCallSpecification = createRollCallSpecification(request.getSearchStudent(), request.getSearchModifier(), request.getSearchClassroom(), request.getStudyScheduleId(), request.getStatuses(), request.getIsDelete());

        Page<RollCall> rollCalls = baseRollCallService.get(rollCallSpecification, pageable);

        if (role != null && (role.equals(Role.ADMIN) || role.equals(Role.TUTOR))) {
            return rollCalls.map(rollCall -> new RollCallResponse(rollCall, request.getIncludeForAdmin(), request.getIncludeStudySchedule()));
        } else {
            return rollCalls.map(rollCall -> new RollCallResponse(rollCall, false, false));
        }
    }

    private Specification<RollCall> createRollCallSpecification(String searchStudent, String searchModifier, String searchClassroom, String studyScheduleId, List<RollCallStatus> statuses, IsDelete isDelete) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add search conditions dynamically
            if (searchClassroom != null && !searchClassroom.isEmpty())
                predicates.add(searchClassroomPredicate(searchClassroom, root, criteriaBuilder));

            if (searchStudent != null && !searchStudent.isEmpty())
                predicates.add(searchStudentPredicate(searchStudent, root, criteriaBuilder));

            if (searchModifier != null && !searchModifier.isEmpty()) {
                Join<RollCall, Account> modifierJoin = root.join("modifyBy", JoinType.LEFT);
                predicates.add(searchModifierPredicate(searchModifier, modifierJoin, criteriaBuilder));
            }

            if (studyScheduleId != null && !studyScheduleId.isEmpty())
                predicates.add(criteriaBuilder.equal(root.get("id").get("studyScheduleId"), studyScheduleId));

            if (statuses != null && !statuses.isEmpty())
                predicates.add(root.get("status").in(statuses));

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDelete));

            assert query != null;
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private Predicate searchClassroomPredicate(String searchClassroom, Root<RollCall> root, CriteriaBuilder criteriaBuilder) {
        Join<RollCall, StudySchedule> studyScheduleJoin = root.join("studySchedule", JoinType.INNER);
        Join<StudySchedule, Classroom> classroomJoin = studyScheduleJoin.join("classroom", JoinType.INNER);
        String pattern = "%" + searchClassroom.toLowerCase() + "%";

        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(classroomJoin.get("code")), pattern),
                criteriaBuilder.like(criteriaBuilder.lower(classroomJoin.get("id")), pattern)
        );
    }

    private Predicate searchStudentPredicate(String search, Root<RollCall> root, CriteriaBuilder cb) {
        Join<RollCall, Student> studentJoin = root.join("student", JoinType.INNER);
        String pattern = "%" + search.toLowerCase() + "%";

        return cb.or(
                cb.like(cb.lower(studentJoin.get("firstName")), pattern),
                cb.like(cb.lower(studentJoin.get("middleName")), pattern),
                cb.like(cb.lower(studentJoin.get("lastName")), pattern),
                cb.like(cb.lower(studentJoin.get("email")), pattern)
        );
    }

    private Predicate searchModifierPredicate(String search, Join<RollCall, Account> modifierJoin, CriteriaBuilder cb) {
        String pattern = "%" + search.toLowerCase() + "%";

        return cb.or(
                cb.like(cb.lower(modifierJoin.get("id")), pattern),
                cb.like(cb.lower(modifierJoin.get("username")), pattern)
        );
    }

    public RollCallResponse updateRollCall(String studyScheduleId, RollCallRequest request, String failMessage){
        RollCall rollCall = baseRollCallService.get(new RollCallId(request.studentId, studyScheduleId), IsDelete.NOT_DELETED, failMessage);

        rollCall.setNote(CompareUtil.compare(request.note.trim(), rollCall.getNote()));
        rollCall.setStatus(CompareUtil.compare(request.status, rollCall.getStatus()));
        rollCall.updateAudit();

        baseRollCallService.update(rollCall, failMessage);

        return RollCallResponse.info(rollCall);
    }

    public List<RollCallResponse> updateRollCalls(String studyScheduleId, UpdateRollCallsRequest request, String failMessage){
        List<RollCall> rollCalls = baseRollCallService.findByStudyScheduleId(studyScheduleId, IsDelete.NOT_DELETED, failMessage);
        Account modifier = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        List<RollCall> updatedRollCalls = new ArrayList<>();
        if (request.rollCalls == null || request.rollCalls.isEmpty())
            throw new BadRequestException(failMessage, Error.build(RollCallMessage.REQUEST_IS_EMPTY));
        for (RollCall rollCall : rollCalls){
            RollCallRequest rollCallRequest = request.rollCalls.stream()
                    .filter(rq -> rq.studentId.equals(rollCall.getId().getStudentId()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(failMessage, Error.build(StudyScheduleMessage.ROLL_CALL_NOT_EXIST, List.of(rollCall.getId().getStudentId()))));

            rollCall.setNote(CompareUtil.compare(rollCallRequest.note.trim(), rollCall.getNote()));
            rollCall.setStatus(CompareUtil.compare(rollCallRequest.status, rollCall.getStatus()));
            rollCall.updateAudit(modifier, currentTime);
            updatedRollCalls.add(rollCall);
        }
        List<RollCall> result = baseRollCallService.update(updatedRollCalls, failMessage);
        return result.stream().map(RollCallResponse::info).toList();
    }

    public List<RollCallResponse> getAllByStudyScheduleId(String studyScheduleId,
                                                          Boolean includeForAdmin,
                                                          IsDelete isDelete,
                                                          Boolean includeStudyScheduleInfo,
                                                          String failMessage) {
        Role role = null;

        try {
            role = SecurityContextHolderUtil.getRole();
        } catch (Exception e) {
            log.info("Không phân quyền. Mặc định là USER");
        }

        if (role != null && !role.equals(Role.ADMIN) && isDelete != null && isDelete.isDeleted())
            throw new BadRequestException(ClassroomMessage.NOT_ALLOWED, Error.build("Không có quyền tìm kiếm lớp học đã xóa"));
        else
            isDelete = IsDelete.NOT_DELETED;

        List<RollCall> rollCalls = baseRollCallService.findByStudyScheduleId(studyScheduleId, isDelete, failMessage);

        if (rollCalls.isEmpty()) {
            StudySchedule studySchedule = baseStudyScheduleService.get(studyScheduleId, IsDelete.NOT_DELETED, failMessage);
            List<Student> students = baseStudentService.getByClassId(studyScheduleId);
            if (students.isEmpty())
                return List.of();
            for (Student student : students) {
                if (student.getIsDeleted().equals(IsDelete.DELETED))
                    continue;
                rollCalls.add(new RollCall(student, studySchedule));
            }
        }

        if (role != null && (role.equals(Role.ADMIN) || role.equals(Role.TUTOR)))
            return rollCalls.stream().map(rollCall -> new RollCallResponse(rollCall, includeForAdmin, includeStudyScheduleInfo)).toList();
        else
            return rollCalls.stream().map(rollCall -> new RollCallResponse(rollCall, false, includeStudyScheduleInfo)).toList();
    }

    public List<RollCallResponse> getByStudentIdAndClassId(String studentId, String classId, Boolean includeForAdmin, String failMessage) {
        List<StudySchedule> studySchedules = baseStudyScheduleService.findByClassIdWithViewStatus(classId, IsDelete.NOT_DELETED, ViewStatus.VIEW).stream().toList();
        if (studySchedules.isEmpty()) {
            throw new NotFoundException(failMessage, Error.build(StudyScheduleMessage.NOT_EXIST, List.of(classId)));
        }

        List<RollCall> rollCalls = baseRollCallService.findByStudentIdAndStudyScheduleIds(studentId, studySchedules.stream().map(StudySchedule::getId).toList(), IsDelete.NOT_DELETED, failMessage);

        if (rollCalls.isEmpty()) {
            Student student = baseStudentService.get(studentId, IsDelete.NOT_DELETED, failMessage);
            for (StudySchedule studySchedule : studySchedules) {
                rollCalls.add(new RollCall(student, studySchedule));
            }
        } else if (rollCalls.size() <= studySchedules.size()){
            for (int i = rollCalls.size(); i < studySchedules.size(); i++){
                rollCalls.add(new RollCall(rollCalls.get(0).getStudent(), studySchedules.get(i)));
            }
        }

        return rollCalls.stream().map(rollCall -> new RollCallResponse(rollCall, includeForAdmin, false)).toList();
    }

    public List<RollCallResponse> getAllByClassId(String classId, Boolean includeForAdmin, String failMessage) {
        Classroom classroom = baseClassroomService.get(classId, IsDelete.NOT_DELETED, failMessage);
        Set<StudySchedule> studySchedules = baseStudyScheduleService.findByClassId(classId, IsDelete.NOT_DELETED, null);
        List<ClassroomStudent> classroomStudents = baseClassroomStudentService.findByClassroomId(classId, IsDelete.NOT_DELETED);
        List<RollCall> rollCalls = baseRollCallService.findByClassId(classId, IsDelete.NOT_DELETED, failMessage);

        if (rollCalls.size() == (studySchedules.size() * classroomStudents.size()))
            return rollCalls.stream()
                    .map(rollCall -> new RollCallResponse(rollCall, false, false))
                    .sorted(Comparator.comparing((RollCallResponse rc) -> rc.getStudySchedule().getPlace())
                            .thenComparing(rc -> rc.getStudent().getFirstName()))
                    .toList();

        if (rollCalls.isEmpty()) {
            for (StudySchedule studySchedule : studySchedules) {
                for (ClassroomStudent classroomStudent : classroomStudents) {
                    rollCalls.add(new RollCall(classroomStudent.getStudent(), studySchedule));
                }
            }

            return rollCalls.stream()
                    .sorted(Comparator.comparing((RollCall rc) -> rc.getStudySchedule().getPlace())
                            .thenComparing(rc -> rc.getStudent().getFirstName()))
                    .map(rollCall -> new RollCallResponse(rollCall, includeForAdmin, false))
                    .toList();
        }
        List<StudySchedule> futureStudySchedules = studySchedules.stream()
                .filter(studySchedule -> studySchedule.getFromTime().isAfter(rollCalls.get(rollCalls.size()-1).getStudySchedule().getFromTime()))
                .toList();

        for (StudySchedule studySchedule : futureStudySchedules){
            rollCalls.stream()
                    .filter(rollCall -> rollCall.getId().getStudyScheduleId().equals(studySchedule.getId()))
                    .forEach(rollCalls::add);
        }

        return rollCalls.stream()
                .sorted(Comparator.comparing((RollCall rc) -> rc.getStudySchedule().getPlace())
                        .thenComparing(rc -> rc.getStudent().getFirstName()))
                .map(rollCall -> new RollCallResponse(rollCall, includeForAdmin, false))
                .toList();
    }

    public List<RollCallResponse> createRollCalls(String studyScheduleId, CreateRollCallsRequest request, String failMessage) {
        StudySchedule studySchedule = baseStudyScheduleService.get(studyScheduleId, IsDelete.NOT_DELETED, failMessage);
        List<RollCall> rollCalls = new ArrayList<>();
        Account creator = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        List<Student> students = baseStudentService.getByClassId(studySchedule.getClassroom().getId());

        if (students.isEmpty())
            return List.of();

        if (request.getRollCalls() == null || request.getRollCalls().isEmpty())
            throw new BadRequestException(failMessage, Error.build(RollCallMessage.REQUEST_IS_EMPTY));

        if (request.getRollCalls().size() != students.size()){
            List<String> missingStudentIds = new ArrayList<>();
            for (Student student: students){
                if (request.getRollCalls().stream().noneMatch(rq -> rq.studentId.equals(student.getId())))
                    missingStudentIds.add(student.getId());
            }
            throw new BadRequestException(failMessage, Error.build(RollCallMessage.STUDENT_IS_MISSING_IN_REQUEST, missingStudentIds));
        }

        for (Student student: students){
            RollCallRequest detailRequest = request.getRollCalls().stream()
                    .filter(rq -> rq.studentId.equals(student.getId()))
                    .findFirst().get();
            rollCalls.add(ModelMapper.map(detailRequest, student, studySchedule, creator, currentTime));
        }

        List<RollCall> result = baseRollCallService.create(rollCalls, failMessage);
        return result.stream().map(RollCallResponse::info).toList();
    }
}
