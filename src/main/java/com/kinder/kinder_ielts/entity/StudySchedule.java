package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity(name = "study_schedule")
public class StudySchedule extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Size(max = 500)
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "beLongToStudySchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClassroomLink> classroomLinks;

    @OneToMany(mappedBy = "beLongTo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WarmUpTest> warmUpTests;

    @OneToMany(mappedBy = "beLongTo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Homework> homework;

    @OneToMany(mappedBy = "beLongTo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudyMaterial> studyMaterials;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Classroom belongToClassroom;
}
