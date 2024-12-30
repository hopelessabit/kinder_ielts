package com.kinder.kinder_ielts.dto.response.constant;

import com.kinder.kinder_ielts.constant.IsDelete;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IsDeletedResponse {
    private String name;
    private String vietnamese;
    public static IsDeletedResponse from(IsDelete isDelete) {
        return new IsDeletedResponse(isDelete);
    }
    public IsDeletedResponse(IsDelete isDelete) {
        this.name = isDelete.name();
        this.vietnamese = isDelete.getVietnamese();
    }
}
