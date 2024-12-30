package com.kinder.kinder_ielts.dto.request.classroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassroomRequest {
    private String description;
    private String timeDescription;
}
