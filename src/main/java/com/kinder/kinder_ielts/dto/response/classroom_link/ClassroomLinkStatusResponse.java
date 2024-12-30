package com.kinder.kinder_ielts.dto.response.classroom_link;

import com.kinder.kinder_ielts.constant.ClassroomLinkStatus;
import lombok.Getter;

@Getter
public class ClassroomLinkStatusResponse {
    private String name;
    private String vietnamese;

    public static ClassroomLinkStatusResponse from(ClassroomLinkStatus status) {
        return new ClassroomLinkStatusResponse(status);
    }

    public ClassroomLinkStatusResponse(ClassroomLinkStatus status){
        this.name = status.name();
        this.vietnamese = status.getVietnamese();
    }
}
