package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Tutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TutorRepository extends BaseEntityRepository<Tutor, String> {
    List<Tutor> findByIdInAndAccount_Status(Collection<String> ids, AccountStatus status);

}
