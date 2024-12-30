package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.warm_up_test.CreateWarmUpTestRequest;
import com.kinder.kinder_ielts.dto.response.warm_up_test.WarmUpTestResponse;
import com.kinder.kinder_ielts.entity.WarmUpTest;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.response_message.WarmUpMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseStudyScheduleService;
import com.kinder.kinder_ielts.service.base.BaseWarmUpTestService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarmUpTestServiceImpl {
    private final BaseAccountService baseAccountService;
    private final BaseWarmUpTestService baseWarmUpTestService;
    private final BaseStudyScheduleService baseStudyScheduleService;

    public WarmUpTestResponse createWarmUpTest (String scheduleId , CreateWarmUpTestRequest request, String message){
        WarmUpTest warmUpTest = ModelMapper.map(request);

        String createBy = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for creator ID: {}", createBy);
        warmUpTest.setCreateBy(baseAccountService.get(createBy, IsDelete.NOT_DELETED, message));

        warmUpTest.setBeLongTo(baseStudyScheduleService.get(scheduleId, IsDelete.DELETED, message));

        return WarmUpTestResponse.detail(baseWarmUpTestService.create(warmUpTest, message));
    }

    public WarmUpTestResponse getInfo(String id) {
        return WarmUpTestResponse.infoWithStudySchedule(baseWarmUpTestService.get(id, IsDelete.NOT_DELETED, WarmUpMessage.NOT_FOUND));
    }
}
