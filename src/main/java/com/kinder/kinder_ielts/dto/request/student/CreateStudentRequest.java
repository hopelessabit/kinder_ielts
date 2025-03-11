package com.kinder.kinder_ielts.dto.request.student;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudentRequest {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String phone;
    @Nullable
    private String addToCourseId;
    @Nullable
    private String addToClassroomId;
}
