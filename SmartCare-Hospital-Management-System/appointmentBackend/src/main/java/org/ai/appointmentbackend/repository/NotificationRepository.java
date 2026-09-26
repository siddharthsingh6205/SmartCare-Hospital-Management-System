package org.ai.appointmentbackend.repository;

import org.ai.appointmentbackend.entity.NotificationEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.enumpack.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserAndIsRead(UserEntity recipient, boolean isRead);
    List<NotificationEntity> findByUser(UserEntity user);
    int countByUserAndIsRead(UserEntity recipient, boolean isRead);

    List<NotificationEntity> findByUserOrderByCreatedAtDesc(UserEntity user);

    int countByUserAndIsReadAndType(UserEntity user, boolean b, NotificationType notificationType);

    List<NotificationEntity> findByUserAndIsReadAndType(UserEntity user, boolean b, NotificationType notificationType);
}
