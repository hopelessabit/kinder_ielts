package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.material_link.CreateMaterialLinkRequest;
import com.kinder.kinder_ielts.dto.request.template.CreateTemplateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.response.template.study_material.TemplateStudyMaterialResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.MaterialLink;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudyMaterial;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyMaterialService;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateStudyMaterialServiceImpl {
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

    public void setMaterialLinks(TemplateStudyMaterial templateStudyMaterial, List<CreateMaterialLinkRequest> materialLinkRequests, Account actor, ZonedDateTime currentTime){
        templateStudyMaterial.setMaterialLinks(materialLinkRequests.stream().map(a -> ModelMapper.map(a, actor, currentTime)).toList());
    }

    
}
