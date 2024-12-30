package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.exception.SqlException;

import java.util.List;

/**
 * Service interface for managing Account entities.
 */
public interface BaseAccountService extends BaseEntityService<Account, String> {

    /**
     * Fetch accounts by status.
     *
     * @param status Account status (e.g., ACTIVE, INACTIVE)
     * @param message Custom error message
     * @return List of accounts with the given status
     */
    List<Account> getAccountsByStatus(AccountStatus status, String message);
}