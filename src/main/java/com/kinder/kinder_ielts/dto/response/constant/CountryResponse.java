package com.kinder.kinder_ielts.dto.response.constant;

import com.kinder.kinder_ielts.constant.EnumCountry;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryResponse {
    private String name;
    private String vietnamese;

    public CountryResponse(EnumCountry country) {
        this.name = country.getEnglish();
        this.vietnamese = country.getVietnamese();
    }
}
