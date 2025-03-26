package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.GradeStatus;
import com.kinder.kinder_ielts.constant.HomeWorkSubmitStatus;
import com.kinder.kinder_ielts.constant.HomeworkSubmitAllowLateStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.dto.request.student_homework.GradingStudentHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.student_homework.UpdateStudentHomeworkRequest;
import com.kinder.kinder_ielts.dto.response.student_homework.StudentHomeworkResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Homework;
import com.kinder.kinder_ielts.entity.id.StudentHomeworkId;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import com.kinder.kinder_ielts.exception.BadRequestException;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.response_message.StudentHomeworkMessage;
import com.kinder.kinder_ielts.service.StudentHomeworkService;
import com.kinder.kinder_ielts.service.base.BaseHomeworkService;
import com.kinder.kinder_ielts.service.base.BaseStudentHomeworkService;
import com.kinder.kinder_ielts.service.base.BaseStudentService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentHomeworkServiceImpl implements StudentHomeworkService {
    private final BaseStudentHomeworkService baseStudentHomeworkService;
    private final BaseHomeworkService baseHomeworkService;
    private final BaseStudentService baseStudentService;

    public Page<StudentHomeworkResponse> getStudentHomeworks(String homeworkId, Pageable pageable, String failMessage) {
        Homework homework = baseHomeworkService.get(homeworkId, IsDelete.NOT_DELETED, failMessage);
        return baseStudentHomeworkService.getByHomeworkId(homeworkId, pageable, IsDelete.NOT_DELETED, failMessage).map(StudentHomeworkResponse::detail);
    }

    public StudentHomeworkResponse getStudentHomework(String homeworkId, String studentId, boolean includeForAdmin, boolean includeHomeworkDetail, String failMessage) {
        StudentHomework studentHomework = baseStudentHomeworkService.get(StudentHomeworkId.of(studentId, homeworkId), IsDelete.NOT_DELETED, failMessage);
        if (studentHomework == null) {
            throw new NotFoundException(failMessage, Error.build(StudentHomeworkMessage.NOT_FOUND, Map.of("studentId", studentId, "homeworkId", homeworkId)));
        }
        return new StudentHomeworkResponse(studentHomework, includeForAdmin, includeHomeworkDetail);
    }

    public StudentHomeworkResponse studentSubmit(String homeworkId, UpdateStudentHomeworkRequest request, String failMessage){
        ZonedDateTime currentTime = ZonedDateTime.now();
        Account studentAccount = SecurityContextHolderUtil.getAccount();
        StudentHomework studentHomework = baseStudentHomeworkService.get(StudentHomeworkId.of(studentAccount.getId(), homeworkId), IsDelete.NOT_DELETED, failMessage);

        if (studentHomework.getHomework().getDueDate().compareTo(currentTime) < 0  && studentHomework.getHomework().getSubmitAllowLateStatus().equals(HomeworkSubmitAllowLateStatus.NOT_ALLOW)) {
            throw new BadRequestException(failMessage, Error.build(StudentHomeworkMessage.HOMEWORK_IS_OVERDUE));
        } else {
            studentHomework.setSubmitStatus(HomeWorkSubmitStatus.SUBMITTED);
        }

        if (UpdateStudentHomeworkRequest.isValid(request)) {
            throw new BadRequestException(failMessage, Error.build(StudentHomeworkMessage.INVALID_REQUEST));
        }

        if (studentHomework.getSubmitStatus().equals(HomeWorkSubmitStatus.SUBMITTED))
            updateSubmit(studentHomework, request, studentAccount, currentTime);
        else
            submitNew(studentHomework, request, studentAccount, currentTime);
        return StudentHomeworkResponse.detail(baseStudentHomeworkService.create(studentHomework, failMessage));
    }

    protected void submitNew(StudentHomework studentHomework, UpdateStudentHomeworkRequest request, Account studentAccount, ZonedDateTime currentTime){
        studentHomework.setSubmitFile(request.getSubmitFile());
        studentHomework.setSubmitText(request.getSubmitText());
        studentHomework.setSubmissionDate(currentTime);
        studentHomework.updateAudit(studentAccount, ZonedDateTime.now());
    }

    protected void updateSubmit(StudentHomework studentHomework, UpdateStudentHomeworkRequest request, Account studentAccount, ZonedDateTime currentTime){
        studentHomework.setSubmitFile(CompareUtil.compare(studentHomework.getSubmitFile(), request.getSubmitFile()));
        studentHomework.setSubmitText(CompareUtil.compare(studentHomework.getSubmitText(), request.getSubmitText()));
        studentHomework.setSubmissionDate(currentTime);
        studentHomework.updateAudit(studentAccount, ZonedDateTime.now());
    }

    public StudentHomeworkResponse gradingSubmit(String homeworkId, GradingStudentHomeworkRequest request, String failMessage){
        if (!GradingStudentHomeworkRequest.isValid(request)) {
            throw new BadRequestException(failMessage, Error.build(StudentHomeworkMessage.INVALID_REQUEST));
        }

        Account modifier = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();

        StudentHomework studentHomework = baseStudentHomeworkService.get(StudentHomeworkId.of(request.getStudentId(), homeworkId), IsDelete.NOT_DELETED, failMessage);
        if (studentHomework.getSubmitStatus().equals(HomeWorkSubmitStatus.NOT_SUBMITTED)) {
            throw new BadRequestException(failMessage, Error.build(StudentHomeworkMessage.STUDENT_NOT_SUBMITTED));
        }
        if (studentHomework.getGradeStatus().equals(GradeStatus.GRADED))
            updateGrading(studentHomework, request, modifier, currentTime);
        else
            gradingNew(studentHomework, request, modifier, currentTime);
        return StudentHomeworkResponse.detailWithDetails(baseStudentHomeworkService.create(studentHomework, failMessage));
    }

    protected void gradingNew(StudentHomework studentHomework, GradingStudentHomeworkRequest request, Account modifier, ZonedDateTime currentTime){
        studentHomework.setGradingDate(currentTime);
        studentHomework.setGradeStatus(GradeStatus.GRADED);
        studentHomework.setGrader(modifier);
        studentHomework.setGraderId(modifier.getId());
        studentHomework.setScore(request.getScore());
        studentHomework.setGradeComment(request.getGradeComment());
        studentHomework.updateAudit(modifier, currentTime);
    }

    protected void updateGrading(StudentHomework studentHomework, GradingStudentHomeworkRequest request, Account modifier, ZonedDateTime currentTime){
        studentHomework.setGradingDate(currentTime);
        studentHomework.setGradeStatus(GradeStatus.GRADED);
        studentHomework.setGrader(modifier);
        studentHomework.setGraderId(modifier.getId());
        studentHomework.setScore(CompareUtil.compare(studentHomework.getScore(), request.getScore()));
        studentHomework.setGradeComment(CompareUtil.compare(studentHomework.getGradeComment(), request.getGradeComment()));
        studentHomework.updateAudit(modifier, currentTime);
    }

    public int updateStudentHomeworkStatus(String homeworkId, String failMessage){
        Homework homework = baseHomeworkService.get(homeworkId, IsDelete.NOT_DELETED, failMessage);
        List<StudentHomework> studentHomeworks = baseStudentHomeworkService.getByHomeworkId(homeworkId, IsDelete.NOT_DELETED, failMessage);
        if (studentHomeworks.isEmpty())
            return 0;
        ZonedDateTime currentTime = ZonedDateTime.now();
        if (!homework.getSubmitAllowLateStatus().equals(HomeworkSubmitAllowLateStatus.NOT_ALLOW) && homework.getDueDate().compareTo(currentTime) >= 0) {
            return 0;
        }

        int count = 0;

        for (StudentHomework studentHomework : studentHomeworks) {
            if (studentHomework.getSubmitStatus().equals(HomeWorkSubmitStatus.NOT_SUBMITTED)) {
                studentHomework.setSubmitStatus(HomeWorkSubmitStatus.OVER_DUE);
                studentHomework.updateAudit(SecurityContextHolderUtil.getAccount(), currentTime);
                count++;
            }
        }
        return count;
    }
}
