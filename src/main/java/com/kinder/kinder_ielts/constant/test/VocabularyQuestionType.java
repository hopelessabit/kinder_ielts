package com.kinder.kinder_ielts.constant.test;

import com.kinder.kinder_ielts.constant.IEnumerate;
import lombok.Getter;

@Getter
public enum VocabularyQuestionType implements IEnumerate {
    WORD_MEANING("Nghĩa của từ", "Word Meaning"),
    WORD_FORM("Dạng từ", "Word Form"),
    SYNONYM("Từ đồng nghĩa", "Synonym"),
    ANTONYM("Từ trái nghĩa", "Antonym"),
    COLLOCATION("Cụm từ thông dụng", "Collocation"),
    IDIOM("Thành ngữ", "Idiom"),
    PHRASES("Cụm từ", "Phrases");

    private final String vietnamese;
    private final String english;

    VocabularyQuestionType(String vietnamese, String english) {
        this.vietnamese = vietnamese;
        this.english = english;
    }
}
