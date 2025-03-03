package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.tutor.CreateTutorRequest;
import com.kinder.kinder_ielts.dto.request.tutor.UpdateTutorInfoRequest;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.entity.TutorCertificate;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseTutorCertificateService;
import com.kinder.kinder_ielts.service.base.BaseTutorService;
import com.kinder.kinder_ielts.service.implement.base.BaseTutorCertificateServiceImpl;
import com.kinder.kinder_ielts.util.CompareUtil;
import com.kinder.kinder_ielts.util.SecurityContextHolderUtil;
import com.kinder.kinder_ielts.util.name.NameParts;
import com.kinder.kinder_ielts.util.name.NameUtil;
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

    public TutorResponse updateInfo(String tutorId, UpdateTutorInfoRequest request, String failMessage){
        log.info("Update tutor info with request: {}", request);
        Account updater = SecurityContextHolderUtil.getAccount();
        ZonedDateTime currentTime = ZonedDateTime.now();
        Tutor tutor = baseTutorService.get(tutorId, IsDelete.NOT_DELETED, failMessage);

        NameParts nameParts = NameUtil.splitName(request.getName());
        tutor.setFirstName(CompareUtil.compare(tutor.getFirstName(), nameParts.firstName));
        tutor.setMiddleName(CompareUtil.compare(tutor.getMiddleName(), nameParts.middleName));
        tutor.setLastName(CompareUtil.compare(tutor.getLastName(), nameParts.lastName));
        tutor.setFullName(tutor.getLastName() + " " + tutor.getMiddleName() + " " + tutor.getFirstName());
        tutor.setEmail(CompareUtil.compare(tutor.getEmail(), request.getEmail()));
        tutor.setCitizenIdentification(CompareUtil.compare(tutor.getCitizenIdentification(), request.getCitizenIdentification()));
        tutor.setPhone(CompareUtil.compare(tutor.getPhone(), request.getPhone()));
        tutor.setDob(CompareUtil.compare(tutor.getDob(), request.getDob()));
        tutor.setReading(CompareUtil.compare(tutor.getReading(), request.getReading()));
        tutor.setListening(CompareUtil.compare(tutor.getListening(), request.getListening()));
        tutor.setWriting(CompareUtil.compare(tutor.getWriting(), request.getWriting()));
        tutor.setSpeaking(CompareUtil.compare(tutor.getSpeaking(), request.getSpeaking()));
        tutor.setOverall(CompareUtil.compare(tutor.getOverall(), request.getOverall()));
        tutor.setCountry(CompareUtil.compare(tutor.getCountry(), request.getCountry()));

        baseTutorService.update(tutor, failMessage);
        log.info("Update tutor info success with id: {}, username: {}", tutor.getId(), tutor.getAccount().getUsername());
        return TutorResponse.withAccountInfo(tutor);
    }
}
