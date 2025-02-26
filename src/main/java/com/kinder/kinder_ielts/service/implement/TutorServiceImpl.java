package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.tutor.CreateTutorRequest;
import com.kinder.kinder_ielts.dto.response.tutor.TutorResponse;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.service.base.BaseTutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TutorServiceImpl {
    private final BaseTutorService baseTutorService;

    public TutorResponse getTutor(String id, boolean includeExtendDetails, IsDelete isDeleted, String failMessage){
        return new TutorResponse(baseTutorService.get(id, isDeleted, failMessage), includeExtendDetails);
    }

    public List<TutorResponse> getAllTutor(boolean includeExtendDetails, IsDelete isDeleted, String failMessage){
        List<Tutor> tutors = baseTutorService.get(isDeleted, failMessage);

        return tutors.stream().map(tutor -> new TutorResponse(tutor, includeExtendDetails)).toList();
    }

    public TutorResponse create(CreateTutorRequest request, String failMessage){
//        Tutor tutor = baseTutorService.create(request, failMessage);
//        return new TutorResponse(tutor, true);
        return null;
    }
}
