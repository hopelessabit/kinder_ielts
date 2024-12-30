package com.kinder.kinder_ielts.dto.request.classroom;

import java.util.List;
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
    private String timeDescription;
    private String courseId;
    private List<String> tutorIds;
}
