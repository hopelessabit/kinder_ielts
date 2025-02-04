package com.kinder.kinder_ielts.util;

import com.kinder.kinder_ielts.constant.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationUtil {
    public static void isUserAdminOrModOrTutor(boolean includeForAdmin) {
        Role role = null;

        try {
            role = SecurityContextHolderUtil.getRole();
        } catch (Exception e) {
            log.info("Không phân quyền. Mặc định là USER");
        }
        if (role != null && includeForAdmin && !(role.equals(Role.ADMIN) || role.equals(Role.MODERATOR) || role.equals(Role.TUTOR))) {
            throw new RuntimeException("Không có quyền truy cập");
        }
    }
    public static void isUserAdminOrMod(boolean includeForAdmin) {
        Role role = null;

        try {
            role = SecurityContextHolderUtil.getRole();
        } catch (Exception e) {
            log.info("Không phân quyền. Mặc định là USER");
        }
        if (role != null && includeForAdmin && !(role.equals(Role.ADMIN) || role.equals(Role.MODERATOR))) {
            throw new RuntimeException("Không có quyền truy cập");
        }
    }
}
