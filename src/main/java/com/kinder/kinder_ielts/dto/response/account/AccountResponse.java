package com.kinder.kinder_ielts.dto.response.account;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.dto.response.constant.AccountStatusResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.dto.response.constant.RoleResponse;
import com.kinder.kinder_ielts.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AccountResponse {
    private String id;
    private String username;
    private RoleResponse role;
    private AccountStatusResponse status;
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

    public AccountResponse(Account account, boolean includeInfoForAdmin) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.role = RoleResponse.from(account.getRole());
        this.status = AccountStatusResponse.from(account.getStatus());
        this.isDeleted = IsDeletedResponse.from(account.getIsDeleted());
        mapSubInfo(account, includeInfoForAdmin);
    }

    public void mapSubInfo(Account account ,boolean includeInfoForAdmin) {
        if (includeInfoForAdmin) {
            this.createTime = account.getCreateTime();

            this.modifyTime = account.getModifyTime();

            this.createBy = SubAccountResponse.from(account.getCreateBy());

            this.modifyBy = SubAccountResponse.from(account.getModifyBy());

            this.isDeleted = IsDeletedResponse.from(account.getIsDeleted());
        }
    }
}
