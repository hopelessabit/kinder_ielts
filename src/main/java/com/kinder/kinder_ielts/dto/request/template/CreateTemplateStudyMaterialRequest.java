package com.kinder.kinder_ielts.dto.request.template;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.dto.request.material_link.CreateMaterialLinkRequest;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateStudyMaterialRequest {
    private String title;
    private String description;
    private StudyMaterialStatus privacyStatus;
    @Nullable
    private ViewStatus viewStatus;
    private List<CreateMaterialLinkRequest> materialLinks;
}
