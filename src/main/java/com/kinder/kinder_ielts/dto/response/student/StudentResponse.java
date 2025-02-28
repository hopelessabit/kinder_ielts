package com.kinder.kinder_ielts.dto.response.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.dto.response.account.AccountResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.CountryResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.course.CourseInfoRequest;
import com.kinder.kinder_ielts.entity.Classroom;
import com.kinder.kinder_ielts.entity.Course;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.util.name.NameUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class StudentResponse{
    private AccountResponse account;
    private String fullName;
    private String email;
    private String citizenIdentification;
    private String phone;
    private ZonedDateTime dob;
    private String firstName;
    private String middleName;
    private String lastName;
    private CountryResponse country;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse createBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime modifyTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse modifyBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IsDeletedResponse isDeleted;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CourseInfoRequest> courses;

    public StudentResponse(Student student, boolean includeInfoForAdmin) {
        this.account = new AccountResponse(student.getAccount(), includeInfoForAdmin);
        this.email = student.getEmail();
        this.fullName = NameUtil.getFullName(student);
        this.firstName = student.getFirstName();
        this.middleName = student.getMiddleName();
        this.lastName = student.getLastName();
        this.citizenIdentification = student.getCitizenIdentification();
        this.phone = student.getPhone();
        this.dob = student.getDob();
        this.country = student.getCountry() != null ? new CountryResponse(student.getCountry()) : null;
        mapSubInfo(student, includeInfoForAdmin);
    }

    public void mapSubInfo(Student student , boolean includeInfoForAdmin) {
        if (includeInfoForAdmin) {
            this.createTime = student.getCreateTime();

            this.modifyTime = student.getModifyTime();

            this.createBy = SubAccountResponse.from(student.getCreateBy());

            this.modifyBy = SubAccountResponse.from(student.getModifyBy());

            this.isDeleted = IsDeletedResponse.from(student.getIsDeleted());
        }
    }

    public static StudentResponse info(Student student) {
        return new StudentResponse(student, false);
    }

    public static StudentResponse detail(Student student) {
        return new StudentResponse(student, true);
    }

    public static StudentResponse withCourses(Student student, List<Course> courses){
        return withCourses(student, courses, false);
    }

    public static StudentResponse withCourses(Student student, List<Course> courses, boolean autoIncludeAllClassInCourse){
        StudentResponse response = new StudentResponse(student, false);
        response.courses = courses.stream().map(CourseInfoRequest::info).toList();
        return response;
    }

    public static StudentResponse withCoursesFromClass(Student student, List<Classroom> classrooms) {
        StudentResponse response = new StudentResponse(student, false);

        List<Course> courses = classrooms.stream().map(Classroom::getCourse).distinct().toList();

        for (Course course : courses) {
            course.setClassrooms(classrooms.stream().filter(classroom -> classroom.getCourse().getId().equals(course.getId())).toList());
        }

        response.courses = courses.stream().map(CourseInfoRequest::info).toList();
        return response;
    }
}
