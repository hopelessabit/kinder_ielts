package com.kinder.kinder_ielts.constant.test;

import com.kinder.kinder_ielts.constant.IEnumerate;
import lombok.Getter;

@Getter
public enum WordType implements IEnumerate {
    NOUN("Danh từ", "Noun"),
    VERB("Động từ", "Verb"),
    ADJECTIVE("Tính từ", "Adjective"),
    ADVERB("Trạng từ", "Adverb"),
    PRONOUN("Đại từ", "Pronoun"),
    PREPOSITION("Giới từ", "Preposition"),
    CONJUNCTION("Liên từ", "Conjunction"),
    INTERJECTION("Thán từ", "Interjection"),
    ARTICLE("Mạo từ", "Article"),
    PHRASAL_VERB("Cụm động từ", "Phrasal Verb"),
    IDIOM("Thành ngữ", "Idiom"),
    COLLOCATION("Sự kết hợp từ", "Collocation");

    private final String vietnamese;
    private final String english;

    WordType(String vietnamese, String english) {
        this.vietnamese = vietnamese;
        this.english = english;
    }
}
