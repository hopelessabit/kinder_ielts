package com.kinder.kinder_ielts.mapper;

import com.kinder.kinder_ielts.constant.*;
import com.kinder.kinder_ielts.dto.request.account.CreateAccountRequest;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.link.CreateClassroomLinkRequest;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.request.homework.CreateHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.material_link.CreateMaterialLinkRequest;
import com.kinder.kinder_ielts.dto.request.student.CreateStudentRequest;
import com.kinder.kinder_ielts.dto.request.study_material.CreateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.study_schedule.CreateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.template.CreateTemplateStudyMaterialRequest;
import com.kinder.kinder_ielts.dto.request.template.classroom_link.CreateTemplateClassroomLink;
import com.kinder.kinder_ielts.dto.request.template.homework.CreateTemplateHomeworkRequest;
import com.kinder.kinder_ielts.dto.request.template.study_schedule.CreateTemplateStudyScheduleRequest;
import com.kinder.kinder_ielts.dto.request.template.warmup_test.CreateTemplateWarmupTestRequest;
import com.kinder.kinder_ielts.dto.request.tutor.CreateTutorRequest;
import com.kinder.kinder_ielts.dto.request.warm_up_test.CreateWarmUpTestRequest;
import com.kinder.kinder_ielts.entity.*;
import com.kinder.kinder_ielts.entity.course_template.*;
import com.kinder.kinder_ielts.util.IdUtil;
import com.kinder.kinder_ielts.util.PasswordUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import com.kinder.kinder_ielts.util.TimeUtil;
import com.kinder.kinder_ielts.util.name.NameParts;
import com.kinder.kinder_ielts.util.name.NameUtil;

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
        course.setThumbnailLink(request.getThumbnailLink());
        course.setIconLink(request.getIconLink());
        course.setPrice(request.getPrice());
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
        templateStudySchedule.setPlace(request.getPlace());
        if (request.getStatus() != null)
            templateStudySchedule.setStatus(request.getStatus());
        else
            templateStudySchedule.setStatus(ViewStatus.VIEW);
        templateStudySchedule.setCreateBy(SecurityContextHolderUtil.getAccount());
        templateStudySchedule.setCreateTime(ZonedDateTime.now());
        templateStudySchedule.setIsDeleted(IsDelete.NOT_DELETED);
        templateStudySchedule.setDescription(request.getDescription());
        templateStudySchedule.setTitle(request.getTitle());
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
        classroomLink.setStatus(ViewStatus.VIEW);
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
        warmUpTest.setStatus(ViewStatus.VIEW);
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
        homework.setViewStatus(request.getViewStatus() != null ? request.getViewStatus() : ViewStatus.VIEW);
        homework.setPrivacyStatus(request.getPrivacyStatus() != null ? request.getPrivacyStatus() : HomeworkPrivacyStatus.PUBLIC);
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

    public static TemplateHomework map(CreateTemplateHomeworkRequest request) {
        TemplateHomework templateHomework = new TemplateHomework();
        templateHomework.setId(IdUtil.generateId());
        templateHomework.setTitle(request.getTitle());
        templateHomework.setDescription(request.getDescription());
        templateHomework.setLink(request.getLink());
        templateHomework.setPrivacyStatus(request.getPrivacyStatus());
        templateHomework.setStatus(request.getStatus());
        templateHomework.setStartDate(request.getStartDate());
        templateHomework.setDueDate(request.getDueDate());
        templateHomework.setIsDeleted(IsDelete.NOT_DELETED);
        templateHomework.setCreateBy(SecurityContextHolderUtil.getAccount());
        templateHomework.setCreateTime(ZonedDateTime.now());
        return templateHomework;
    }

    public static TemplateStudyMaterial map(CreateTemplateStudyMaterialRequest request, Account actor, ZonedDateTime currentTime) {
        TemplateStudyMaterial templateStudyMaterial = new TemplateStudyMaterial();
        templateStudyMaterial.setId(IdUtil.generateId());
        templateStudyMaterial.setTitle(request.getTitle());
        templateStudyMaterial.setDescription(request.getDescription());
        templateStudyMaterial.setPrivacyStatus(request.getPrivacyStatus() != null ? request.getPrivacyStatus() : StudyMaterialStatus.PUBLIC);
        templateStudyMaterial.setViewStatus(request.getViewStatus() != null ? request.getViewStatus() : ViewStatus.VIEW);
        templateStudyMaterial.initForNew(actor, currentTime);
        return templateStudyMaterial;
    }

    public static MaterialLink map(CreateMaterialLinkRequest request, TemplateStudyMaterial templateStudyMaterial, Account actor, ZonedDateTime currentTime) {
        MaterialLink materialLink = new MaterialLink();
        materialLink.setId(IdUtil.generateId());
        materialLink.setTitle(request.getTitle());
        materialLink.setLink(request.getLink());
        materialLink.setTemplateStudyMaterial(templateStudyMaterial);

        materialLink.initForNew(actor, currentTime);

        return materialLink;
    }

    public static MaterialLink map(CreateMaterialLinkRequest request, StudyMaterial studyMaterial, Account actor, ZonedDateTime currentTime) {
        MaterialLink materialLink = new MaterialLink();
        materialLink.setId(IdUtil.generateId());
        materialLink.setTitle(request.getTitle());
        materialLink.setLink(request.getLink());
        materialLink.setStudyMaterial(StudyMaterial.from(studyMaterial));

        materialLink.initForNew(actor, currentTime);

        return materialLink;
    }

    public static TemplateWarmUpTest map(CreateTemplateWarmupTestRequest request) {
        TemplateWarmUpTest templateWarmUpTest = new TemplateWarmUpTest();
        templateWarmUpTest.setId(IdUtil.generateId());
        templateWarmUpTest.setTitle(request.getTitle());
        templateWarmUpTest.setDescription(request.getDescription());
        templateWarmUpTest.setLink(request.getLink());
        templateWarmUpTest.setStatus(request.getStatus());

        templateWarmUpTest.setCreateBy(SecurityContextHolderUtil.getAccount());
        templateWarmUpTest.setCreateTime(ZonedDateTime.now());
        templateWarmUpTest.setIsDeleted(IsDelete.NOT_DELETED);
        return templateWarmUpTest;
    }

    public static TemplateClassroomLink map(CreateTemplateClassroomLink request) {
        TemplateClassroomLink templateClassroomLink = new TemplateClassroomLink();
        templateClassroomLink.setId(IdUtil.generateId());
        templateClassroomLink.setTitle(request.getTitle());
        templateClassroomLink.setDescription(request.getDescription());
        templateClassroomLink.setLink(request.getLink());
        templateClassroomLink.setStatus(request.getStatus());

        templateClassroomLink.setCreateBy(SecurityContextHolderUtil.getAccount());
        templateClassroomLink.setCreateTime(ZonedDateTime.now());
        templateClassroomLink.setIsDeleted(IsDelete.NOT_DELETED);
        return templateClassroomLink;
    }

    public static Student map(CreateStudentRequest request, ZonedDateTime currentTime, Account creator) {
        Account account = new Account();
        account.setId(IdUtil.generateId());
        account.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        //TODO: set default avatar
        account.setRole(Role.STUDENT);
        account.setIsDeleted(IsDelete.NOT_DELETED);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreateBy(creator);
        account.setCreateTime(currentTime);

        Student student = new Student(account.getId());
        student.setId(account.getId());

        NameParts nameParts = NameUtil.splitName(request.getName());
        student.setFirstName(nameParts.firstName);
        student.setMiddleName(nameParts.middleName);
        student.setLastName(nameParts.lastName);
        student.setFullName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setCreateBy(creator);
        student.setCreateTime(currentTime);

        account.setUsername(nameParts.firstName.toLowerCase() + nameParts.lastName.toLowerCase() + request.getPhone().substring(request.getPhone().length() -4));
        student.setAccount(account);
        return student;
    }

    public static Tutor map(CreateTutorRequest request, Account creator, ZonedDateTime currentTime) {
        Account account = new Account();
        account.setId(IdUtil.generateId());
        account.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        account.setRole(Role.TUTOR);
        account.setIsDeleted(IsDelete.NOT_DELETED);
        account.setStatus(AccountStatus.ACTIVE);
        account.initForNew(creator, currentTime);

        Tutor tutor = new Tutor();
        tutor.setId(account.getId());
        NameParts nameParts = NameUtil.splitName(request.getName());
        tutor.setFirstName(nameParts.firstName);
        tutor.setMiddleName(nameParts.middleName);
        tutor.setLastName(nameParts.lastName);
        tutor.setFullName(request.getName());
        tutor.setEmail(request.getEmail());
        tutor.setPhone(request.getPhone());
        tutor.setCountry(request.getCountry());
        tutor.setReading(request.getReading());
        tutor.setListening(request.getListening());
        tutor.setSpeaking(request.getSpeaking());
        tutor.setWriting(request.getWriting());
        tutor.setOverall(request.getOverall());
        tutor.initForNew(creator, currentTime);

        if (request.getCertificate() != null && !request.getCertificate().isEmpty()) {
            tutor.setCertificates(request.getCertificate()
                    .stream()
                    .map(certificate -> {
                        TutorCertificate cert = new TutorCertificate();
                        cert.setId(IdUtil.generateId());
                        cert.setTutor(tutor);
                        cert.setDetail(certificate);
                        cert.initForNew(creator, currentTime);
                        return cert;
                    })
                    .toList());
        }
        account.setUsername("t_" + nameParts.firstName + nameParts.lastName);
        tutor.setAccount(account);
        return tutor;
    }
}
