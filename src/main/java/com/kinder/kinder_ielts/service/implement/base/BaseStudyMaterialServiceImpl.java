package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.MaterialLink;
import com.kinder.kinder_ielts.entity.StudyMaterial;
import com.kinder.kinder_ielts.repository.StudyMaterialRepository;
import com.kinder.kinder_ielts.service.base.BaseStudyMaterialService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Implementation of {@link BaseStudyMaterialService}.
 * Provides CRUD operations for {@link StudyMaterial} entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BaseStudyMaterialServiceImpl extends BaseEntityServiceImpl<StudyMaterial, String> implements BaseStudyMaterialService {

    private final StudyMaterialRepository studyMaterialRepository;

    @Override
    protected StudyMaterialRepository getRepository() {
        return studyMaterialRepository;
    }

    @Override
    protected String getEntityName() {
        return "StudyMaterial";
    }

    @Override
    protected String getEntityId(StudyMaterial entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(StudyMaterial entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<StudyMaterial> entity, Account modifier, ZonedDateTime currentTime) {
        for (StudyMaterial studyMaterial : entity) {
            studyMaterial.setIsDeleted(IsDelete.DELETED);
            studyMaterial.updateAudit(modifier, currentTime);
            if (studyMaterial.getMaterialLinks() != null)
                for (MaterialLink materialLink: studyMaterial.getMaterialLinks()){
                    materialLink.setIsDeleted(IsDelete.DELETED);
                    materialLink.updateAudit(modifier, currentTime);
                }
        }
    }

    @Override
    public Page<StudyMaterial> getByStudyScheduleId(String studyScheduleId, Pageable pageable, IsDelete isDelete) {
        return studyMaterialRepository.findByStudyScheduleIdAndIsDeleted(studyScheduleId, isDelete, pageable);
    }

    @Override
    public List<StudyMaterial> getByStudyScheduleIdAndStudentId(String studyScheduleId, String studentId, IsDelete isDelete, String notFound) {
        List<StudyMaterial> studyMaterials = studyMaterialRepository.findByStudyScheduleIdAndStudentIdAndIsDeleted(studyScheduleId, studentId, isDelete);
        return studyMaterials.stream()
                .filter(studyMaterial -> studyMaterial.getViewStatus().equals(ViewStatus.VIEW))
                .toList();
    }
}