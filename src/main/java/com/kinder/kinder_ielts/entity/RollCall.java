package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.RollCallStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.id.RollCallId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "roll_call")
public class RollCall extends BaseEntity {
    @EmbeddedId
    private RollCallId id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private RollCallStatus status;

    @Nationalized
    @Column(name = "note", nullable = true, length = 255)
    private String note;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("studentId")
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_roll_call_student")
    )
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studyScheduleId")
    @JoinColumn(
            name = "study_schedule_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_roll_call_study_schedule")
    )
    private StudySchedule studySchedule;

    public RollCall(Student student, StudySchedule studySchedule) {
        this.student = student;
        this.studySchedule = studySchedule;
        this.id = new RollCallId(student.getId(), studySchedule.getId());
        this.status = RollCallStatus.NOT_YET;
        this.setIsDeleted(IsDelete.NOT_DELETED);
        this.setCreateBy(new Account("0"));
        this.setCreateTime(student.getCreateTime());
    }

    @Override
    public String   toString() {
        return "RollCall{" +
                "id=" + id +
                ", status=" + status +
                ", note='" + note + '\'' +
                ", student=" + student +
                ", studySchedule=" + studySchedule +
                '}';
    }
}
