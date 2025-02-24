package com.kinder.kinder_ielts.entity.course_template;

import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.MaterialLink;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.StudySchedule;
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
@Entity(name = "template_study_material")
public class TemplateStudyMaterial extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Nationalized
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Nationalized
    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_status")
    private StudyMaterialStatus privacyStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_status")
    private ViewStatus viewStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_study_schedule_id", nullable = false)
    private TemplateStudySchedule templateStudySchedule;

    @OneToMany(mappedBy = "templateStudyMaterial", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MaterialLink> materialLinks;
}
