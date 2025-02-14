package com.kinder.kinder_ielts.dto.request.study_material;

import com.kinder.kinder_ielts.dto.request.material_link.CreateMaterialLinkRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateMaterialLinksRequest {
    private List<String> remove;
    private List<CreateMaterialLinkRequest> add;
}
