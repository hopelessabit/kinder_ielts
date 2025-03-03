package com.kinder.kinder_ielts.entity.id;

import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.StudySchedule;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RollCallId implements Serializable {
    @Column(name = "student_id")
    private String studentId;
    @Column(name = "study_schedule_id")
    private String studyScheduleId;

    public static RollCallId from(Student student, StudySchedule studySchedule) {
        return new RollCallId(student.getId(), studySchedule.getId());
    }
}
