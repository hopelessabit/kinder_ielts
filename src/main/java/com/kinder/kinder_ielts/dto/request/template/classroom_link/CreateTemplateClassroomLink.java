package com.kinder.kinder_ielts.dto.request.template.classroom_link;
import com.kinder.kinder_ielts.constant.ViewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateClassroomLink {
    private String title;
    private String description;
    private String link;
    private ViewStatus status;
}
