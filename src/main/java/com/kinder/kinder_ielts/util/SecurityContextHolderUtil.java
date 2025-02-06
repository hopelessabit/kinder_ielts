package com.kinder.kinder_ielts.util;

import com.kinder.kinder_ielts.constant.Role;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.exception.InternalServerExceptionException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderUtil {
    private SecurityContextHolderUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Role getRole(){
        try {
            var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof String){
                return null;
            }
            return ((Account) principal).getRole();
        } catch (Exception e){
            throw new InternalServerExceptionException("Cannot get role from SecurityContextHolder", Error.build(e.getMessage()));
        }
    }

    public static String getAccountId(){
        return ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static Account getAccount(){
        return ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
