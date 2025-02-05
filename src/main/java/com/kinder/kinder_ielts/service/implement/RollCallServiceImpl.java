package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.RollCallStatus;
import com.kinder.kinder_ielts.dto.response.roll_call.RollCallResponse;
import com.kinder.kinder_ielts.entity.RollCall;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.entity.id.RollCallId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.service.base.BaseRollCallService;
import com.kinder.kinder_ielts.service.base.BaseStudentService;
import com.kinder.kinder_ielts.service.base.BaseStudyScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RollCallServiceImpl {
    private final BaseRollCallService baseRollCallService;
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseStudentService baseStudentService;


    public List<RollCallResponse> getByStudyScheduleId(String studyScheduleId, Boolean includeForAdmin, String failMessage) {
        List<RollCall> rollCalls = baseRollCallService.findByStudyScheduleId(studyScheduleId, IsDelete.NOT_DELETED, failMessage);
        if (rollCalls.isEmpty()) {
            StudySchedule studySchedule = baseStudyScheduleService.get(studyScheduleId, IsDelete.NOT_DELETED, failMessage);
            List<Student> students = studySchedule.getClassroom().getClassroomStudents().stream().map(ClassroomStudent::getStudent).toList();
            for (Student student : students) {
                rollCalls.add(new RollCall(student, studySchedule));
            }
        }
        return rollCalls.stream().map(rollCall -> new RollCallResponse(rollCall, includeForAdmin, false)).toList();
    }

    public List<RollCallResponse> getByStudentIdAndClassId(String studentId, String classId, Boolean includeForAdmin, String failMessage) {
        List<StudySchedule> studySchedules = baseStudyScheduleService.findByClassId(classId, IsDelete.NOT_DELETED, failMessage);
        if (studySchedules.isEmpty()) {
            return List.of();
        }

        List<RollCall> rollCalls = baseRollCallService.findByStudentIdAndClassId(studentId, classId, IsDelete.NOT_DELETED, failMessage);

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
}
