package com.kinder.kinder_ielts.constant.test;

import com.kinder.kinder_ielts.constant.IEnumerate;
import lombok.Getter;

@Getter
public enum QuestionType implements IEnumerate {
    MULTIPLE_CHOICE("Câu hỏi trắc nghiệm", "Multiple Choice"),
    FILL_IN_THE_BLANK("Điền vào chỗ trống", "Fill in the Blank"),
    TRUE_FALSE("Đúng hoặc Sai", "True/False"),
    SHORT_ANSWER("Câu trả lời ngắn", "Short Answer"),
    MATCHING("Ghép cặp", "Matching"),
    ERROR_IDENTIFICATION("Xác định lỗi sai", "Error Identification"),
    SENTENCE_COMPLETION("Hoàn thành câu", "Sentence Completion"),
    PARAGRAPH_WRITING("Viết đoạn văn", "Paragraph Writing"),
    HEADING_MATCHING("Ghép tiêu đề", "Heading Matching");

    private final String vietnamese;
    private final String english;

    QuestionType(String vietnamese, String english) {
        this.vietnamese = vietnamese;
        this.english = english;
    }
}
