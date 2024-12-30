package com.kinder.kinder_ielts.dto.response.account;

import com.kinder.kinder_ielts.dto.response.constant.AccountStatusResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;

import com.kinder.kinder_ielts.dto.response.constant.RoleResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.Tutor;
import com.kinder.kinder_ielts.util.name.NameUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SubAccountResponse {
    private String id;
    private String username;
    private RoleResponse role;
    private String fullName;
    private AccountStatusResponse status;
    private IsDeletedResponse isDeleted;

    public SubAccountResponse(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.role = RoleResponse.from(account.getRole());
        this.status = AccountStatusResponse.from(account.getStatus());
        this.isDeleted = IsDeletedResponse.from(account.getIsDeleted());
        mapFullName(account);
    }

    public SubAccountResponse(Account account, String fullName) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.role = RoleResponse.from(account.getRole());
        this.status = AccountStatusResponse.from(account.getStatus());
        this.isDeleted = IsDeletedResponse.from(account.getIsDeleted());
        this.fullName = fullName;
    }

    public void mapFullName(Account account){
        switch (account.getRole()){
            case TUTOR -> this.fullName = NameUtil.getFullName(account.getTutor());
            case STUDENT -> this.fullName = NameUtil.getFullName(account.getStudent());
            default -> this.fullName = account.getUsername();
        }
    }

    public static SubAccountResponse from(Account account){
        if (account == null) return null;
        return new SubAccountResponse(account);
    }

    public static SubAccountResponse from(Account account, String fullName){
        return new SubAccountResponse(account, fullName);
    }

    public static SubAccountResponse from(Tutor tutor, String fullName){
        return new SubAccountResponse(tutor.getAccount(), fullName);
    }

    public static SubAccountResponse from(Student student, String fullName){
        return new SubAccountResponse(student.getAccount(), fullName);
    }
}
