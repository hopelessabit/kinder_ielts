package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.exception.SqlException;

import java.util.List;

/**
 * Service interface for managing {@link Tutor} entities.
 * Extends {@link BaseEntityService} to inherit common CRUD operations.
 */
public interface BaseTutorService extends BaseEntityService<Tutor, String> {
    /**
     * Retrieves a list of {@link Tutor} entities based on their IDs and account status.
     * This method ensures that only active or specified status Tutors are fetched,
     * and validates if all requested Tutors exist.
     *
     * @param ids           A {@link List} of {@link String} representing the IDs of the Tutors to fetch.
     * @param accountStatus An {@link AccountStatus} enum specifying the desired status of the Tutors
     *                      (e.g., ACTIVE, INACTIVE).
     * @param message       A custom error message to include in exceptions if the requested Tutors are not found.
     *
     * @return A {@link List} of {@link Tutor} entities matching the specified IDs and account status.
     *
     * @throws NotFoundException If no Tutors match the specified criteria, or if only a subset of requested Tutors are found.
     * @throws SqlException      If a database access error occurs during the retrieval process.
     *
     * @implSpec
     * - The method first attempts to retrieve Tutors with the given IDs and account status.
     * - If no Tutors are found, a {@link NotFoundException} is thrown with the provided error message.
     * - If the size of the fetched list does not match the requested list, a partial {@link NotFoundException}
     *   is thrown detailing the missing IDs.
     * - All Tutors must match the specified account status.
     *
     * @apiNote
     * This method is useful when fetching Tutors while ensuring they are in a specific account state (e.g., ACTIVE).
     * The custom error message allows for more context-specific exception handling.
     *
     * @example
     * <pre>
     * {@code
     * List<String> tutorIds = List.of("tutor1", "tutor2");
     * List<Tutor> tutors = baseTutorService.get(tutorIds, AccountStatus.ACTIVE, "Failed to fetch active tutors");
     * }
     * </pre>
     *
     * @see AccountStatus
     * @see Tutor
     * @see NotFoundException
     * @see SqlException
     */
    List<Tutor> get(List<String> ids, AccountStatus accountStatus, String message);
    Tutor create(Tutor tutor, String failMessage);
}