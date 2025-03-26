package com.kinder.kinder_ielts.dto.request.student_homework;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GradingStudentHomeworkRequest {
    private String studentId;
    private Integer score;
    private String gradeComment;

    public static boolean isValid(GradingStudentHomeworkRequest request) {
        return request != null && request.getStudentId() != null && request.getScore() != null && (request.getScore() >= 0 && request.getScore() <= 100);
    }
}
