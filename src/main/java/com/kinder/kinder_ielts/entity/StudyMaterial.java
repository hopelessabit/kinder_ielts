package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.constant.StudyMaterialViewStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudyMaterial;
import com.kinder.kinder_ielts.util.IdUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Nationalized
    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @Nationalized
    @Size(max = 500)
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_schedule_id", nullable = false)
    private StudySchedule beLongTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_status", nullable = false)
    private StudyMaterialViewStatus viewStatus = StudyMaterialViewStatus.VIEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_status", nullable = false)
    private StudyMaterialStatus privacyStatus = StudyMaterialStatus.PUBLIC;

    @OneToMany(mappedBy = "studyMaterial", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MaterialLink> materialLinks;

    @ManyToMany(fetch = FetchType.EAGER)
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
    private Set<Student> studyMaterialsForStudents;

    public StudyMaterial(String id) {
        this.id = id;
    }

    public static StudyMaterial from(StudyMaterial studyMaterial){
        return new StudyMaterial(studyMaterial.getId());
    }

    public static StudyMaterial from(TemplateStudyMaterial sm, StudySchedule studySchedule, Account account, ZonedDateTime currentTime) {
        StudyMaterial studyMaterial = new StudyMaterial();
        studyMaterial.setId(IdUtil.generateId());
        studyMaterial.setTitle(sm.getTitle());
        studyMaterial.setDescription(sm.getDescription());
        studyMaterial.setBeLongTo(studySchedule);
        studyMaterial.setPrivacyStatus(sm.getPrivacyStatus());
        studyMaterial.setViewStatus(sm.getViewStatus());

        List<MaterialLink> materialLinks = new ArrayList<>(sm.getMaterialLinks().stream().map(materialLink -> new MaterialLink(materialLink, studyMaterial, account, currentTime)).toList());
        studyMaterial.setMaterialLinks(materialLinks);

        studyMaterial.setCreateBy(account);
        studyMaterial.setCreateTime(currentTime);
        studyMaterial.setIsDeleted(IsDelete.NOT_DELETED);

        return studyMaterial;
    }
}