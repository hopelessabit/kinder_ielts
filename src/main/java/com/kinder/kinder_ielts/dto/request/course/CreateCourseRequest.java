package com.kinder.kinder_ielts.dto.request.course;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {
    private String description;
    private String detail;
    private String thumbnailLink;
    private String iconLink;
    private BigDecimal price;
    private String levelId;
    private Integer slots;
    private List<String> tutorIds;
}
