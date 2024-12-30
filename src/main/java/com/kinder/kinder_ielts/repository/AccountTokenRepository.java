package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.entity.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, String> {
}
