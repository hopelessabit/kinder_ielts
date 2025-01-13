package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.ClassroomLinkStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroomLink;
import com.kinder.kinder_ielts.util.IdUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "classroom_link")
public class ClassroomLink extends BaseEntity {
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
    private ClassroomLinkStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_schedule_id", nullable = false)
    private StudySchedule beLongToStudySchedule;

    public static ClassroomLink from(TemplateClassroomLink templateClassroomLink, StudySchedule studySchedule, Account account, ZonedDateTime currentTime) {
        ClassroomLink classroomLink = new ClassroomLink();
        classroomLink.setId(IdUtil.generateId());
        classroomLink.setTitle(templateClassroomLink.getTitle());
        classroomLink.setDescription(templateClassroomLink.getDescription());
        classroomLink.setLink(templateClassroomLink.getLink());
        classroomLink.setStatus(templateClassroomLink.getStatus());
        classroomLink.setBeLongToStudySchedule(studySchedule);
        classroomLink.setIsDeleted(IsDelete.NOT_DELETED);
        classroomLink.setCreateBy(account);
        classroomLink.setCreateTime(currentTime);
        return classroomLink;
    }
}
