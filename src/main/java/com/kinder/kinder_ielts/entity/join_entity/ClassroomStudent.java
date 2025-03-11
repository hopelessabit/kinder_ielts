package com.kinder.kinder_ielts.entity.join_entity;

import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.id.ClassStudentId;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "class_student")
public class ClassroomStudent extends BaseEntity {

    @EmbeddedId
    private ClassStudentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("classId")
    @JoinColumn(
            name = "class_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_class_student_class")
    )
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("studentId")
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_class_student_student")
    )
    private Student student;

    @Column(name = "assigned_date", nullable = true)
    private ZonedDateTime assignedDate;

    public ClassroomStudent(String classId, String studentId, ZonedDateTime assignedDate) {
        this.id = new ClassStudentId(classId, studentId);
        this.classroom = new Classroom(classId);
        this.student = new Student(studentId);
        this.assignedDate = assignedDate;
        this.initForNew(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    public ClassroomStudent(String classId, String studentId, Account creator, ZonedDateTime currentTime) {
        this.id = new ClassStudentId(classId, studentId);
        this.classroom = new Classroom(classId);
        this.student = new Student(studentId);
        this.assignedDate = currentTime;
        this.initForNew(creator, currentTime);
    }

    public ClassroomStudent(Classroom classroom, Student student, Account actor, ZonedDateTime currentTime) {
        this.id = new ClassStudentId(classroom.getId(), student.getId());
        this.classroom = classroom;
        this.student = student;
        this.assignedDate = currentTime;
        this.initForNew(actor, currentTime);
    }
}
