package com.kinder.kinder_ielts.mapper;

import com.kinder.kinder_ielts.constant.*;
import com.kinder.kinder_ielts.dto.request.account.CreateAccountRequest;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.link.CreateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.request.homework.CreateHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.study_material.CreateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.study_schedule.CreateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.CreateTemplateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.warm_up_test.CreateWarmUpTestRequest;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.entity.course_template.TemplateStudySchedule;
import com.kinder.kinder_ielts.util.IdUtil;
import com.kinder.kinder_ielts.util.TimeUtil;
import com.kinder.kinder_ielts.util.TimeZoneUtil;

import java.time.ZonedDateTime;

public class ModelMapper {
    public static Account map (CreateAccountRequest request, Account createBy){
        Account account = new Account();
        account.setId(IdUtil.generateId());
        account.setCreateTime(ZonedDateTime.now());
        account.setCreateBy(createBy);
        account.setModifyTime(ZonedDateTime.now());
        account.setModifyBy(createBy);
        account.setRole(Role.STUDENT);
        account.setStatus(AccountStatus.ACTIVE);
        return account;
    }
    public static Course map (CreateCourseRequest request){
        Course course = new Course();
        course.setId(IdUtil.generateId());
        course.setSlots(request.getSlots());
        course.setCreateTime(ZonedDateTime.now());
        course.setIsDeleted(IsDelete.NOT_DELETED);
        course.setDescription(request.getDescription());
        course.setDetail(request.getDetail());
        course.setPrice(request.getPrice());
        course.setSale(request.getSale());
        course.setStatus(CourseStatus.INACTIVE);
        return course;
    }
    public static Classroom map (CreateClassroomRequest request, ZonedDateTime createTime){
        Classroom classroom = new Classroom();
        classroom.setId(IdUtil.generateId());
        classroom.setCreateTime(createTime);
        classroom.setIsDeleted(IsDelete.NOT_DELETED);
        classroom.setCode(request.getCode());
        classroom.setDescription(request.getDescription());
        classroom.setFromTime(TimeUtil.convert(request.getFromTime()));
        classroom.setToTime(TimeUtil.convert(request.getToTime()));
        classroom.setStartDate(request.getStartDate());
        classroom.setFromTime(TimeUtil.convert(request.getFromTime()));
        classroom.setToTime(TimeUtil.convert(request.getToTime()));
        return classroom;
    }
    public static TemplateStudySchedule map(CreateTemplateStudyScheduleRequest request){
        TemplateStudySchedule templateStudySchedule = new TemplateStudySchedule();
        templateStudySchedule.setId(IdUtil.generateId());
        templateStudySchedule.setCreateTime(ZonedDateTime.now());
        templateStudySchedule.setIsDeleted(IsDelete.NOT_DELETED);
        templateStudySchedule.setDescription(request.getDescription());
        templateStudySchedule.setTitle(request.getTitle());
        templateStudySchedule.setDateTime(request.getDateTime());
        return templateStudySchedule;
    }
    public static StudySchedule map(CreateStudyScheduleRequest request){
        StudySchedule studySchedule = new StudySchedule();
        studySchedule.setId(IdUtil.generateId());
        studySchedule.setCreateTime(ZonedDateTime.now());
        studySchedule.setIsDeleted(IsDelete.NOT_DELETED);
        studySchedule.setDescription(request.getDescription());
        studySchedule.setTitle(request.getTitle());
        studySchedule.setFromTime(request.getFromTime());
        studySchedule.setToTime(request.getToTime());
        return studySchedule;
    }
    public static ClassroomLink map(CreateClassroomLinkRequest request){
        ClassroomLink classroomLink = new ClassroomLink();
        classroomLink.setId(IdUtil.generateId());
        classroomLink.setCreateTime(ZonedDateTime.now());
        classroomLink.setIsDeleted(IsDelete.NOT_DELETED);
        classroomLink.setDescription(request.getDescription());
        classroomLink.setTitle(request.getTitle());
        classroomLink.setLink(request.getLink());
        classroomLink.setStatus(ClassroomLinkStatus.VIEW);
        return classroomLink;
    }
    public static WarmUpTest map(CreateWarmUpTestRequest request){
        WarmUpTest warmUpTest = new WarmUpTest();
        warmUpTest.setId(IdUtil.generateId());
        warmUpTest.setCreateTime(ZonedDateTime.now());
        warmUpTest.setIsDeleted(IsDelete.NOT_DELETED);
        warmUpTest.setDescription(request.getDescription());
        warmUpTest.setTitle(request.getTitle());
        warmUpTest.setLink(request.getLink());
        warmUpTest.setStatus(WarmUpTestStatus.VIEW);
        return warmUpTest;
    }
    public static Homework map(CreateHomeworkRequest request){
        Homework homework = new Homework();
        homework.setId(IdUtil.generateId());
        homework.setCreateTime(ZonedDateTime.now());
        homework.setIsDeleted(IsDelete.NOT_DELETED);
        homework.setDescription(request.getDescription());
        homework.setTitle(request.getTitle());
        homework.setLink(request.getLink());
        homework.setStatus(HomeworkStatus.ASSIGNED);
        homework.setDueDate(request.getDueDate());
        homework.setStartDate(request.getStartDate());
        return homework;
    }
    public static StudyMaterial map(CreateStudyMaterialRequest request){
        StudyMaterial studyMaterial = new StudyMaterial();
        studyMaterial.setId(IdUtil.generateId());
        studyMaterial.setCreateTime(ZonedDateTime.now());
        studyMaterial.setIsDeleted(IsDelete.NOT_DELETED);
        studyMaterial.setDescription(request.getDescription());
        studyMaterial.setTitle(request.getTitle());
        studyMaterial.setPrivacyStatus(request.getPrivacyStatus() != null ? request.getPrivacyStatus() : StudyMaterialStatus.PUBLIC);
        return studyMaterial;
    }
}
