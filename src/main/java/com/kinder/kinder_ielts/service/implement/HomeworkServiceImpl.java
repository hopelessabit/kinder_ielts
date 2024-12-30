package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.GradeStatus;
import com.kinder.kinder_ielts.constant.HomeWorkSubmitStatus;
import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.homework.CreateHomeworkRequest;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Homework;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.entity.id.StudentHomeworkId;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.HomeworkMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseHomeworkService;
import com.kinder.kinder_ielts.service.base.BaseStudentHomeworkService;
import com.kinder.kinder_ielts.service.base.BaseStudyScheduleService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeworkServiceImpl implements HomeworkService{
    private final BaseAccountService baseAccountService;
    private final BaseHomeworkService baseHomeworkService;
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseStudentHomeworkService baseStudentHomeworkService;

    public HomeworkResponse createHomework(String studyScheduleId, CreateHomeworkRequest request, String message){
        Homework homework = ModelMapper.map(request);

        String createById = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for creator ID: {}", createById);
        Account account = baseAccountService.get(createById, IsDelete.NOT_DELETED, message);
        homework.setCreateBy(account);

        StudySchedule studySchedule = baseStudyScheduleService.get(studyScheduleId, IsDelete.NOT_DELETED, message);
        homework.setBeLongTo(studySchedule);

        if (request.getStudentIds() != null && !request.getStudentIds().isEmpty()){
            List<StudentHomework> studentHomeworks = studySchedule
                    .getBelongToClassroom()
                    .getClassroomStudents()
                    .stream()
                    .filter(classroomStudent -> classroomStudent.getIsDeleted().equals(IsDelete.NOT_DELETED) && request.getStudentIds().contains(classroomStudent.getId().getStudentId()))
                    .map(ClassroomStudent::getId)
                    .map(ClassStudentId::getStudentId)
                    .map(studentId -> StudentHomework.builder()
                            .id(new StudentHomeworkId(studentId, homework.getId()))
                            .gradeStatus(GradeStatus.NOT_SUBMITTED)
                            .submitStatus(HomeWorkSubmitStatus.NOT_SUBMITTED)
                            .build())
                    .toList();
            if (studentHomeworks.isEmpty())
                homework.setPrivacyStatus(HomeworkPrivacyStatus.PUBLIC);
            else {
                homework.setPrivacyStatus(HomeworkPrivacyStatus.PRIVATE);
                baseStudentHomeworkService.create(studentHomeworks, message);
                homework.setStudentHomeworks(studentHomeworks);
            }
        } else {
            homework.setPrivacyStatus(HomeworkPrivacyStatus.PUBLIC);
        }

        return HomeworkResponse.detail(baseHomeworkService.create(homework, message));
    }

    public HomeworkResponse getInfo(String id) {
        return HomeworkResponse.infoWithDetails(baseHomeworkService.get(id, IsDelete.NOT_DELETED, HomeworkMessage.NOT_FOUND));
    }
}
