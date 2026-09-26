package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.NotificationDto;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.enumpack.NotificationType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {

    //Notification can be used in many type of user so commonly user
    void createNotification(UserEntity user, String message,
                            NotificationType type, Long appointmentId);
 Response getUnreadNotifications(Long userId);
    Response markAsRead(Long notificationId,String token);
    Response getNotificationCount(String token);
}
