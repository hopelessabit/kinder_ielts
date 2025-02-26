package com.kinder.kinder_ielts.dto.response.classroom;

import com.kinder.kinder_ielts.entity.Classroom;
import lombok.Getter;

@Getter
public class ClassInfoResponse {
    private String id;
    private String description;
    private String code;

    public ClassInfoResponse(Classroom classroom) {
        this.id = classroom.getId();
        this.description = classroom.getDescription();
        this.code = classroom.getCode();
    }

    public static ClassInfoResponse info(Classroom classroom) {
        return new ClassInfoResponse(classroom);
    }
}
