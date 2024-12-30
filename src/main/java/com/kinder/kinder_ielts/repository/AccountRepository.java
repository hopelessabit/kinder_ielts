package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends BaseEntityRepository<Account, String> {
    Optional<Account> findFirstByUsername(String username);

    Optional<Account> findFirstById(String id);

    List<Account> findByStatus(AccountStatus status);
}
