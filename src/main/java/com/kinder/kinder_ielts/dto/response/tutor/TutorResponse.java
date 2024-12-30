package com.kinder.kinder_ielts.dto.response.tutor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.dto.response.TutorCertificateResponse;
import com.kinder.kinder_ielts.dto.response.account.AccountResponse;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.CountryResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.util.name.NameUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class TutorResponse {
    private AccountResponse account;
    private String fullName;
    private String email;
    private String citizenIdentification;
    private String phone;
    private ZonedDateTime dob;
    private Double reading;
    private Double listening;
    private Double writing;
    private Double speaking;
    private String firstName;
    private String middleName;
    private String lastName;
    private CountryResponse country;
    private List<TutorCertificateResponse> certificates;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse createBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZonedDateTime modifyTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse modifyBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IsDeletedResponse isDeleted;


    public TutorResponse(Tutor tutor, boolean includeInfoForAdmin) {
        this.account = new AccountResponse(tutor.getAccount(), includeInfoForAdmin);
        this.email = tutor.getEmail();
        this.fullName = NameUtil.getFullName(tutor);
        this.firstName = tutor.getFirstName();
        this.middleName = tutor.getMiddleName();
        this.lastName = tutor.getLastName();
        this.citizenIdentification = tutor.getCitizenIdentification();
        this.phone = tutor.getPhone();
        this.dob = tutor.getDob();
        this.reading = tutor.getReading();
        this.listening = tutor.getListening();
        this.writing = tutor.getWriting();
        this.speaking = tutor.getSpeaking();
        this.country = tutor.getCountry() != null ? new CountryResponse(tutor.getCountry()) : null;
        this.certificates = tutor.getCertificates() != null
                ? tutor.getCertificates().stream().map(TutorCertificateResponse::new).toList()
                : null;
        mapSubInfo(tutor, includeInfoForAdmin);
    }

    public void mapSubInfo(Tutor tutor , boolean includeInfoForAdmin) {
        if (includeInfoForAdmin) {
            this.createTime = tutor.getCreateTime();

            this.modifyTime = tutor.getModifyTime();

            this.createBy = SubAccountResponse.from(tutor.getCreateBy());

            this.modifyBy = SubAccountResponse.from(tutor.getModifyBy());

            this.isDeleted = IsDeletedResponse.from(tutor.getIsDeleted());
        }
    }

    public static TutorResponse withNoAccountInfo(Tutor tutor) {
        return new TutorResponse(tutor, false);
    }

    public static TutorResponse withAccountInfo(Tutor tutor) {
        return new TutorResponse(tutor, true);
    }
}
