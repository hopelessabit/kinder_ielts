package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudySchedule> studySchedules;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomTutor> classroomTutors;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomStudent> classroomStudents;
}
