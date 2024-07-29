package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.entity.oauth2.EmailConfirmationToken;

import java.util.Optional;

public interface EmailConfirmationTokenService {
    void saveConfirmationToken(EmailConfirmationToken token);

    Optional<EmailConfirmationToken> getToken(String token);

    int setConfirmedAt(String token);
}
