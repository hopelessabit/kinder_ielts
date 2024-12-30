package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.classroom.link.CreateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.response.classroom_link.ClassroomLinkResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.ClassroomLink;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseClassroomLinkService;
import com.kinder.kinder_ielts.service.base.BaseStudyScheduleService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomLinkServiceImpl {
    private final BaseAccountService baseAccountService;
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseClassroomLinkService baseClassroomLinkService;

    public ClassroomLinkResponse createClassroomLink(String scheduleId ,CreateClassroomLinkRequest request, String failMessage){
        ClassroomLink classroomLink = ModelMapper.map(request);

        String createBy = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for creator ID: {}", createBy);
        Account creator = baseAccountService.get(createBy, IsDelete.NOT_DELETED, failMessage);
        classroomLink.setCreateBy(creator);

        classroomLink.setBeLongToStudySchedule(baseStudyScheduleService.get(scheduleId, IsDelete.NOT_DELETED, failMessage));
        return ClassroomLinkResponse.detailWithStudySchedule(baseClassroomLinkService.create(classroomLink, failMessage));
    }

}
