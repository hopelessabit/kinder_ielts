package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CourseRepository extends BaseEntityRepository<Course, String> {
    Course findFirstByIdAndIsDeleted(String id, IsDelete isDeleted);

    List<Course> findByIdInAndIsDeletedIn(Collection<String> ids, List<IsDelete> isDeleted);
}
