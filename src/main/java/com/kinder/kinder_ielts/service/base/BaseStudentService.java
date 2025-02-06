package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.exception.SqlException;

import java.util.List;

/**
 * Service interface for managing {@link Student} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseStudentService extends BaseEntityService<Student, String> {
    /**
     * Retrieves a list of {@link Student} entities based on their IDs and account status.
     * Ensures that only students matching the specified account status are fetched,
     * and validates if all requested students exist.
     *
     * @param ids           A {@link List} of {@link String} representing the IDs of the students to fetch.
     * @param accountStatus An {@link AccountStatus} enum specifying the desired status of the students
     *                      (e.g., ACTIVE, INACTIVE).
     * @param message       A custom error message to include in exceptions if the requested students are not found.
     *
     * @return A {@link List} of {@link Student} entities matching the specified IDs and account status.
     *
     * @throws NotFoundException If no students match the specified criteria, or if only a subset of requested students are found.
     * @throws SqlException      If a database access error occurs during the retrieval process.
     *
     * @implSpec
     * - The method first attempts to retrieve students with the given IDs and account status.
     * - If no students are found, a {@link NotFoundException} is thrown with the provided error message.
     * - If the size of the fetched list does not match the requested list, a partial {@link NotFoundException}
     *   is thrown detailing the missing IDs.
     * - All students must match the specified account status.
     *
     * @apiNote
     * This method is useful when fetching students while ensuring they are in a specific account state (e.g., ACTIVE).
     * The custom error message allows for more context-specific exception handling.
     *
     * @example
     * <pre>
     * {@code
     * List<String> studentIds = List.of("student1", "student2");
     * List<Student> students = baseStudentService.get(studentIds, AccountStatus.ACTIVE, "Failed to fetch active students");
     * }
     * </pre>
     *
     * @see AccountStatus
     * @see Student
     * @see NotFoundException
     * @see SqlException
     */
    List<Student> get(List<String> ids, AccountStatus accountStatus, String message);

    List<Student> getByClassId(String classId, String failMessage);
    List<Student> getByClassId(String classId);
}