package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.StudyMaterialStatus;
import com.kinder.kinder_ielts.dto.request.material_link.CreateMaterialLinkRequest;
import com.kinder.kinder_ielts.dto.request.template.CreateTemplateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.template.study_material.UpdateTemplateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.response.template.study_material.TemplateStudyMaterialResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudyMaterial;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyMaterialService;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.service.template.TemplateStudyMaterialService;
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
public class TemplateStudyMaterialServiceImpl implements TemplateStudyMaterialService {
    private final BaseTemplateStudyScheduleService baseTemplateStudyScheduleService;
    private final BaseTemplateStudyMaterialService baseTemplateStudyMaterialService;

    public TemplateStudyMaterialResponse create(String templateStudyScheduleId, CreateTemplateStudyMaterialRequest request, String failMessage){
        Account actor = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        TemplateStudyMaterial templateStudyMaterial = ModelMapper.map(request, actor, currentTime);
        TemplateStudySchedule templateStudySchedule = baseTemplateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);

        templateStudyMaterial.setTemplateStudySchedule(templateStudySchedule);
        baseTemplateStudyMaterialService.create(templateStudyMaterial, failMessage);
        if (request.getMaterialLinks() == null || request.getMaterialLinks().isEmpty()){
            return TemplateStudyMaterialResponse.infoWithDetails(templateStudyMaterial);
        }

        setMaterialLinks(templateStudyMaterial, request.getMaterialLinks(), actor, currentTime);
        return TemplateStudyMaterialResponse.infoWithDetails(baseTemplateStudyMaterialService.update(templateStudyMaterial, failMessage));
    }

    protected void setMaterialLinks(TemplateStudyMaterial templateStudyMaterial, List<CreateMaterialLinkRequest> materialLinkRequests, Account actor, ZonedDateTime currentTime){
        templateStudyMaterial.setMaterialLinks(materialLinkRequests.stream().map(a -> ModelMapper.map(a, templateStudyMaterial, actor, currentTime)).toList());
    }

    public TemplateStudyMaterialResponse get(String templateStudyMaterialId, String failMessage){
        return TemplateStudyMaterialResponse.infoWithDetails(baseTemplateStudyMaterialService.get(templateStudyMaterialId, IsDelete.NOT_DELETED, failMessage));
    }

    public List<TemplateStudyMaterialResponse> getByTemplateStudyScheduleId(String templateStudyScheduleId, String failMessage){
        TemplateStudySchedule templateStudySchedule = baseTemplateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);
        List<TemplateStudyMaterial> templateStudyMaterials = templateStudySchedule.getStudyMaterials();

        if (templateStudyMaterials == null || templateStudyMaterials.isEmpty()){
            return List.of();
        }

        return templateStudyMaterials.stream().filter(a -> a.getIsDeleted().equals(IsDelete.NOT_DELETED)).map(TemplateStudyMaterialResponse::info).toList();
    }

    public TemplateStudyMaterialResponse updateInfo(String templateStudyMaterialId, UpdateTemplateStudyMaterialRequest request, String failMessage){
        TemplateStudyMaterial templateStudyMaterial = baseTemplateStudyMaterialService.get(templateStudyMaterialId, IsDelete.NOT_DELETED, failMessage);

        templateStudyMaterial.setTitle(CompareUtil.compare(request.getTitle(), templateStudyMaterial.getTitle()));
        templateStudyMaterial.setDescription(CompareUtil.compare(request.getDescription(), templateStudyMaterial.getDescription()));

        templateStudyMaterial.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateStudyMaterial.setModifyTime(ZonedDateTime.now());

        return TemplateStudyMaterialResponse.infoWithDetails(baseTemplateStudyMaterialService.update(templateStudyMaterial, failMessage));
    }

    public TemplateStudyMaterialResponse updatePrivacyStatus(String templateStudyMaterialId, StudyMaterialStatus privacyStatus, String failMessage){
        TemplateStudyMaterial templateStudyMaterial = baseTemplateStudyMaterialService.get(templateStudyMaterialId, IsDelete.NOT_DELETED, failMessage);

        templateStudyMaterial.setPrivacyStatus(privacyStatus);

        templateStudyMaterial.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateStudyMaterial.setModifyTime(ZonedDateTime.now());

        return TemplateStudyMaterialResponse.infoWithDetails(baseTemplateStudyMaterialService.update(templateStudyMaterial, failMessage));
    }

    public void delete(String templateStudyMaterialId, String failMessage){
        baseTemplateStudyMaterialService.delete(templateStudyMaterialId, failMessage);
    }
}
