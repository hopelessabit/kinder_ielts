package com.kinder.kinder_ielts.dto.request.classroom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassroomStudentRequest {
    private List<String> add;
    private List<String> remove;
}
