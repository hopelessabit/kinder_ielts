package com.kinder.kinder_ielts.service.base;

import com.kinder.kinder_ielts.constant.IsDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Base service interface for generic CRUD operations on entities.
 *
 * @param <T> The type of the entity.
 * @param <ID> The type of the entity ID.
 */
public interface BaseEntityService<T, ID> {
    /**
     * Fetch all entities that match the given specification.
     *
     * @param specification The specification to filter entities.
     * @return A list of fetched entities.
     */
    public Page<T> get(Specification<T> specification);

    public List<T> get(IsDelete isDelete, String message);

    /**
     * Fetch multiple entities by their IDs and deletion state.
     *
     * @param ids The list of entity IDs to fetch.
     * @param isDeletes The deletion states to consider.
     * @param message Custom error message for exceptions.
     * @return A list of fetched entities.
     */
    List<T> get(List<ID> ids, List<IsDelete> isDeletes, String message);

    /**
     * Fetch a single entity by its ID and deletion state.
     *
     * @param id The ID of the entity to fetch.
     * @param isDeleted The deletion state to consider.
     * @param message Custom error message for exceptions.
     * @return The fetched entity.
     */
    T get(ID id, IsDelete isDeleted, String message);

    List<T> get(List<ID> ids, List<IsDelete> isDeletes, String message, boolean requireAll);

    /**
     * Create a new entity.
     *
     * @param entity The entity to create.
     * @param message Custom error message for exceptions.
     * @return The created entity.
     */
    T create(T entity, String message);

    /**
     * Create a new entity.
     *
     * @param entities List of entities to create.
     * @param message Custom error message for exceptions.
     * @return The created entity.
     */
    List<T> create(List<T> entities, String message);

    /**
     * Update an existing entity.
     *
     * @param entity The entity with updated information.
     * @param message Custom error message for exceptions.
     * @return The updated entity.
     */
    T update(T entity, String message);

    List<T> update(List<T> entities, String message);

    /**
     * Delete an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     * @param message Custom error message for exceptions.
     */
    void delete(ID id, String message);

    /**
     * Delete many entities by its IDs.
     *
     * @param ids The list of entity IDs to delete.
     * @param message Custom error message for exceptions.
     */
    void delete(List<ID> ids, String message);

    /**
     * Physically remove an entity by its ID.
     *
     * @param id The ID of the entity to remove.
     * @param message Custom error message for exceptions.
     */
    void remove(ID id, String message);

    /**
     * Physically remove many entities by their IDs.
     *
     * @param ids The list of entity IDs to remove.
     * @param message Custom error message for exceptions.
     */
    void remove(List<ID> ids, String message);

    /**
     * Physically remove many entities by their IDs.
     *
     * @param entities The list of entities to remove.
     * @param message Custom error message for exceptions.
     */
    void removeEntities(List<T> entities, String message);

    /**
     * Get all entities
     *
     * @return List of all entities.
     */
    List<T> all();
}