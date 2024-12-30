package com.kinder.kinder_ielts.dto.response;

import com.kinder.kinder_ielts.entity.TutorCertificate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorCertificateResponse {
    private String id;
    private String detail;

    public TutorCertificateResponse(TutorCertificate certificate) {
        this.id = certificate.getId();
        this.detail = certificate.getDetail();
    }
}
