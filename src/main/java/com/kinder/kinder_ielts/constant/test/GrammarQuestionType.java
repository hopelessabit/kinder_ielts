package com.kinder.kinder_ielts.constant.test;

import com.kinder.kinder_ielts.constant.IEnumerate;
import lombok.Getter;

@Getter
public enum GrammarQuestionType implements IEnumerate {
    MULTIPLE_CHOICE("Câu hỏi trắc nghiệm", "Multiple Choice"),
    FILL_IN_THE_BLANK("Điền vào chỗ trống", "Fill in the Blank"),
    TRUE_FALSE("Đúng hoặc Sai", "True/False"),
    SHORT_ANSWER("Câu trả lời ngắn", "Short Answer"),
    ERROR_IDENTIFICATION("Xác định lỗi sai", "Error Identification"),
    MATCHING("Nối cặp", "Matching"),
    PARAGRAPH_WRITING("Viết đoạn văn", "Paragraph Writing");

    private final String vietnamese;
    private final String english;

    GrammarQuestionType(String vietnamese, String english) {
        this.vietnamese = vietnamese;
        this.english = english;
    }
}
