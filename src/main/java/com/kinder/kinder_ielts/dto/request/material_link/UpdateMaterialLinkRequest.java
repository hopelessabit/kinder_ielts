package com.kinder.kinder_ielts.dto.request.material_link;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaterialLinkRequest {
    private String title;
    private String link;
}
