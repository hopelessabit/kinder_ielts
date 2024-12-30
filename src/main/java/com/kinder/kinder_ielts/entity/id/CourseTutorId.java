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
public class CourseTutorId implements Serializable {
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "tutor_id")
    private String tutorId;
}
