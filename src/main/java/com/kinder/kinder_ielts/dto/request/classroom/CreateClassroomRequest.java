package com.kinder.kinder_ielts.dto.request.classroom;

import java.time.ZonedDateTime;
import java.util.List;

import com.kinder.kinder_ielts.constant.DateOfWeek;
import com.kinder.kinder_ielts.util.Time;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClassroomRequest {
    private String description;
    private String code;
    private Time fromTime;
    private Time toTime;
    private ZonedDateTime startDate;
    private List<DateOfWeek> schedules;
    private List<String> tutorIds;
    private String templateClassroomId;
    @Nullable
    private Integer slots;
}
