package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "course_level")
public class CourseLevel extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sessions", nullable = false)
    private int sessions;

    @Column(name = "hours", nullable = false)
    private Double hours;

    @Column(name = "min_input")
    private Double minInput;

    @Column(name = "min_output")
    private Double minOutput;

    @Nationalized
    @Size(max = 1000)
    @Column(name = "skill_development")
    private String skillDevelopment;

    @Nationalized
    @Size(max = 1000)
    @Column(name = "focus")
    private String focus;

    @OneToMany(mappedBy = "level")
    private List<Course> courses;
}
