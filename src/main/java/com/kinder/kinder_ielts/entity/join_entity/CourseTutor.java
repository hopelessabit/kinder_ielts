package com.kinder.kinder_ielts.entity.join_entity;

import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.id.CourseTutorId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "course_tutor")
public class CourseTutor extends BaseEntity {

    @EmbeddedId
    private CourseTutorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_course_tutor_course")
    )
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tutorId")
    @JoinColumn(
            name = "tutor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_course_tutor_tutor")
    )
    private Tutor tutor;

    @Column(name = "assigned_date", nullable = true)
    private ZonedDateTime assignedDate;

    public CourseTutor(String courseId, String tutorId, ZonedDateTime assignedDate) {
        this.id = new CourseTutorId(courseId, tutorId);
        this.assignedDate = assignedDate;
    }

    public CourseTutor(Course course, Tutor tutor, ZonedDateTime assignedDate){
        this.id = new CourseTutorId(course.getId(), tutor.getId());
        this.assignedDate = assignedDate;
        this.course = course;
        this.tutor = tutor;
    }
}