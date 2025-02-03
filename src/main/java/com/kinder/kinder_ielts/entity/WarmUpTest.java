package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.HomeworkStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.WarmUpTestStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.course_template.TemplateWarmUpTest;
import com.kinder.kinder_ielts.util.IdUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "warm_up_test")
public class WarmUpTest extends BaseEntity {
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

    @Size(max = 255)
    @Column(name = "link", nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Size(max = 11)
    @Column(name = "status", nullable = false)
    private WarmUpTestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_schedule_id", nullable = false)
    private StudySchedule beLongTo;

    public static WarmUpTest from(TemplateWarmUpTest templateWarmUpTest, StudySchedule studySchedule, Account account, ZonedDateTime currentTime) {
        WarmUpTest warmUpTest = new WarmUpTest();
        warmUpTest.setId(IdUtil.generateId());
        warmUpTest.setTitle(templateWarmUpTest.getTitle());
        warmUpTest.setDescription(templateWarmUpTest.getDescription());
        warmUpTest.setLink(templateWarmUpTest.getLink());
        warmUpTest.setStatus(templateWarmUpTest.getStatus());
        warmUpTest.setBeLongTo(studySchedule);
        warmUpTest.setCreateBy(account);
        warmUpTest.setCreateTime(currentTime);
        warmUpTest.setIsDeleted(IsDelete.NOT_DELETED);
        return warmUpTest;
    }
}
