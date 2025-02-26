package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.tutor.CreateTutorRequest;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.entity.TutorCertificate;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseTutorCertificateService;
import com.kinder.kinder_ielts.service.base.BaseTutorService;
import com.kinder.kinder_ielts.service.implement.base.BaseTutorCertificateServiceImpl;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TutorServiceImpl {
    private final BaseTutorService baseTutorService;
    private final BaseAccountService baseAccountService;
    private final BaseTutorCertificateService baseTutorCertificateService;

    public TutorResponse getTutor(String id, boolean includeExtendDetails, IsDelete isDeleted, String failMessage){
        return new TutorResponse(baseTutorService.get(id, isDeleted, failMessage), includeExtendDetails);
    }

    public List<TutorResponse> getAllTutor(boolean includeExtendDetails, IsDelete isDeleted, String failMessage){
        List<Tutor> tutors = baseTutorService.get(isDeleted, failMessage);

        return tutors.stream().map(tutor -> new TutorResponse(tutor, includeExtendDetails)).toList();
    }

    public TutorResponse create(CreateTutorRequest request, String failMessage){
        log.info("Create tutor with request: {}", request);
        Account creator = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        Tutor tutor = ModelMapper.map(request, creator, currentTime);
        Account account = baseAccountService.create(tutor.getAccount(), failMessage);

        List<TutorCertificate> tutorCertificates = tutor.getCertificates();
        tutor.setCertificates(null);

        baseTutorService.create(tutor, failMessage);

        if (request.getCertificate() != null){
            baseTutorCertificateService.create(tutorCertificates, failMessage);
        }
        log.info("Create tutor success with id: {}, username: {}", tutor.getId(), tutor.getAccount().getUsername());
        tutor.setCertificates(tutorCertificates);
        return TutorResponse.withAccountInfo(tutor);
    }
}
