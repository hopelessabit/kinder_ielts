package com.kinder.kinder_ielts.entity.test;

import com.kinder.kinder_ielts.constant.ViewStatus;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "tests")
public class Test extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", length = 500, columnDefinition = "nvarchar")
    private String name;

    @Column(name = "description", length = 1000, columnDefinition = "nvarchar")
    private String description;

    @Column(name = "view_status", length = 6, columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private ViewStatus viewStatus;

//    private Set<>
}
