package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.dto.request.material_link.UpdateMaterialLinkRequest;
import com.kinder.kinder_ielts.dto.response.material_link.MaterialLinkResponse;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseMaterialLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialLinkServiceImpl {
    private final BaseMaterialLinkService baseMaterialLinkService;
    private final BaseAccountService baseAccountService;

    public MaterialLinkResponse updateMaterialLink(String materialLinkId, UpdateMaterialLinkRequest request) {
        return null;
    }
}
