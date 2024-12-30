package com.kinder.kinder_ielts.dto.response.constant;

import com.kinder.kinder_ielts.constant.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoleResponse {
    private String name;
    private String vietnamese;

    public RoleResponse(Role role) {
        this.name = role.name();
        this.vietnamese = role.getVietnamese();
    }

    public static RoleResponse from(Role role) {
        return new RoleResponse(role);
    }
}
