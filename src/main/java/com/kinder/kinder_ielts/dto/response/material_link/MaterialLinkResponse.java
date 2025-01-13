package com.kinder.kinder_ielts.dto.response.material_link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.study_material.StudyMaterialResponse;
import com.kinder.kinder_ielts.dto.response.template.study_material.TemplateStudyMaterialResponse;
import com.kinder.kinder_ielts.entity.MaterialLink;
import lombok.Getter;

@Getter
public class MaterialLinkResponse {
    private String id;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TemplateStudyMaterialResponse studyMaterial;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public MaterialLinkResponse(MaterialLink materialLink, boolean includeInfoForAdmin, boolean includeDetails) {
        this.id = materialLink.getId();
        this.title = materialLink.getTitle();

        mapSubInfo(materialLink, includeInfoForAdmin);
    }

    public void mapSubInfo(MaterialLink materialLink, boolean includeInfoForAdmin) {
        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(materialLink);
    }

    public static MaterialLinkResponse info(MaterialLink materialLink) {
        return new MaterialLinkResponse(materialLink, false, false);
    }

    public static MaterialLinkResponse infoWithDetails(MaterialLink materialLink) {
        return new MaterialLinkResponse(materialLink, false, true);
    }

    public static MaterialLinkResponse detail(MaterialLink materialLink) {
        return new MaterialLinkResponse(materialLink, true, false);
    }

    public static MaterialLinkResponse detailWithDetail(MaterialLink materialLink) {
        return new MaterialLinkResponse(materialLink, true, true);
    }
}
