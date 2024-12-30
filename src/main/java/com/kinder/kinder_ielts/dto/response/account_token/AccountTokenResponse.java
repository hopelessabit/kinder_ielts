package com.kinder.kinder_ielts.dto.response.account_token;

import com.kinder.kinder_ielts.entity.AccountToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AccountTokenResponse {
    private String id;
    private String refreshToken;
    private String isRevoked;

    public AccountTokenResponse(AccountToken accountToken) {
        this.id = accountToken.getId();
        this.refreshToken = accountToken.getRefreshToken();
        this.isRevoked = accountToken.getIsRevoked().getVietnamese();
    }
}
