package com.kinder.kinder_ielts.constant;

import lombok.Getter;

@Getter
public enum DateOfWeek implements IEnumerate {
    MONDAY ("Thứ hai", 1),
    TUESDAY ("Thứ ba", 3),
    WEDNESDAY ("Thứ tư", 4),
    THURSDAY ("Thứ năm", 5),
    FRIDAY ("Thứ sáu", 6),
    SATURDAY ("Thứ bảy", 7),
    SUNDAY ("Chủ nhật", 8);
    private String vietnamese;
    private int value;

    DateOfWeek(String vietnamese, int value) {
        this.vietnamese = vietnamese;
        this.value = value;
    }
}
