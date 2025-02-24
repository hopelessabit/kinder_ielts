package com.kinder.kinder_ielts.entity.course_template;


import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "template_study_schedule")
public class TemplateStudySchedule extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Nationalized
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Nationalized
    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "place")
    private Integer place;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ViewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private TemplateClassroom templateClassroom;

    @OneToMany(mappedBy = "templateStudySchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TemplateClassroomLink> classroomLinks;

    @OneToMany(mappedBy = "templateStudySchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TemplateWarmUpTest> warmUpTests;

    @OneToMany(mappedBy = "templateStudySchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TemplateHomework> homework;

    @OneToMany(mappedBy = "templateStudySchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TemplateStudyMaterial> studyMaterials;
}