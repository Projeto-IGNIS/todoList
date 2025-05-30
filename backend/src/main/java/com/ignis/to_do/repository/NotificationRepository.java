package com.ignis.to_do.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ignis.to_do.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}
