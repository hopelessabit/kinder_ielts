package com.kinder.kinder_ielts.dto.request.classroom;

import com.kinder.kinder_ielts.constant.DateOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassroomRequest {
    private String description;
    private String code;
    private OffsetTime fromTime;
    private OffsetTime toTime;
    private ZonedDateTime startDate;
    private List<DateOfWeek> schedules;
}
