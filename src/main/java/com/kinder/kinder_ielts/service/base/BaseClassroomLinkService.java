package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.ClassroomLink;

import java.util.List;

public interface BaseClassroomLinkService extends BaseEntityService<ClassroomLink, String> {
    List<ClassroomLink> findByStudyScheduleIdAndViewStatusAndIsDeleted(String id, ViewStatus viewStatus, IsDelete isDelete, String notFound);
}
