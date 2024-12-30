package com.kinder.kinder_ielts.dto.request.add_student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddStudentRequest {
    private List<String> studentIds;
}
