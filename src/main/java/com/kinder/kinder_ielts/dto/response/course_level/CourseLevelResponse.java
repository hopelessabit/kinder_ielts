package com.kinder.kinder_ielts.dto.response.course_level;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kinder.kinder_ielts.dto.response.account.SubAccountResponse;
import com.kinder.kinder_ielts.dto.response.constant.IsDeletedResponse;
import com.kinder.kinder_ielts.entity.CourseLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseLevelResponse {
    private String id;
    private String name;
    private int sessions;
    private Double hours;
    private Double minInput;
    private Double minOutput;
    private String skillDevelopment;
    private String focus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse createBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String modifyTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SubAccountResponse modifyBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IsDeletedResponse isDeleted;

    public CourseLevelResponse(CourseLevel courseLevel, boolean includeInfoForAdmin) {
        this.id = courseLevel.getId();
        this.name = courseLevel.getName();
        this.sessions = courseLevel.getSessions();
        this.hours = courseLevel.getHours();
        this.minInput = courseLevel.getMinInput();
        this.minOutput = courseLevel.getMinOutput();
        this.skillDevelopment = courseLevel.getSkillDevelopment();
        this.focus = courseLevel.getFocus();

        if (includeInfoForAdmin) {
            this.createTime = courseLevel.getCreateTime() != null
                    ? courseLevel.getCreateTime().toLocalDateTime().toString()
                    : null;

            this.modifyTime = courseLevel.getModifyTime() != null
                    ? courseLevel.getModifyTime().toLocalDateTime().toString()
                    : null;

            this.createBy = SubAccountResponse.from(courseLevel.getCreateBy());

            this.modifyBy = SubAccountResponse.from(courseLevel.getModifyBy());
        }
    }

    public static CourseLevelResponse info(CourseLevel courseLevel) {
        return new CourseLevelResponse(courseLevel, false);
    }

    public static CourseLevelResponse detailed(CourseLevel courseLevel) {
        return new CourseLevelResponse(courseLevel, true);
    }
}
