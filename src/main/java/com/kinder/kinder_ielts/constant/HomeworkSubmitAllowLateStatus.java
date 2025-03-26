package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum HomeworkSubmitAllowLateStatus implements IEnumerate {
    ALLOW("Cho phép"),
    NOT_ALLOW("Không cho phép");
    private final String vietnamese;
    HomeworkSubmitAllowLateStatus(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
