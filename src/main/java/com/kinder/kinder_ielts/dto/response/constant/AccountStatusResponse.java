package com.kinder.kinder_ielts.dto.response.constant;

import com.kinder.kinder_ielts.constant.AccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AccountStatusResponse {
    private String name;
    private String vietnamese;

    public static AccountStatusResponse from(AccountStatus status) {
        return new AccountStatusResponse(status);
    }

    public AccountStatusResponse(AccountStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
