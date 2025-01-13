package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.HomeworkPrivacyStatus;
import com.kinder.kinder_ielts.constant.HomeworkStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.course_template.TemplateHomework;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import com.kinder.kinder_ielts.util.IdUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "homework")
public class Homework extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Size(max = 255)
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
    @JoinColumn(name = "study_schedule_id", nullable = false)
    private StudySchedule beLongTo;

    @OneToMany(mappedBy = "homework", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentHomework> studentHomeworks;

    public static Homework from(TemplateHomework hw, StudySchedule studySchedule, Account account, ZonedDateTime currentTime) {
        Homework homework = new Homework();
        homework.setId(IdUtil.generateId());
        homework.setTitle(hw.getTitle());
        homework.setDescription(hw.getDescription());
        homework.setLink(hw.getLink());
        homework.setStatus(hw.getStatus());
        homework.setPrivacyStatus(hw.getPrivacyStatus());
        homework.setDueDate(currentTime);
        homework.setStartDate(hw.getStartDate());
        homework.setBeLongTo(studySchedule);
        homework.setCreateBy(account);
        homework.setCreateTime(currentTime);
        homework.setIsDeleted(IsDelete.NOT_DELETED);

        return homework;
    }
}
