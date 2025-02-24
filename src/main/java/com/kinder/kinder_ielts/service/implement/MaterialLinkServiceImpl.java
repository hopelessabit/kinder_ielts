package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.dto.request.material_link.UpdateMaterialLinkRequest;
import com.kinder.kinder_ielts.dto.response.material_link.MaterialLinkResponse;
import com.kinder.kinder_ielts.entity.MaterialLink;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseMaterialLinkService;
import com.kinder.kinder_ielts.util.AuthorizationUtil;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialLinkServiceImpl {
    private final BaseMaterialLinkService baseMaterialLinkService;
    private final BaseAccountService baseAccountService;

    public MaterialLinkResponse updateMaterialLink(String materialLinkId, UpdateMaterialLinkRequest request, String failMessage) {
        MaterialLink materialLink = baseMaterialLinkService.get(materialLinkId, IsDelete.NOT_DELETED, failMessage);
        materialLink.setTitle(CompareUtil.compare(request.getTitle().trim(), materialLink.getTitle()));
        materialLink.setLink(CompareUtil.compare(request.getLink().trim(), materialLink.getLink()));
        materialLink.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
        baseMaterialLinkService.update(materialLink, failMessage);
        return MaterialLinkResponse.detail(materialLink);
    }

    public MaterialLinkResponse get(String materialLinkId, Boolean includeForAdmin, String notFound) {
        AuthorizationUtil.isUserAdminOrModOrTutor(includeForAdmin);
        return new MaterialLinkResponse(baseMaterialLinkService.get(materialLinkId, IsDelete.NOT_DELETED, notFound), includeForAdmin);
    }

    public Void delete(String materialLinkId, String deleteFailed) {
        baseMaterialLinkService.delete(materialLinkId, deleteFailed);
        return null;
    }

    public Void delete(List<String> materialLinkIds, String deleteFailed) {
        baseMaterialLinkService.delete(materialLinkIds, deleteFailed);
        return null;
    }

    public MaterialLinkResponse updateViewStatus(String materialLinkId, String failMessage) {
        MaterialLink materialLink = baseMaterialLinkService.get(materialLinkId, IsDelete.NOT_DELETED, failMessage);

        materialLink.setViewStatus(materialLink.getViewStatus().equals(ViewStatus.VIEW) ? ViewStatus.HIDDEN : ViewStatus.VIEW);

        materialLink.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());

        baseMaterialLinkService.update(materialLink, failMessage);

        return MaterialLinkResponse.detail(materialLink);
    }
}
