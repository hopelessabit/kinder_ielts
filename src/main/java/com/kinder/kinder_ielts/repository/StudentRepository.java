package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface StudentRepository extends BaseEntityRepository<Student, String> {
    List<Student> findByIdInAndAccount_Status(List<String> ids, AccountStatus status);

    Student findByIdAndAccount_Status(String id, AccountStatus status);

    @Query(value = """
    SELECT DISTINCT s.* FROM student s
    inner join classroom_student cs on s.id = cs.student_id
    where sc.classroom_id = :classId
    and sc.is_deleted = :isDelete
""", nativeQuery = true)
    List<Student> findAllByClassId(String classId, IsDelete isDelete);

    @Modifying
    @Query(value = """
insert into student (id, create_time, is_deleted, email, first_name, last_name, middle_name, full_name, phone, create_by) values (:id, :createTime, :isDeleted, :email, :firstName, :lastName, :middleName, :fullName, :phone, :createBy);
""", nativeQuery = true)
    void saveStudent(@Param("id") String id,
                     @Param("createTime") ZonedDateTime createTime,
                     @Param("isDeleted") IsDelete isDeleted,
                     @Param("email") String email,
                     @Param("firstName") String firstName,
                     @Param("lastName") String lastName,
                     @Param("middleName") String middleName,
                     @Param("fullName") String fullName,
                     @Param("phone") String phone,
                     @Param("createBy") String createBy);
}
