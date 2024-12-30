package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseJoinEntityRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> findByIdIn(List<ID> ids);
    Optional<T> findFirstById(ID id);
}