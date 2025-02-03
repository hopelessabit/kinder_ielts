package com.kinder.kinder_ielts.dto.request.study_schedule;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudyScheduleRequest {
    private String title;
    private String description;
    private ZonedDateTime fromTime;
    private ZonedDateTime toTime;
}
