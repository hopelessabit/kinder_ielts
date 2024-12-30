package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.WarmUpTestStatus;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.WarmUpTest;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.repository.WarmUpTestRepository;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseWarmUpTestService;

import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

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

    //TODO: change message
    @Override
    protected void markAsDeleted(WarmUpTest entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.setStatus(WarmUpTestStatus.HIDDEN);

        String createBy = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for modifier ID: {}", createBy);
        Account modifier = baseAccountService.get(createBy, IsDelete.NOT_DELETED, CourseMessage.DELETE_FAILED);
        entity.setModifyBy(modifier);
        entity.setModifyTime(ZonedDateTime.now());
    }
}
