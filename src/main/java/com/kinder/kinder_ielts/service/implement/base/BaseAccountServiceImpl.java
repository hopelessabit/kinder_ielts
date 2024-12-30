package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.repository.AccountRepository;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseAccountServiceImpl  extends BaseEntityServiceImpl<Account, String> implements BaseAccountService {
    private final AccountRepository accountRepository;

    // ==========================
    // BaseEntityService Overrides
    // ==========================
    @Override
    protected AccountRepository getRepository() {
        return accountRepository;
    }

    @Override
    protected String getEntityName() {
        return "Account";
    }

    @Override
    protected String getEntityId(Account entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(Account account) {
        account.setIsDeleted(IsDelete.DELETED);
        account.setStatus(AccountStatus.INACTIVE);
    }

    // ==========================
    // Account-Specific Methods
    // ==========================

    /**
     * Fetch accounts by status.
     *
     * @param status Account status (e.g., ACTIVE, INACTIVE)
     * @param message Custom error message
     * @return List of accounts with the given status
     */
    public List<Account> getAccountsByStatus(AccountStatus status, String message) {
        log.info("Fetching Accounts with status: {}", status);
        List<Account> accounts = accountRepository.findByStatus(status);

        if (accounts.isEmpty()) {
            log.warn("No Accounts found with status: {}", status);
            throw new NotFoundException(message, Error.build(message));
        }

        log.info("Successfully fetched Accounts with status: {}", status);
        return accounts;
    }
}
