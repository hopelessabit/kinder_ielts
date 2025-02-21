package com.kinder.kinder_ielts.entity.course_template;

import com.kinder.kinder_ielts.constant.WarmUpTestStatus;
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
@Entity(name = "template_warm_up_test")
public class TemplateWarmUpTest extends BaseEntity {
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

    @Lob
    @Column(name = "link", nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Size(max = 11)
    @Column(name = "status", nullable = false)
    private WarmUpTestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_study_schedule_id", nullable = false)
    private TemplateStudySchedule templateStudySchedule;
}
