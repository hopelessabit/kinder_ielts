package com.kinder.kinder_ielts.repository;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends BaseEntityRepository<Classroom, String> {
    List<Classroom> findByIdInAndIsDeletedIn(List<String> ids, List<IsDelete> isDeletes);

    Optional<Classroom> findByIdAndIsDeleted(String id, IsDelete findDeleted);

    Page<Classroom> findAll(Specification<Classroom> classroomSpecification, Pageable unsortedPageable);

    Page<Classroom> findAll(Specification<Classroom> classroomSpecification);

    @Query(value = """
select c.*
from study_material sm
left join study_schedule sc on sm.study_schedule_id = sc.id
left join classroom c on sc.classroom_id = c.id
where sm.id = :studyMaterialId
""", nativeQuery = true)
    Optional<Classroom> findByStudyMaterialId(String studyMaterialId);
}
