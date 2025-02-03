package com.kinder.kinder_ielts.entity.join_entity;

import com.kinder.kinder_ielts.constant.DateOfWeek;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import com.kinder.kinder_ielts.entity.id.ClassroomWeeklyScheduleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity(name = "classroom_weekly_schedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomWeeklySchedule extends BaseEntity {
    @EmbeddedId
    private ClassroomWeeklyScheduleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("classId")
    @JoinColumn(name="class_id")
    private Classroom classroom;

    public ClassroomWeeklySchedule(Classroom classroom, DateOfWeek dateOfWeek, Account account, ZonedDateTime currentTime) {
        this.id = new ClassroomWeeklyScheduleId(classroom.getId(), dateOfWeek);
        this.classroom = classroom;
        this.setCreateBy(account);
        this.setCreateTime(currentTime);
        this.setIsDeleted(IsDelete.NOT_DELETED);
    }
}
