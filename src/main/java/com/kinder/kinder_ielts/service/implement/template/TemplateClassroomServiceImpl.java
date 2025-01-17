package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.template.classroom.CreateTemplateClassroomRequest;
import com.kinder.kinder_ielts.dto.response.template.classroom.TemplateClassroomResponse;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import com.kinder.kinder_ielts.repository.TemplateClassroomRepository;
import com.kinder.kinder_ielts.response_message.TemplateClassroomMessage;
import com.kinder.kinder_ielts.service.TemplateClassroomService;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.base.BaseTemplateClassroomService;
import com.kinder.kinder_ielts.util.IdUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateClassroomServiceImpl implements TemplateClassroomService {
    private final BaseTemplateClassroomService baseTemplateClassroomService;
    private final BaseCourseService baseCourseService;
    private final TemplateClassroomRepository templateClassroomRepository;

    public TemplateClassroomResponse create(String courseId, CreateTemplateClassroomRequest request, String failMessage){
        TemplateClassroom templateClassroom = new TemplateClassroom();
        templateClassroom.setName(request.getName().trim());
        templateClassroom.setId(IdUtil.generateId());
        templateClassroom.setIsDeleted(IsDelete.NOT_DELETED);
        templateClassroom.setCreateTime(ZonedDateTime.now());
        templateClassroom.setCreateBy(SecurityContextHolderUtil.getAccount());
        Course course = baseCourseService.get(courseId, IsDelete.NOT_DELETED, failMessage);
        templateClassroom.setCourse(course);
        return TemplateClassroomResponse.info(baseTemplateClassroomService.create(templateClassroom, failMessage));
    }

    public TemplateClassroomResponse updateStatus(String templateClassroomId, IsDelete isDelete, String failMessage){
        TemplateClassroom templateClassroom = baseTemplateClassroomService.get(templateClassroomId, IsDelete.NOT_DELETED, failMessage);
        templateClassroom.setIsDeleted(isDelete);
        templateClassroom.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateClassroom.setModifyTime(ZonedDateTime.now());
        return TemplateClassroomResponse.info(baseTemplateClassroomService.update(templateClassroom, failMessage));
    }

    public Page<TemplateClassroomResponse> getByCourseId(String courseId, Pageable pageable){
        Page<TemplateClassroom> templateClassrooms = templateClassroomRepository.findByCourse_Id(courseId, pageable);

        return new PageImpl<>(templateClassrooms.getContent().stream().map(TemplateClassroomResponse::info).toList(), pageable, templateClassrooms.getTotalElements());
    }

    public TemplateClassroomResponse get(String templateClassroomId){
        return TemplateClassroomResponse.info(baseTemplateClassroomService.get(templateClassroomId, IsDelete.NOT_DELETED, TemplateClassroomMessage.NOT_FOUND));
    }

    public void delete(String templateClassroomId){
        baseTemplateClassroomService.delete(templateClassroomId, TemplateClassroomMessage.DELETE_FAILED);
    }
}
