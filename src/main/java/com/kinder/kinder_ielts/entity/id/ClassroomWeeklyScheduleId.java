package com.kinder.kinder_ielts.entity.id;

import com.kinder.kinder_ielts.constant.DateOfWeek;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomWeeklyScheduleId implements Serializable {
    @Column(name = "class_id")
    private String classId;

    @Enumerated(EnumType.STRING)
    private DateOfWeek dayOfWeek;
}
