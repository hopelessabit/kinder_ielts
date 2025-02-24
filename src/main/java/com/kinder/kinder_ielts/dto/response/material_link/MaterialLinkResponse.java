package com.kinder.kinder_ielts.dto.response.material_link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.dto.response.BaseEntityResponse;
import com.kinder.kinder_ielts.dto.response.StatusResponse;
import com.kinder.kinder_ielts.dto.response.template.study_material.TemplateStudyMaterialResponse;
import com.kinder.kinder_ielts.entity.MaterialLink;
import lombok.Getter;

@Getter
public class MaterialLinkResponse {
    private String id;
    private String title;
    private String link;
    private StatusResponse<ViewStatus> viewStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TemplateStudyMaterialResponse studyMaterial;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseEntityResponse extendDetail;

    public MaterialLinkResponse(MaterialLink materialLink, boolean includeInfoForAdmin) {
        this.id = materialLink.getId();
        this.title = materialLink.getTitle();
        this.link = materialLink.getLink();
        this.viewStatus = StatusResponse.from(materialLink.getViewStatus());
        mapSubInfo(materialLink, includeInfoForAdmin);
    }

    public void mapSubInfo(MaterialLink materialLink, boolean includeInfoForAdmin) {
        if (includeInfoForAdmin)
            this.extendDetail = BaseEntityResponse.from(materialLink);
    }

    public static MaterialLinkResponse info(MaterialLink materialLink) {
        return new MaterialLinkResponse(materialLink, false);
    }

    public static MaterialLinkResponse detail(MaterialLink materialLink) {
        return new MaterialLinkResponse(materialLink, true);
    }
}
