package com.kinder.kinder_ielts.mapper;

import com.kinder.kinder_ielts.constant.*;
import com.kinder.kinder_ielts.dto.request.account.CreateAccountRequest;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.link.CreateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.request.homework.CreateHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.study_material.CreateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.study_schedule.CreateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.warm_up_test.CreateWarmUpTestRequest;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.util.TimeZoneUtil;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ModelMapper {
    public static Account map (CreateAccountRequest request, Account createBy){
        Account account = new Account();
        account.setId(UUID.randomUUID().toString() + TimeZoneUtil.getCurrentDateTime());
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
        course.setId(UUID.randomUUID() + TimeZoneUtil.getCurrentDateTime());
        course.setCreateTime(ZonedDateTime.now());
        course.setIsDeleted(IsDelete.NOT_DELETED);
        course.setDescription(request.getDescription());
        course.setDetail(request.getDetail());
        course.setPrice(request.getPrice());
        course.setSale(request.getSale());
        return course;
    }
    public static Classroom map (CreateClassroomRequest request, ZonedDateTime createTime){
        Classroom classroom = new Classroom();
        classroom.setId(UUID.randomUUID() + TimeZoneUtil.getCurrentDateTime());
        classroom.setTimeDescription(request.getTimeDescription());
        classroom.setCreateTime(createTime);
        classroom.setIsDeleted(IsDelete.NOT_DELETED);
        classroom.setDescription(request.getDescription());
        return classroom;
    }
    public static StudySchedule map(CreateStudyScheduleRequest request){
        StudySchedule studySchedule = new StudySchedule();
        studySchedule.setId(UUID.randomUUID() + TimeZoneUtil.getCurrentDateTime());
        studySchedule.setCreateTime(ZonedDateTime.now());
        studySchedule.setIsDeleted(IsDelete.NOT_DELETED);
        studySchedule.setDescription(request.getDescription());
        studySchedule.setTitle(request.getTitle());
        studySchedule.setDateTime(request.getDateTime());
        return studySchedule;
    }
    public static ClassroomLink map(CreateClassroomLinkRequest request){
        ClassroomLink classroomLink = new ClassroomLink();
        classroomLink.setId(UUID.randomUUID() + TimeZoneUtil.getCurrentDateTime());
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
        warmUpTest.setId(UUID.randomUUID() + TimeZoneUtil.getCurrentDateTime());
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
        homework.setId(UUID.randomUUID() + TimeZoneUtil.getCurrentDateTime());
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
        studyMaterial.setId(UUID.randomUUID() + TimeZoneUtil.getCurrentDateTime());
        studyMaterial.setCreateTime(ZonedDateTime.now());
        studyMaterial.setIsDeleted(IsDelete.NOT_DELETED);
        studyMaterial.setDescription(request.getDescription());
        studyMaterial.setTitle(request.getTitle());
        studyMaterial.setLink(request.getLink());
        studyMaterial.setPrivacyStatus(request.getPrivacyStatus() != null ? request.getPrivacyStatus() : StudyMaterialStatus.PUBLIC);
        return studyMaterial;
    }
}
