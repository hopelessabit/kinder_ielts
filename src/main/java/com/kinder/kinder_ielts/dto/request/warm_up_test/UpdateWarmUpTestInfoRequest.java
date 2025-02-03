package com.kinder.kinder_ielts.dto.request.warm_up_test;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWarmUpTestInfoRequest {
    private String title;
    private String description;
    private String link;
}
