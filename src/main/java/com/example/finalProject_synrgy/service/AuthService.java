package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.auth.*;
import com.example.finalProject_synrgy.entity.oauth2.User;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.security.Principal;

import javax.mail.MessagingException;

public interface AuthService {

    User register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    Object sendEmailOtp(EmailRequest request, String subject);

    Object confirmOtp(String otp);

    Object checkOtpValid(OtpRequest otp);

    Object resetPassword(ResetPasswordRequest request);

    User getCurrentUser(Principal principal);

    Object signWithGoogle(MultiValueMap<String, String> parameters) throws IOException;

    void sendRegistrationConfirmationEmail(User user, String token) throws MessagingException;

    boolean verifyUser(String token);
}
