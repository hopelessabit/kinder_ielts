package com.kinder.kinder_ielts.dto.response;

import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class BaseEntityResponse {
    private ZonedDateTime createTime;
    private SubAccountResponse createBy;
    private ZonedDateTime modifyTime;
    private SubAccountResponse modifyBy;
    private StatusResponse<IsDelete> isDeleted;

    // Constructor to map fields from a BaseEntity
    public BaseEntityResponse(BaseEntity entity) {
        if (entity != null) {
            this.createTime = entity.getCreateTime();
            this.createBy = SubAccountResponse.from(entity.getCreateBy());
            this.modifyTime = entity.getModifyTime();
            this.modifyBy = entity.getModifyBy() != null ? SubAccountResponse.from(entity.getModifyBy()) : null;
            this.isDeleted = StatusResponse.from(entity.getIsDeleted());
        }
    }

    public static BaseEntityResponse from(BaseEntity entity) {
        return new BaseEntityResponse(entity);
    }
}
