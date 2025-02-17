package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.EnumCountry;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomStudent;
import com.kinder.kinder_ielts.entity.join_entity.CourseStudent;
import com.kinder.kinder_ielts.entity.join_entity.StudentHomework;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "student")
public class Student extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 255)
    @Nationalized
    @Column(name = "middle_name")
    private String middleName;

    @Size(max = 255)
    @Nationalized
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 500)
    @NotNull
    @Nationalized
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "ci", length = 20)
    private String citizenIdentification;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "dob")
    private ZonedDateTime dob;

    @Size(max = 32)
    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private EnumCountry country;

    @ManyToMany(mappedBy = "studyMaterialsForStudents",fetch = FetchType.LAZY)
    private List<StudyMaterial> studyMaterials;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentHomework> studentHomeworks;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseStudent> courseStudents;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomStudent> classroomStudents;

    public Student(String id) {
        this.id = id;
    }
}
