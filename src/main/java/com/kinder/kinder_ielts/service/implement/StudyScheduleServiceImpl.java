package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.study_schedule.CreateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.StudySchedule;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseClassroomService;
import com.kinder.kinder_ielts.service.base.BaseStudyScheduleService;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyScheduleServiceImpl {
    private final BaseStudyScheduleService baseStudyScheduleService;
    private final BaseAccountService baseAccountService;
    private final BaseClassroomService baseClassroomService;
    public StudyScheduleResponse createStudySchedule(String classroomId, CreateStudyScheduleRequest request, String failMessage) {
        StudySchedule studySchedule = ModelMapper.map(request);

        String createBy = SecurityContextHolderUtil.getAccountId();
        log.debug("Fetching account for creator ID: {}", createBy);
        Account creator = baseAccountService.get(createBy, IsDelete.NOT_DELETED, failMessage);
        studySchedule.setCreateBy(creator);

        Classroom classroom = baseClassroomService.get(classroomId, IsDelete.NOT_DELETED, failMessage);
        studySchedule.setBelongToClassroom(classroom);

        return StudyScheduleResponse.detailWithDetail(baseStudyScheduleService.create(studySchedule, failMessage));
    }

    public StudyScheduleResponse getInfoWithDetail(String id) {
        return StudyScheduleResponse.infoWithDetail(baseStudyScheduleService.get(id, IsDelete.NOT_DELETED, StudyScheduleMessage.NOT_FOUND));
    }

    public List<StudyScheduleResponse> getAllInfo() {
        return baseStudyScheduleService.all().stream().map(StudyScheduleResponse::detailWithDetail).toList();
    }
}
