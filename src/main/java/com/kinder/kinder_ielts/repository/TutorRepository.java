package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Tutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TutorRepository extends BaseEntityRepository<Tutor, String> {
    List<Tutor> findByIdInAndAccount_Status(Collection<String> ids, AccountStatus status);

    @Modifying
    @Query(value = """
INSERT INTO tutor (id, create_time, create_by, is_deleted, email, first_name, last_name, middle_name, full_name, ci, phone, dob, reading, listening, speaking, writing, overall, country)
VALUES (:#{#tutor.id}, :#{#tutor.createTime}, :#{#tutor.createBy.id}, :#{#tutor.isDeleted}, :#{#tutor.email}, :#{#tutor.firstName}, :#{#tutor.lastName}, :#{#tutor.middleName}, :#{#tutor.fullName}, :#{#tutor.citizenIdentification}, :#{#tutor.phone}, :#{#tutor.dob}, :#{#tutor.reading}, :#{#tutor.listening}, :#{#tutor.speaking}, :#{#tutor.writing}, :#{#tutor.overall}, :#{#tutor.country.name});
""",nativeQuery = true)
    void createTutor(Tutor tutor);
}
