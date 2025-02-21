package com.kinder.kinder_ielts.entity.join_entity;


import com.kinder.kinder_ielts.constant.GradeStatus;
import com.kinder.kinder_ielts.constant.HomeWorkSubmitStatus;
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
}