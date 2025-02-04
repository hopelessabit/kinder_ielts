package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.service.MaterialLinkService;
import com.kinder.kinder_ielts.service.implement.MaterialLinkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/material-links")
@RequiredArgsConstructor
public class MaterialLinkController {
    private final MaterialLinkServiceImpl materialLinkService;
}
