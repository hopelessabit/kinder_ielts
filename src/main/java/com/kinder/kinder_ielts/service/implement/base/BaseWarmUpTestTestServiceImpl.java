package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.WarmUpTest;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.WarmUpTestRepository;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseWarmUpTestService;

import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseWarmUpTestTestServiceImpl extends BaseEntityServiceImpl<WarmUpTest, String> implements BaseWarmUpTestService {
    private final BaseAccountService baseAccountService;
    private final WarmUpTestRepository warmUpTestRepository;
    @Override
    protected BaseEntityRepository<WarmUpTest, String> getRepository() {
        return warmUpTestRepository;
    }

    @Override
    protected String getEntityName() {
        return "Warm Up Test";
    }

    @Override
    protected String getEntityId(WarmUpTest entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(WarmUpTest entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.setStatus(ViewStatus.HIDDEN);
        entity.updateAudit(SecurityContextHolderUtil.getAccount(), ZonedDateTime.now());
    }

    @Override
    protected void markAsDeleted(List<WarmUpTest> entity, Account modifier, ZonedDateTime currentTime) {
        for (WarmUpTest warmUpTest : entity) {
            warmUpTest.setIsDeleted(IsDelete.DELETED);
            warmUpTest.setStatus(ViewStatus.HIDDEN);
            warmUpTest.updateAudit(modifier, currentTime);
        }
    }
}
