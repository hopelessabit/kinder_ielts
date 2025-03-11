package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomWeeklySchedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "class")
public class Classroom extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Nationalized
    @Size(max = 500)
    @Column(name = "description", nullable = false)
    private String description;

    @Size(max = 20)
    @Column(name = "code", nullable = true)
    private String code;

    @Column(name = "from_time", nullable = true)
    private OffsetTime fromTime;

    @Column(name = "to_time", nullable = true)
    private OffsetTime toTime;

    @Column(name = "start_date", nullable = true)
    private ZonedDateTime startDate;

    @Column(name = "end_date", nullable = true)
    private ZonedDateTime endDate;

    @Column(name = "course_id", insertable = false, updatable = false) // Prevents conflict with @ManyToOne
    private String courseId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "course_id",
            nullable = false
    )
    private Course course;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<StudySchedule> studySchedules;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomTutor> classroomTutors;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomStudent> classroomStudents;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ClassroomWeeklySchedule> weeklySchedule;

    @Override
    public String toString() {
        return "Classroom{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }

    public Classroom(String id) {
        this.id = id;
    }
}
