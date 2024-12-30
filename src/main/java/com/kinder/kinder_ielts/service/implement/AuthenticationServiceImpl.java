package com.kinder.kinder_ielts.service.implement;

import com.kinder.kinder_ielts.config.service.JwtService;
import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.authentication.*;
import com.kinder.kinder_ielts.dto.response.authentication.LoginResponseDTO;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.exception.InternalServerExceptionException;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.repository.AccountRepository;
import com.kinder.kinder_ielts.response_message.AccountMessage;
import com.kinder.kinder_ielts.response_message.AuthenticationMessage;
import com.kinder.kinder_ielts.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    public LoginResponseDTO loginAccount(LoginRequestDTO request) throws NotFoundException, InternalServerExceptionException {
        log.info("Start processing function login account");
        String accessToken;
        String refreshToken;
        try {
            log.info("Start [loginAccount] processing login with username or phone or email: {}", request.getUsername());
            var account = accountRepository.findFirstByUsername(request.getUsername())
                    .orElseThrow(() -> new NotFoundException(AccountMessage.NOT_FOUND));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole().name()))));
            accessToken = jwtService.generateToken(account);
            refreshToken = jwtService.generateRefreshToken(account);
            saveToken(account, refreshToken);
        } catch (NotFoundException ex) {
            log.error("Error occurred while login: {}", ex.getMessage());
            throw new NotFoundException(AuthenticationMessage.FAILED, Error.build(ex.getMessage()));
        } catch (Exception ex) {
            log.error("Error occurred while login: {}", ex.getMessage());
            throw new InternalServerExceptionException(AuthenticationMessage.FAILED, Error.build(ex.getMessage()));
        }
        log.info("End [loginAccount] processing function login account");
        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Override
    public ResponseData<String> register(RegisterRequestDTO request) {
        return null;
    }

    @Override
    public ResponseData<LoginResponseDTO> loginAdmin(LoginAdminRequestDTO request) {
        return null;
    }

    @Override
    public ResponseData<String> resetPassword(ResetPasswordRequest request) {
        return null;
    }

    @Override
    public ResponseData<String> verifyResetPassword(String uuid, ConfirmPasswordRequestDTO request) {
        return null;
    }

    @Override
    public ResponseData<LoginResponseDTO> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return null;
    }

    private void saveToken(Account account, String refreshToken) {
        return;
    }
}
