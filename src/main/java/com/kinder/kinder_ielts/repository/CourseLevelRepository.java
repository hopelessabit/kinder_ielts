package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.CourseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseLevelRepository extends BaseEntityRepository<CourseLevel, String> {
    List<CourseLevel> findAllByIsDeleted(IsDelete isDelete);
}
