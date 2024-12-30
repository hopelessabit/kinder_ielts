package com.kinder.kinder_ielts.dto.request.classroom.link;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClassroomLinkRequest {
    private String studyScheduleId;
    private String title;
    private String description;
    private String link;
}
