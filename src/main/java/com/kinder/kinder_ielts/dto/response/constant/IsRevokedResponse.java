package com.kinder.kinder_ielts.dto.response.constant;

import com.kinder.kinder_ielts.constant.IsRevoked;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IsRevokedResponse {
    private String name;
    private String vietnamese;

    public IsRevokedResponse(IsRevoked isRevoked) {
        this.name = isRevoked.name();
        this.vietnamese = isRevoked.getVietnamese();
    }

    public static IsRevokedResponse from(IsRevoked isRevoked) {
        return new IsRevokedResponse(isRevoked);
    }
}
