package com.example.finalProject_synrgy.repository;

import com.example.finalProject_synrgy.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID>, JpaSpecificationExecutor<Notification> {
    Page<Notification> findByBelongsToIn(String[] belongsTos, Pageable pageable);
}
