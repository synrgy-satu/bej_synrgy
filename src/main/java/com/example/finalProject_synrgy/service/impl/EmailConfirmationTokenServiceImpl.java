package com.example.finalProject_synrgy.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.finalProject_synrgy.entity.oauth2.EmailConfirmationToken;
import com.example.finalProject_synrgy.repository.EmailConfirmationTokenRepository;
import com.example.finalProject_synrgy.service.EmailConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailConfirmationTokenServiceImpl implements EmailConfirmationTokenService {

    private final EmailConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(EmailConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
public Optional<EmailConfirmationToken> getToken(String token) {
    return confirmationTokenRepository.findByToken(token);
}

    @Override
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

   

}
