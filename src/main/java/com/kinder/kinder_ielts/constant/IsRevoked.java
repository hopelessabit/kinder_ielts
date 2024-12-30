package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum IsRevoked {
    NOT_REVOKED ("Chưa bị thu hồi"),
    REVOKED ("Bị thu hồi");
    private final String vietnamese;
    IsRevoked(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
