package com.kinder.kinder_ielts.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClassStudentId implements Serializable {
    @Column(name = "course_id")
    private String classId;

    @Column(name = "student_id")
    private String studentId;
}
