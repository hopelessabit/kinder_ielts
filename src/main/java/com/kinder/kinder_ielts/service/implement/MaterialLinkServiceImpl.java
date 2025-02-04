package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.Role;
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
        return MaterialLinkResponse.detailWithDetail(materialLink);
    }

    public MaterialLinkResponse getInfo(String materialLinkId, Boolean includeForAdmin, String failMessage) {
        AuthorizationUtil.isUserAdminOrModOrTutor(includeForAdmin);
        return new MaterialLinkResponse(baseMaterialLinkService.get(materialLinkId, IsDelete.NOT_DELETED, failMessage), includeForAdmin, false);
    }

    public MaterialLinkResponse getDetail(String materialLinkId, Boolean includeForAdmin, String notFound) {
        AuthorizationUtil.isUserAdminOrModOrTutor(includeForAdmin);
        return new MaterialLinkResponse(baseMaterialLinkService.get(materialLinkId, IsDelete.NOT_DELETED, notFound), includeForAdmin, true);
    }

    public Void delete(String materialLinkId, String deleteFailed) {
        baseMaterialLinkService.delete(materialLinkId, deleteFailed);
        return null;
    }
}
