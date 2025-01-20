package com.kinder.kinder_ielts.entity.course_template;

import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;
import com.kinder.kinder_ielts.constant.HomeworkStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "template_homework")
public class TemplateHomework extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 500)
    @Nationalized
    @Column(name = "description")
    private String description;

    @Lob
    @Nationalized
    @Column(name = "link", nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Size(max = 11)
    @Column(name = "status", nullable = false)
    private HomeworkStatus status;

    @Enumerated(EnumType.STRING)
    @Size(max = 7)
    @Column(name = "privacy_status", nullable = false)
    private HomeworkPrivacyStatus privacyStatus;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_study_schedule_id", nullable = false)
    private TemplateStudySchedule templateStudySchedule;
}
