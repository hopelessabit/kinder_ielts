package com.kinder.kinder_ielts.dto.request.tutor;

import com.kinder.kinder_ielts.constant.EnumCountry;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
public class UpdateTutorInfoRequest {
    private String name;
    private String email;
    private String citizenIdentification;
    private String phone;
    private ZonedDateTime dob;
    private Double reading;
    private Double listening;
    private Double writing;
    private Double speaking;
    private Double overall;
    private EnumCountry country;
}
