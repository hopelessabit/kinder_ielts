package com.kinder.kinder_ielts.entity.test;

import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "test_paragraphs")
public class TestParagraph extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "detail", columnDefinition = "nvarchar(max)", nullable = false)
    private String detail;

    @OneToMany(mappedBy = "testParagraph")
    private Set<TestQuestion> questions;

    @Column(name = "test_id", nullable = false, insertable = false, updatable = false)
    private String testId;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;


}
