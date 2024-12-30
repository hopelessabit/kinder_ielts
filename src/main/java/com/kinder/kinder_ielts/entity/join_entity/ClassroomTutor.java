package com.kinder.kinder_ielts.entity.join_entity;

import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.id.ClassroomTutorId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "class_tutor")
public class ClassroomTutor extends BaseEntity {

    @EmbeddedId
    private ClassroomTutorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("classId")
    @JoinColumn(
            name = "class_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_class_tutor_class")
    )
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tutorId")
    @JoinColumn(
            name = "tutor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_class_tutor_tutor")
    )
    private Tutor tutor;

    @Column(name = "assigned_date", nullable = true)
    private ZonedDateTime assignedDate;

    public ClassroomTutor(String classId, String tutorId, ZonedDateTime assignedDate) {
        this.id = new ClassroomTutorId(classId, tutorId);
        this.assignedDate = assignedDate;
    }

    public ClassroomTutor(Classroom classroom, Tutor tutor, ZonedDateTime assignedDate, Account createBy, ZonedDateTime createTime) {
        this.id = new ClassroomTutorId(classroom.getId(), tutor.getId());
        this.classroom = classroom;
        this.tutor = tutor;
        this.setCreateBy(createBy);
        this.setCreateTime(createTime);
    }
}
