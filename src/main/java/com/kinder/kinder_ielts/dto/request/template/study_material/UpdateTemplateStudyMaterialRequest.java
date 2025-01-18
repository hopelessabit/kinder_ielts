package com.kinder.kinder_ielts.dto.request.template.study_material;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateStudyMaterialRequest {
    private String title;
    private String description;
}
