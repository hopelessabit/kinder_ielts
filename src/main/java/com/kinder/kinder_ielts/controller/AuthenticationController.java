package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.authentication.LoginRequestDTO;
import com.kinder.kinder_ielts.dto.response.authentication.LoginResponseDTO;
import com.kinder.kinder_ielts.response_message.AuthenticationMessage;
import com.kinder.kinder_ielts.service.AuthenticationService;
import com.kinder.kinder_ielts.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ResponseData<LoginResponseDTO>> login(@RequestBody @Validated LoginRequestDTO request) {
        return ResponseUtil.getResponse(() -> authenticationService.loginAccount(request), AuthenticationMessage.SUCCESS);
    }
}
