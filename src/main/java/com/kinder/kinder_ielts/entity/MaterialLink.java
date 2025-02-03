package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudyMaterial;
import com.kinder.kinder_ielts.util.IdUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "material_link")
public class MaterialLink extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Nationalized
    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "link", nullable = false)
    private String link;

    // Relationship with StudyMaterial (using join table)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "material_link_study_material", // Name of the join table
            joinColumns = @JoinColumn(
                    name = "material_link_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_m_l_st_m_material_link")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "study_material_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_m_l_st_m_study_material")
            )

    )
    private StudyMaterial studyMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "material_link_template_study_material", // Name of the join table
            joinColumns = @JoinColumn(
                    name = "material_link_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_m_l_temp_st_m_material_link")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "template_study_material_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_m_l_temp_st_m_study_material")
            )
    )
    private TemplateStudyMaterial templateStudyMaterial;

    public MaterialLink(MaterialLink materialLink, StudyMaterial studyMaterial, Account account, ZonedDateTime currentTime) {
        this.id = IdUtil.generateId();
        this.title = materialLink.getTitle();
        this.link = materialLink.getLink();
        this.studyMaterial = studyMaterial;
        this.setCreateBy(account);
        this.setCreateTime(currentTime);
        this.setIsDeleted(IsDelete.NOT_DELETED);
    }

    public MaterialLink(MaterialLink materialLink, TemplateStudyMaterial templateStudyMaterial, Account account, ZonedDateTime currentTime) {
        this.id = IdUtil.generateId();
        this.title = materialLink.getTitle();
        this.link = materialLink.getLink();
        this.templateStudyMaterial = templateStudyMaterial;
        this.setCreateBy(account);
        this.setCreateTime(currentTime);
        this.setIsDeleted(IsDelete.NOT_DELETED);
    }
}
