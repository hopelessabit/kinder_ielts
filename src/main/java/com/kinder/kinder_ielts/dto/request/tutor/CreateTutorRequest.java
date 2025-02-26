package com.kinder.kinder_ielts.dto.request.tutor;

import com.kinder.kinder_ielts.constant.EnumCountry;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@ToString
public class CreateTutorRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private EnumCountry country;
    @Nullable
    private ZonedDateTime dob;
    @Nullable
    private Double reading;
    @Nullable
    private Double listening;
    @Nullable
    private Double speaking;
    @Nullable
    private Double writing;
    @Nullable
    private Double overall;
    @Nullable
    private List<String> certificate;
    @Nullable
    private String addToCourseId;
    @Nullable
    private String addToClassroomId;
}
