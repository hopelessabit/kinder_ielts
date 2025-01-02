package com.kinder.kinder_ielts.entity.join_entity;

import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.id.CourseStudentId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "course_student")
public class CourseStudent extends BaseEntity {

    @EmbeddedId
    private CourseStudentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_course_student_course")
    )
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_course_student_student")
    )
    private Student student;

    @Column(name = "assigned_date", nullable = true)
    private ZonedDateTime assignedDate;

    public CourseStudent(String courseId, String studentId, ZonedDateTime assignedDate) {
        this.id = new CourseStudentId(courseId, studentId);
        this.assignedDate = assignedDate;
    }

    public CourseStudent(Course course, Student student, ZonedDateTime assignedDate) {
        this.id = new CourseStudentId(course.getId(), student.getId());
        this.course = course;
        this.student = student;
        this.assignedDate = assignedDate;
    }
}
