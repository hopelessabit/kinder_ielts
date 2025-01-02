package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.CourseStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
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
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Nationalized
    @Column(name = "detail", nullable = false, length = 500)
    private String detail;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "sale", precision = 12, scale = 2)
    private BigDecimal sale;

    @Column(name = "slots")
    private Integer slots;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    private CourseStatus status;

    @OneToMany(mappedBy = "belongToCourse", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Classroom> classrooms;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseTutor> courseTutors;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseStudent> courseStudents;
}
