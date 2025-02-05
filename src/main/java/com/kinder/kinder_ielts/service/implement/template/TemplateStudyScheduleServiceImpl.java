package com.kinder.kinder_ielts.service.implement.template;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.CreateTemplateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.TemplateStudySchedulePlace;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.UpdateTemplateStudySchedulePlaceRequest;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.UpdateTemplateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.response.template.study_schedule.TemplateStudyScheduleResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.course_template.TemplateClassroom;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.response_message.TemplateStudyScheduleMessage;
import com.kinder.kinder_ielts.service.base.BaseTemplateClassroomService;
import com.kinder.kinder_ielts.service.base.BaseTemplateStudyScheduleService;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateStudyScheduleServiceImpl {
    private final BaseTemplateStudyScheduleService baseTemplateStudyScheduleService;
    private final BaseTemplateClassroomService baseTemplateClassroomService;

    public TemplateStudyScheduleResponse get(String templateStudyScheduleId, String failMessage){
        return TemplateStudyScheduleResponse.infoWithDetail(baseTemplateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, TemplateStudyScheduleMessage.NOT_FOUND));
    }

    public List<TemplateStudyScheduleResponse> getByTemplateClassroomId(String templateClassroomId, String failMessage) {
        List<TemplateStudySchedule> templateStudySchedules = baseTemplateClassroomService.get(templateClassroomId, IsDelete.NOT_DELETED, failMessage).getStudySchedules();
        if (templateStudySchedules == null || templateStudySchedules.isEmpty()) {
            return new ArrayList<>();
        }
        return templateStudySchedules.stream().filter(a -> a.getIsDeleted().equals(IsDelete.NOT_DELETED)).sorted(Comparator.comparing(TemplateStudySchedule::getPlace)).map(TemplateStudyScheduleResponse::infoWithDetail).toList();
    }

    public TemplateStudyScheduleResponse createTemplateStudySchedule(String templateClassroomId, CreateTemplateStudyScheduleRequest request, String failMessage) {
        TemplateClassroom templateClassroom = baseTemplateClassroomService.get(templateClassroomId, IsDelete.NOT_DELETED, failMessage);
//        List<TemplateStudySchedule> existedTemplateStudySchedule = templateClassroom.getStudySchedules();
        TemplateStudySchedule templateStudySchedule = ModelMapper.map(request);

//        if (existedTemplateStudySchedule.isEmpty())
//            templateStudySchedule.setPlace(1);
//        else {
//            templateStudySchedule.setPlace(existedTemplateStudySchedule.size() + 1);
//        }

        templateStudySchedule.setTemplateClassroom(templateClassroom);

        return TemplateStudyScheduleResponse.detailWithDetail(baseTemplateStudyScheduleService.create(templateStudySchedule, failMessage));
    }

    public TemplateStudyScheduleResponse updateInfo(String templateStudyScheduleId, UpdateTemplateStudyScheduleRequest request, String failMessage) {
        TemplateStudySchedule templateStudySchedule = baseTemplateStudyScheduleService.get(templateStudyScheduleId, IsDelete.NOT_DELETED, failMessage);

        templateStudySchedule.setModifyBy(SecurityContextHolderUtil.getAccount());
        templateStudySchedule.setModifyTime(ZonedDateTime.now());

        templateStudySchedule.setTitle(CompareUtil.compare(request.getTitle().trim(), templateStudySchedule.getTitle()));
        templateStudySchedule.setDescription(CompareUtil.compare(request.getDescription().trim(), templateStudySchedule.getDescription()));

        return TemplateStudyScheduleResponse.detailWithDetail(baseTemplateStudyScheduleService.update(templateStudySchedule, failMessage));
    }

    public void delete(String templateStudyScheduleId, String failMessage) {
        baseTemplateStudyScheduleService.delete(templateStudyScheduleId, failMessage);
    }

    public void modifyPlaceV2(String templateClassroomId, UpdateTemplateStudySchedulePlaceRequest request, String failMessage) {
        Account actor = SecurityContextHolderUtil.getAccount();
        ZonedDateTime now = ZonedDateTime.now();
        TemplateClassroom templateClassroom = baseTemplateClassroomService.get(templateClassroomId, IsDelete.NOT_DELETED, failMessage);
        List<TemplateStudySchedule> allTemplateStudySchedule = templateClassroom.getStudySchedules();
        List<String> templateStudyScheduleIds = request.getTemplateStudySchedulePlaces().stream().map(TemplateStudySchedulePlace::getTemplateStudyScheduleId).toList();
        List<String> templateStudyScheduleIdsNotFound = new ArrayList<>();
        for (String templateStudyScheduleId : templateStudyScheduleIds) {
            if (allTemplateStudySchedule.stream().noneMatch(template -> template.getId().equals(templateStudyScheduleId))) {
                templateStudyScheduleIdsNotFound.add(templateStudyScheduleId);
            }
        }

        if (!templateStudyScheduleIdsNotFound.isEmpty())
            throw new NotFoundException(
                    failMessage,
                    new Error<>(
                            "Template Study Schedule with Ids: " + templateStudyScheduleIdsNotFound + " not found",
                            templateStudyScheduleIdsNotFound
                    )
            );

        for (TemplateStudySchedulePlace templateStudySchedulePlace : request.getTemplateStudySchedulePlaces()) {
            TemplateStudySchedule templateStudySchedule = allTemplateStudySchedule.stream()
                    .filter(template -> template.getId().equals(templateStudySchedulePlace.getTemplateStudyScheduleId()))
                    .findFirst().get();

            templateStudySchedule.setPlace(templateStudySchedulePlace.getPlace());
            templateClassroom.setModifyBy(actor);
            templateClassroom.setModifyTime(now);
        }


        baseTemplateClassroomService.update(templateClassroom, failMessage);
    }

    public void modifyPlace(String templateClassroomId, String templateStudyScheduleId, int toPlace, String failMessage) {
        TemplateClassroom templateClassroom = baseTemplateClassroomService.get(templateClassroomId, IsDelete.NOT_DELETED, failMessage);
        List<TemplateStudySchedule> allTemplateStudySchedule = templateClassroom.getStudySchedules()
                .stream()
                .sorted(Comparator.comparing(TemplateStudySchedule::getPlace))
                .toList();


        TemplateStudySchedule templateStudySchedule = allTemplateStudySchedule.stream()
                .filter(template -> template.getId().equals(templateStudyScheduleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        failMessage,
                        new Error(
                                "Template Study Schedule with Id: " + templateStudyScheduleId + " not found",
                                List.of(templateStudyScheduleId)
                        )
                ));

        Account actor = SecurityContextHolderUtil.getAccount();

        if (templateStudySchedule.getPlace() < toPlace){
            for (int i = (templateStudySchedule.getPlace()); i <= (toPlace - 1); i++) {
                TemplateStudySchedule currentTemplateStudySchedule = allTemplateStudySchedule.get(i);
                currentTemplateStudySchedule.setPlace(i);
                currentTemplateStudySchedule.setModifyBy(actor);
                currentTemplateStudySchedule.setModifyTime(ZonedDateTime.now());
            }
            templateStudySchedule.setPlace(toPlace);
            templateClassroom.setModifyBy(actor);
            templateClassroom.setModifyTime(ZonedDateTime.now());
        } else {
            for (int i = (toPlace - 1); i <= (templateStudySchedule.getPlace() - 2); i++) {
                TemplateStudySchedule currentTemplateStudySchedule = allTemplateStudySchedule.get(i);
                currentTemplateStudySchedule.setPlace(i + 2);
                currentTemplateStudySchedule.setModifyBy(actor);
                currentTemplateStudySchedule.setModifyTime(ZonedDateTime.now());
            }
            templateStudySchedule.setModifyBy(actor);
            templateStudySchedule.setModifyTime(ZonedDateTime.now());
            templateStudySchedule.setPlace(toPlace);
        }

        baseTemplateClassroomService.update(templateClassroom, failMessage);
    }
}
