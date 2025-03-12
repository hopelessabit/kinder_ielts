package com.kinder.kinder_ielts.constant.test;

import com.kinder.kinder_ielts.constant.IEnumerate;

public enum TestQuestionType implements IEnumerate {
    SINGLE("single"),
    MULTIPLE("multiple"),
    FILL_IN_BLANK("fill_in_blank"),
    MATCHING("matching"),
    SHORT_ANSWER("short_answer"),
    CORRECT_ORDER("correct_order"),
    ESSAY("essay");

    private final String vietnamese;

    TestQuestionType(String vietnamese) {
        this.vietnamese = vietnamese;
    }

    @Override
    public String getVietnamese() {
        return vietnamese;
    }
}
