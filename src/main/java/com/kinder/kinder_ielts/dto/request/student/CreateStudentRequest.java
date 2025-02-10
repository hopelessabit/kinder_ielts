package com.kinder.kinder_ielts.dto.request.student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudentRequest {
    private String name;
    private String email;
    private String username;
    private String password;
}
