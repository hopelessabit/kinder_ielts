package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.WarmUpTest;

import java.util.List;

public interface BaseWarmUpTestService extends BaseEntityService<WarmUpTest, String> {
    List<WarmUpTest> getByStudyScheduleIdAndStudentId(String studyScheduleId, String studentId, ViewStatus view, IsDelete isDelete, String notFound);
}
