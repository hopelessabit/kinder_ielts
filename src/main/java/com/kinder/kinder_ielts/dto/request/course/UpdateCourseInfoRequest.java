package com.kinder.kinder_ielts.dto.request.course;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseInfoRequest {
    private String description;
    private String detail;
    private BigDecimal price;
    private BigDecimal sale;
    private String levelId;
    private String thumbnail;
    private String icon;
}
