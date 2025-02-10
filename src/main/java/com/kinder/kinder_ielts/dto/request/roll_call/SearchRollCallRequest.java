package com.kinder.kinder_ielts.dto.request.roll_call;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.RollCallStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchRollCallRequest {
    private String searchStudent;
    private String searchModifier;
    private String searchClassroom;
    private String studyScheduleId;
    private List<RollCallStatus> statuses;
    private Boolean includeForAdmin = false;
    private Boolean includeStudySchedule = false;
    private IsDelete isDelete = IsDelete.NOT_DELETED;
}
