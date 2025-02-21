package com.kinder.kinder_ielts.entity.course_template;

import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;
import com.kinder.kinder_ielts.constant.HomeworkStatus;
import com.kinder.kinder_ielts.constant.HomeworkViewStatus;
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

    @Nationalized
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Nationalized
    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "link", columnDefinition = "varchar(MAX)", nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Size(max = 11)
    @Column(name = "status")
    private HomeworkStatus status;

    @Enumerated(EnumType.STRING)
    @Size(max = 7)
    @Column(name = "privacy_status")
    private HomeworkPrivacyStatus privacyStatus;

    @Enumerated(EnumType.STRING)
    @Size(max = 7)
    @Column(name = "view_status", nullable = false)
    private HomeworkViewStatus viewStatus = HomeworkViewStatus.VIEW;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_study_schedule_id", nullable = false)
    private TemplateStudySchedule templateStudySchedule;
}
