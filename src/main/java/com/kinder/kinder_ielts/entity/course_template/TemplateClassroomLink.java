package com.kinder.kinder_ielts.entity.course_template;


import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "template_classroom_link")
public class TemplateClassroomLink extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "title", nullable = false)
    private String title;

    @Nationalized
    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "link", nullable = false, columnDefinition = "varchar(MAX)")
    private String link;

    @Enumerated(EnumType.STRING)
    @Size(max = 11)
    @Column(name = "status")
    private ViewStatus status = ViewStatus.VIEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_study_schedule_id", nullable = false)
    private TemplateStudySchedule templateStudySchedule;
}
