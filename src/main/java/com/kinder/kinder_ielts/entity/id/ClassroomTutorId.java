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
public class ClassroomTutorId implements Serializable {
    @Column(name = "class_id")
    private String classId;

    @Column(name = "tutor_id")
    private String tutorId;
}
