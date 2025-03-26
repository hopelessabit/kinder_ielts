package com.kinder.kinder_ielts.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class StudentHomeworkId implements Serializable {
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "homework_id")
    private String homeworkId;

    public static StudentHomeworkId of(String studentId, String homeworkId) {
        return new StudentHomeworkId(studentId, homeworkId);
    }
}
