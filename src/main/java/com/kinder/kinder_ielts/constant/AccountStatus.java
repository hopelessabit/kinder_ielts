package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum AccountStatus implements IEnumerate {
    ACTIVE ("Hoạt động"),
    INACTIVE ("Không hoạt động");
    private final String vietnamese;
    AccountStatus(String vietnamese){
        this.vietnamese = vietnamese;
    }
}
