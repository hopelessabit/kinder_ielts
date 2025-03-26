package com.kinder.kinder_ielts.dto.request.student_homework;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateStudentHomeworkRequest {
    private String submitFile;
    private String submitText;

    public static boolean isValid(UpdateStudentHomeworkRequest request){
        return !(request.getSubmitFile() == null && request.getSubmitText() == null);
    }
}
