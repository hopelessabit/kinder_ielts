package com.kinder.kinder_ielts.constant.test;

import com.kinder.kinder_ielts.constant.IEnumerate;
import lombok.Getter;

@Getter
public enum ReadingQuestionType implements IEnumerate {
    TRUE_FALSE("Đúng hoặc Sai", "True/False"),
    SHORT_ANSWER("Câu trả lời ngắn", "Short Answer"),
    PARAGRAPH_SUMMARY("Tóm tắt đoạn văn", "Paragraph Summary"),
    SENTENCE_COMPLETION("Hoàn thành câu", "Sentence Completion"),
    MULTIPLE_CHOICE("Câu hỏi trắc nghiệm", "Multiple Choice"),
    INFORMATION_MATCHING("Ghép thông tin", "Information Matching"),
    HEADING_MATCHING("Ghép tiêu đề", "Heading Matching");

    private final String vietnamese;
    private final String english;

    ReadingQuestionType(String vietnamese, String english) {
        this.vietnamese = vietnamese;
        this.english = english;
    }
}
