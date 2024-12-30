package com.kinder.kinder_ielts.util;

import com.kinder.kinder_ielts.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderUtil {
    private SecurityContextHolderUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getAccountId(){
        return ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
