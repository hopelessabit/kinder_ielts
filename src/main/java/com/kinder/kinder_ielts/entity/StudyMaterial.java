package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "study_material")
public class StudyMaterial extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "link", nullable = false)
    private String link;

    @Size(max = 500)
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_schedule_id", nullable = false)
    private StudySchedule beLongTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_status")
    private StudyMaterialStatus privacyStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "study_material_student", // Name of the join table
            joinColumns = @JoinColumn(
                    name = "study_material_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_stu_mat_study_material")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "student_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_stu_mat_student")
            )
    )
    private List<Student> studyMaterialsForStudents;
}