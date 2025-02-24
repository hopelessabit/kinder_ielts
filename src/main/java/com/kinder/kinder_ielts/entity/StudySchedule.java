package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.util.IdUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity(name = "study_schedule")
public class StudySchedule extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "from_time")
    private ZonedDateTime fromTime;

    @Column(name = "to_Time")
    private ZonedDateTime toTime;

    @Nationalized
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Column(name = "place")
    private Integer place;

    @Nationalized
    @Column(name = "description", length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ViewStatus status;

    @OneToMany(mappedBy = "beLongToStudySchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClassroomLink> classroomLinks;

    @OneToMany(mappedBy = "beLongTo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WarmUpTest> warmUpTests;

    @OneToMany(mappedBy = "beLongTo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Homework> homework;

    @OneToMany(mappedBy = "beLongTo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudyMaterial> studyMaterials;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Classroom classroom;

    public StudySchedule(ZonedDateTime fromTime, ZonedDateTime toTime, String title, Account createBy, ZonedDateTime createTime, Classroom classroom, Integer place) {
        this.id = IdUtil.generateId();
        this.place = place;
        this.setCreateTime(createTime);
        this.setIsDeleted(IsDelete.NOT_DELETED);
        this.setCreateBy(createBy);
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.title = title;
        this.status = ViewStatus.HIDDEN;
        this.classroom = classroom;
    }

    public StudySchedule() {}
}
