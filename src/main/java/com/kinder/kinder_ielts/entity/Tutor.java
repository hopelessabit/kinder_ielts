package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.EnumCountry;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.join_entity.ClassroomTutor;
import com.kinder.kinder_ielts.entity.join_entity.CourseTutor;
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
@Entity(name = "tutor")
//TODO: Add Address
public class Tutor extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    @Column(name = "ci", length = 12)
    private String citizenIdentification;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "dob")
    private ZonedDateTime dob;

    @Column(name = "reading")
    private Double reading;

    @Column(name = "listening")
    private Double listening;

    @Column(name = "writing")
    private Double writing;

    @Column(name = "speaking")
    private Double speaking;

    @Size(max = 32)
    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private EnumCountry country;

    @OneToMany(mappedBy = "tutor", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TutorCertificate> certificates;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseTutor> courseTutors;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomTutor> classroomTutors;
}
