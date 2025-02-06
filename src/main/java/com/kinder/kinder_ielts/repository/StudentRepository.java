package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
