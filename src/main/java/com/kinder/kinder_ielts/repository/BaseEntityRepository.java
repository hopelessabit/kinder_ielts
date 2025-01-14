package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseEntityRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> findByIsDeleted(IsDelete isDelete);
    List<T> findByIdInAndIsDeletedIn(List<ID> ids, List<IsDelete> isDeletes);
    Optional<T> findFirstById(ID id);
}