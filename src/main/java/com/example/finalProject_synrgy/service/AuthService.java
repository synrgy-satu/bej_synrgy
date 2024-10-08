package com.example.finalProject_synrgy.service;


import com.example.finalProject_synrgy.dto.auth.*;
import com.example.finalProject_synrgy.entity.oauth2.User;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.security.Principal;

public interface AuthService {

    User register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    Object sendEmailOtp(EmailRequest request, String subject);

    Object confirmOtp(String otp);

    Object checkOtpValid(OtpRequest otp);

    Object resetPassword(ResetPasswordRequest request);

    User getCurrentUser(Principal principal);

    Object checkEmail(String email);

    Object checkPhoneNumber(String phoneNumber);

//    Object signWithGoogle(MultiValueMap<String, String> parameters) throws IOException;

    public Object editPassword(Principal principal, EditPasswordDto req);

    public Object editPin(Principal principal, EditPinDto req);
}
