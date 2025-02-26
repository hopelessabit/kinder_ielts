package com.kinder.kinder_ielts.dto.request.student;

import com.kinder.kinder_ielts.constant.EnumCountry;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
public class UpdateStudentInfoRequest {
    public String name;
    public String email;
    public String phone;
    public String citizenIdentification;
    public ZonedDateTime dob;
    public EnumCountry country;
}
