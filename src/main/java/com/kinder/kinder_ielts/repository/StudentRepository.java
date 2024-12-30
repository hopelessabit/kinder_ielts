package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends BaseEntityRepository<Student, String> {
    List<Student> findByIdInAndAccount_Status(List<String> ids, AccountStatus status);

    Student findByIdAndAccount_Status(String id, AccountStatus status);
}
