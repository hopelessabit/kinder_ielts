package com.kinder.kinder_ielts.dto.request.template.classroom_link;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateClassroomLinkRequest {
    private String title;
    private String description;
    private String link;
}
