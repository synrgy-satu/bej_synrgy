package com.example.finalProject_synrgy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.finalProject_synrgy.entity.oauth2.EmailConfirmationToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {

    Optional<EmailConfirmationToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE EmailConfirmationToken e SET e.confirmedAt = :now WHERE e.token = :token")
    int updateConfirmedAt(String token, LocalDateTime now);
}
