package com.kinder.kinder_ielts.service.implement.base;

import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.repository.TutorRepository;
import com.kinder.kinder_ielts.service.base.BaseTutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseTutorServiceImpl extends BaseEntityServiceImpl<Tutor, String> implements BaseTutorService {

    private final TutorRepository tutorRepository;

    @Override
    protected TutorRepository getRepository() {
        return tutorRepository;
    }

    @Override
    protected String getEntityName() {
        return "Tutor";
    }

    @Override
    protected String getEntityId(Tutor entity) {
        return entity.getId();
    }

    @Override
    protected void markAsDeleted(Tutor entity) {
        entity.setIsDeleted(IsDelete.DELETED);
        entity.getAccount().setIsDeleted(IsDelete.DELETED);
        entity.getAccount().setStatus(com.kinder.kinder_ielts.constant.AccountStatus.INACTIVE);
    }

    @Override
    public List<Tutor> get(List<String> ids, AccountStatus accountStatus, String message) {
        log.info("Fetching {}s with IDs: {} and status: {}", getEntityName(), ids, accountStatus);
        List<Tutor> tutors = getRepository().findByIdInAndAccount_Status(ids, accountStatus);

        if (tutors.isEmpty()) {
            log.warn("No {} found for IDs: {}", getEntityName(), ids);
            throw new NotFoundException(message, Error.build(message, ids));
        }

        if (tutors.size() != ids.size()) {
            List<String> foundIds = tutors.stream()
                    .map(this::getEntityId)
                    .toList();
            List<String> idsNotFound = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            log.warn("Partial {} found. Missing IDs: {}", getEntityName(), idsNotFound);
            throw new NotFoundException(message, Error.build(message, idsNotFound));
        }

        log.info("Successfully fetched {}: {}", getEntityName(), tutors);
        return tutors;
    }
}