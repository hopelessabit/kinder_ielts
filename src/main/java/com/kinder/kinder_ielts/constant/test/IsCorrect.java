package com.kinder.kinder_ielts.constant.test;

import com.kinder.kinder_ielts.constant.IEnumerate;
import lombok.Getter;

@Getter
public enum IsCorrect implements IEnumerate {
    CORRECT ("Đúng"),
    INCORRECT ("Sai");

    private final String vietnamese;
    IsCorrect(String vietnamese){
        this.vietnamese = vietnamese;
    }
}
