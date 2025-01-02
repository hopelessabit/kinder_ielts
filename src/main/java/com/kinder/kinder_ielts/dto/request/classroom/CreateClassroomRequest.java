package com.kinder.kinder_ielts.dto.request.classroom;

import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.List;

import com.kinder.kinder_ielts.constant.DateOfWeek;
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
    private OffsetTime fromTime;
    private OffsetTime toTime;
    private ZonedDateTime startDate;
    private List<DateOfWeek> schedules;
    private List<String> tutorIds;
}
