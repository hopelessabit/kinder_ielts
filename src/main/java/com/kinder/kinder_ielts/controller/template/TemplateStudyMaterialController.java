package com.kinder.kinder_ielts.controller.template;
import com.kinder.kinder_ielts.service.implement.template.TemplateStudyMaterialServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/controller")
@RequiredArgsConstructor
public class TemplateStudyMaterialController {
    private final TemplateStudyMaterialServiceImpl templateStudyMaterialService;
}
