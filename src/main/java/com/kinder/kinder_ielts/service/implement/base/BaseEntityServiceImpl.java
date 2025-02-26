package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.exception.SqlException;
import com.kinder.kinder_ielts.repository.BaseEntityRepository;
import com.kinder.kinder_ielts.service.base.BaseEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseEntityServiceImpl<T, ID> implements BaseEntityService<T, ID> {

    protected abstract BaseEntityRepository<T, ID> getRepository();

    protected abstract String getEntityName();

    protected abstract ID getEntityId(T entity);

    public List<T> get(IsDelete isDelete, String message){
        log.info("Fetching all {} and deletion states: {}", getEntityName(), isDelete);
        List<T> entities = getRepository().findByIsDeleted(isDelete);
        log.info("Successfully fetched {}: {}", getEntityName(), entities);
        return entities;
    }

    // Fetch multiple entities
    public List<T> get(List<ID> ids, List<IsDelete> isDeletes, String message) {
        log.info("Fetching {} with IDs: {} and deletion states: {}", getEntityName(), ids, isDeletes);
        List<T> entities = getRepository().findByIdInAndIsDeletedIn(ids, isDeletes);

        if (entities.isEmpty()) {
            log.warn("No {} found for IDs: {}", getEntityName(), ids);
            throw new NotFoundException(message, Error.build("No " + getEntityName() + " found for IDs: " + ids.toString(), ids.stream().map(Object::toString).toList()));
        }

        if (entities.size() != ids.size()) {
            List<ID> foundIds = entities.stream()
                    .map(this::getEntityId)
                    .toList();
            List<ID> idsNotFound = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            log.warn("Partial {} found. Missing IDs: {}", getEntityName(), idsNotFound);
            throw new NotFoundException(message, Error.build("Partial " + getEntityName() + " found. Missing IDs: " + idsNotFound.toString(), idsNotFound));
        }

        log.info("Successfully fetched {}: {}", getEntityName(), entities);
        return entities;
    }
    // Fetch multiple entities
    public List<T> get(List<ID> ids, List<IsDelete> isDeletes, String message, boolean requireAll) {
        log.info("Fetching {} with IDs: {} and deletion states: {}", getEntityName(), ids, isDeletes);
        List<T> entities = getRepository().findByIdInAndIsDeletedIn(ids, isDeletes);

        if (!requireAll)
            return entities;

        if (entities.isEmpty()) {
            log.warn("No {} found for IDs: {}", getEntityName(), ids);
            throw new NotFoundException(message, Error.build("No " + getEntityName() + " found for IDs: " + ids.toString(), ids.stream().map(Object::toString).toList()));
        }

        if (entities.size() != ids.size()) {
            List<ID> foundIds = entities.stream()
                    .map(this::getEntityId)
                    .toList();
            List<ID> idsNotFound = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            log.warn("Partial {} found. Missing IDs: {}", getEntityName(), idsNotFound);
            throw new NotFoundException(message, Error.build("Partial " + getEntityName() + " found. Missing IDs: " + idsNotFound.toString(), idsNotFound));
        }

        log.info("Successfully fetched {}: {}", getEntityName(), entities);
        return entities;
    }

    // Fetch a single entity
    public T get(ID id, IsDelete isDeleted, String message) {
        log.info("Fetching {} with ID: {} and deletion state: {}", getEntityName(), id, isDeleted);
        return getRepository().findFirstById(id)
                .orElseThrow(() -> {
                    log.warn("{} not found for ID: {}", getEntityName(), id);
                    return new NotFoundException(message, Error.build(getEntityName() + " not found for ID: " + id, List.of(id)));
                });
    }

    // Create an entity
    public T create(T entity, String message) {
        log.info("Creating new {}: {}", getEntityName(), entity);
        try {
            T savedEntity = getRepository().save(entity);
            log.info("Successfully created {}: {}", getEntityName(), savedEntity);
            return savedEntity;
        } catch (Exception e) {
            log.error("Error creating {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build(e.getMessage()));
        }
    }

    // Create entities
    public List<T> create(List<T> entities, String message) {
        log.info("Creating new {}: {}", getEntityName(), entities);
        try {
            List<T> savedEntities = getRepository().saveAll(entities);
            log.info("Successfully created multiple {}: {}", getEntityName(), savedEntities);
            return savedEntities;
        } catch (Exception e) {
            log.error("Error creating multiple {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build(e.getMessage()));
        }
    }

    // Update an entity
    public T update(T entity, String message) {
        log.info("Updating {} with ID: {}", getEntityName(), getEntityId(entity));
        get(getEntityId(entity), IsDelete.NOT_DELETED, message);
        try {
            T updatedEntity = getRepository().save(entity);
            log.info("Successfully updated {}: {}", getEntityName(), updatedEntity);
            return updatedEntity;
        } catch (Exception e) {
            log.error("Error updating {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build("Error updating " + getEntityName(), Map.of("cause", e.getMessage())));
        }
    }

    // Update entities
    public List<T> update(List<T> entities, String message) {
        log.info("Updating {} with ID: {}", getEntityName(), entities);
        try {
            List<T> updatedEntities = getRepository().saveAll(entities);
            log.info("Successfully updated {}: {}", getEntityName(), updatedEntities);
            return updatedEntities;
        } catch (Exception e) {
            log.error("Error updating {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build("Error updating " + getEntityName(), Map.of("cause", e.getMessage())));
        }
    }

    // Delete an entity
    public void delete(ID id, String message) {
        log.info("Deleting {} with ID: {}", getEntityName(), id);
        T entity = get(id, IsDelete.NOT_DELETED, message);
        markAsDeleted(entity);
        try {
            getRepository().save(entity);
            log.info("Successfully deleted {} with ID: {}", getEntityName(), id);
        } catch (Exception e) {
            log.error("Error deleting {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build("Error deleting " + getEntityName(), Map.of("cause", e.getMessage())));
        }
    }

    //Delete entities
    public void delete(List<ID> ids, String message){
        log.info("Deleting {} with IDs: {}", getEntityName(), ids);
        List<T> entities = get(ids, null, null);
        entities.forEach(this::markAsDeleted);
        try {
            getRepository().saveAll(entities);
            log.info("Successfully deleted {} with IDs: {}", getEntityName(), ids);
        } catch (Exception e) {
            log.error("Error deleting {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build("Error deleting " + getEntityName() + "s", Map.of("cause", e.getMessage())));
        }
    }

    public void remove(ID id, String message){
        log.info("Remove {} with ID: {}", getEntityName(), id);
        T entity = get(id, IsDelete.NOT_DELETED, message);
        markAsDeleted(entity);
        try {
            getRepository().delete(entity);
            log.info("Successfully removed {} with ID: {}", getEntityName(), id);
        } catch (Exception e) {
            log.error("Error removing {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build("Error removing " + getEntityName(), Map.of("cause", e.getMessage())));
        }
    }

    public void remove(List<ID> ids, String message){
        log.info("Remove {} with IDs: {}", getEntityName(), ids);
        List<T> entities = get(ids, List.of(IsDelete.NOT_DELETED, IsDelete.DELETED), message);
        entities.forEach(this::markAsDeleted);
        try {
            getRepository().saveAll(entities);
            log.info("Successfully removed {} with IDs: {}", getEntityName(), ids);
        } catch (Exception e) {
            log.error("Error removing {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build("Error removing " + getEntityName() + "s", Map.of("cause", e.getMessage())));
        }
    }

    public void removeEntities(List<T> entities, String message){
        log.info("Remove {} with IDs: {}", getEntityName(), entities);
        try {
            getRepository().saveAll(entities);
            log.info("Successfully removed {} with IDs: {}", getEntityName(), entities);
        } catch (Exception e) {
            log.error("Error removing {}: {}", getEntityName(), e.getMessage());
            throw new SqlException(message, Error.build("Error removing " + getEntityName() + "s", Map.of("cause", e.getMessage())));
        }
    }

    protected abstract void markAsDeleted(T entity);
    protected abstract void markAsDeleted(List<T> entity, Account modifier, ZonedDateTime currentTime);

    public List<T> all(){
        return getRepository().findAll();
    }

    public Page<T> findAll(Specification<T> specification, Pageable pageable){
        return getRepository().findAll(specification, pageable);
    }

    public Page<T> get(Specification<T> specification){
        return getRepository().findAll(specification);
    }
    public Page<T> get(Specification<T> specification, Pageable pageable){
        return getRepository().findAll(specification, pageable);
    }


}