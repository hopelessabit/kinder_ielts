package com.kinder.kinder_ielts.dto.request.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CreateAccountRequest {
    private String fullName;
    private String email;
    private String phone;
    private ZonedDateTime dob;
}
