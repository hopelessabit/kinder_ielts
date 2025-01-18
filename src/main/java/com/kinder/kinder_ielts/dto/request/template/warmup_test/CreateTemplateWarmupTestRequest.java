package com.kinder.kinder_ielts.dto.request.template.warmup_test;
import com.kinder.kinder_ielts.constant.WarmUpTestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateWarmupTestRequest {
    private String title;
    private String description;
    private String link;
    private WarmUpTestStatus status;
}
