package com.kinder.kinder_ielts.dto.response.student_homework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.homework.HomeworkResponse;
import com.kinder.kinder_ielts.dto.response.student.StudentResponse;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;

import java.time.ZonedDateTime;

public class StudentHomeworkResponse {
    private StudentResponse student;
    private HomeworkResponse homework;
    private ZonedDateTime submissionDate;
    private ZonedDateTime gradingDate;
    private Integer score;
    private String submitFile;
    private HomeWorkSubmitStatusResponse submitStatus;
    private GradeStatusResponse gradeStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse createBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime modifyTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse modifyBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IsDeletedResponse isDeleted;

    public StudentHomeworkResponse(StudentHomework studentHomework, boolean includeInfoForAdmin, boolean includeDetails) {
        this.student = StudentResponse.info(studentHomework.getStudent());
        this.submissionDate = studentHomework.getSubmissionDate();
        this.gradingDate = studentHomework.getGradingDate();
        this.score = studentHomework.getScore();
        this.submitFile = studentHomework.getSubmitFile();
        this.submitStatus = HomeWorkSubmitStatusResponse.from(studentHomework.getSubmitStatus());
        this.gradeStatus = GradeStatusResponse.from(studentHomework.getGradeStatus());

        mapSubInfo(studentHomework, includeInfoForAdmin);
        mapDetail(studentHomework, includeDetails);
    }

    public void mapSubInfo(StudentHomework studentHomework , boolean includeInfoForAdmin) {
        if (includeInfoForAdmin) {
            this.createTime = studentHomework.getCreateTime();

            this.modifyTime = studentHomework.getModifyTime();

            this.createBy = SubAccountResponse.from(studentHomework.getCreateBy());

            this.modifyBy = SubAccountResponse.from(studentHomework.getModifyBy());

            this.isDeleted = IsDeletedResponse.from(studentHomework.getIsDeleted());
        }
    }

    public void mapDetail(StudentHomework studentHomework, boolean includeDetails) {
        if (includeDetails) {
            this.homework = HomeworkResponse.info(studentHomework.getHomework());
        }
    }

    public static StudentHomeworkResponse info(StudentHomework studentHomework){
        return new StudentHomeworkResponse(studentHomework, false, false);
    }

    public static StudentHomeworkResponse detail(StudentHomework studentHomework){
        return new StudentHomeworkResponse(studentHomework, true, false);
    }

    public static StudentHomeworkResponse infoWithDetails(StudentHomework studentHomework) {
        return new StudentHomeworkResponse(studentHomework, false, true);
    }

    public static StudentHomeworkResponse detailWithDetails(StudentHomework studentHomework) {
        return new StudentHomeworkResponse(studentHomework, true, true);
    }
}
