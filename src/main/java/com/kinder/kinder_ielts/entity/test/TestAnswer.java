package com.kinder.kinder_ielts.entity.test;

import com.kinder.kinder_ielts.constant.test.IsCorrect;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "test_single_answers")
public class TestAnswer extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "detail", length = 1000, columnDefinition = "nvarchar")
    private String detail;

    @Column(name = "question_id", nullable = false, insertable = false, updatable = false)
    private String questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private TestQuestion question;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_correct", length = 10, columnDefinition = "varchar", nullable = true)
    private IsCorrect isCorrect;

    @Column(name = "place", nullable = false)
    private Integer place;

    @Column(name = "correct_place")
    private Integer correctPlace;

}
