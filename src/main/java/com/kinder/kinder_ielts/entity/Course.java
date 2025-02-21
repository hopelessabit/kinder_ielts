package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.CourseStatus;
import com.kinder.kinder_ielts.constant.CourseType;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "course")
public class  Course extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_level_id")
    private CourseLevel level;

    @Nationalized
    @Column(name = "description", length = 4000)
    private String description;

    @Nationalized
    @Column(name = "detail", length = 4000)
    private String detail;

    @Column(name = "thumbnail_link", columnDefinition = "varchar(MAX)")
    private String thumbnailLink;

    @Column(name = "icon_link", columnDefinition = "varchar(MAX)")
    private String iconLink;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "slots")
    private Integer slots;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    private CourseStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = true)
    private CourseType type;

    @OneToMany(mappedBy = "course", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Classroom> classrooms;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseTutor> courseTutors;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseStudent> courseStudents;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemplateClassroom> templates;
}
