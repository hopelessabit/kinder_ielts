package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.TutorCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorCertificateRepository extends BaseEntityRepository<TutorCertificate, String> {
}
