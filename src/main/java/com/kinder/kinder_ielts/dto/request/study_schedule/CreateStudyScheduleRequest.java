package com.kinder.kinder_ielts.dto.request.study_schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudyScheduleRequest {
    private ZonedDateTime fromTime;
    private ZonedDateTime toTime;
    private String title;
    private String description;
}
