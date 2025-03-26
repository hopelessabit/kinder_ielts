package com.kinder.kinder_ielts.entity.join_entity;


import com.kinder.kinder_ielts.constant.GradeStatus;
import com.kinder.kinder_ielts.constant.HomeWorkSubmitStatus;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Homework;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.id.StudentHomeworkId;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "student_homework")
public class StudentHomework extends BaseEntity {

    @EmbeddedId
    private StudentHomeworkId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "fk_student_homework_student"))
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("homeworkId")
    @JoinColumn(name = "homework_id", foreignKey = @ForeignKey(name = "fk_student_homework_homework"))
    private Homework homework;

    @Column(name = "submission_date")
    private ZonedDateTime submissionDate;

    @Column(name = "grading_date")
    private ZonedDateTime gradingDate;

    //Score by percentage. Ex: 50%
    @Column(name = "score")
    private Integer score;

    @Column(name = "grade_comment", columnDefinition = "varchar(1000)")
    private String gradeComment;

    @Column(name = "submit_file", columnDefinition = "varchar(MAX)")
    private String submitFile;

    @Column(name = "submit_text", columnDefinition = "varchar(MAX)")
    private String submitText;

    @Enumerated(EnumType.STRING)
    @Column(name = "submit_status", length = 13)
    private HomeWorkSubmitStatus submitStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_status", length = 14)
    private GradeStatus gradeStatus;

    @Column(name = "grader_id", length = 255, insertable = false, updatable = false)
    private String graderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grader_id")
    private Account grader;

    public void initStatus() {
        this.submitStatus = HomeWorkSubmitStatus.NOT_SUBMITTED;
        this.gradeStatus = GradeStatus.NOT_SUBMITTED;
    }

    public StudentHomework(String studentId, Homework homework, Account actor, ZonedDateTime currentTime) {
        this.id = new StudentHomeworkId(studentId, homework.getId());
        this.initForNew(actor, currentTime);
        this.initStatus();
    }
}