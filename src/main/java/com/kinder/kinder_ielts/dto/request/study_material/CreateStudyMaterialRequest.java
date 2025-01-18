package com.kinder.kinder_ielts.dto.request.study_material;

import com.kinder.kinder_ielts.constant.StudyMaterialStatus;

import java.util.List;

import com.kinder.kinder_ielts.dto.request.material_link.CreateMaterialLinkRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudyMaterialRequest {
    private String title;
    private String description;
    private StudyMaterialStatus privacyStatus;
    private List<String> studentIds;
    private List<CreateMaterialLinkRequest> materialLinks;
}
