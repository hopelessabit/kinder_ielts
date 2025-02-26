package com.kinder.kinder_ielts.dto.request.tutor;

import lombok.Getter;

@Getter
public class CreateTutorRequest {
    private String name;
    private String email;
    private String username;
    private String password;
    private String addToCourseId;
    private String addToClassroomId;
}
