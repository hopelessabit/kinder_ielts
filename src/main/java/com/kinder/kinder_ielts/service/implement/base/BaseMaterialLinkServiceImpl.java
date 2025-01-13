package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.MaterialLink;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.MaterialLinkRepository;
import com.kinder.kinder_ielts.service.base.BaseMaterialLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseMaterialLinkServiceImpl extends BaseEntityServiceImpl<MaterialLink, String> implements BaseMaterialLinkService {
    private final MaterialLinkRepository repository;

    @Override
    protected BaseEntityRepository<MaterialLink, String> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "MaterialLink";
    }

    @Override
    protected String getEntityId(MaterialLink entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(MaterialLink entity) {
        entity.setIsDeleted(IsDelete.DELETED);
    }
}
