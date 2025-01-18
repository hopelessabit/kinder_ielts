package com.kinder.kinder_ielts.dto.request.template.warmup_test;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateWarmupTestRequest {
    private String title;
    private String description;
    private String link;
}
