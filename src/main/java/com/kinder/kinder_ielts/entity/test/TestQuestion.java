package com.kinder.kinder_ielts.entity.test;

import com.kinder.kinder_ielts.constant.test.TestQuestionType;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "test_questions")
public class TestQuestion extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "place", nullable = false)
    private Integer place;

    @Column(name = "test_id", nullable = false, insertable = false, updatable = false)
    private String testId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @Column(name = "detail", length = 1000, columnDefinition = "nvarchar")
    private String detail;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10, columnDefinition = "varchar", nullable = true)
    private TestQuestionType type;

    @Column(name = "test_paragraph_id", nullable = true, insertable = false, updatable = false)
    private String testParagraphId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_paragraph_id", referencedColumnName = "id")
    private TestParagraph testParagraph;
}
