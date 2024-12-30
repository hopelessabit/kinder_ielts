package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkRepository extends BaseEntityRepository<Homework, String> {
    List<Homework> findByIdInAndIsDeletedIn(List<String> ids, List<IsDelete> isDeletes);

    Optional<Homework> findByIdAndIsDeleted(String id, IsDelete findDeleted);
}
