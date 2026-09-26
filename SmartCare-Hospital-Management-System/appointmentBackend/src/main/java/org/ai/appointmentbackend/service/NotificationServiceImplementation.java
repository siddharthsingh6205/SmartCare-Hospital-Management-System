package org.ai.appointmentbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.ai.appointmentbackend.configuration.JwtTokenProvider;
import org.ai.appointmentbackend.dto.NotificationDto;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.entity.NotificationEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.enumpack.NotificationType;
import org.ai.appointmentbackend.mapper.DtoConverter;
import org.ai.appointmentbackend.repository.NotificationRepository;
import org.ai.appointmentbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImplementation implements NotificationService{

    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public NotificationServiceImplementation(NotificationRepository notificationRepository, UserRepository userRepository, EmailService emailService,JwtTokenProvider jwtTokenProvider) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public NotificationServiceImplementation() {
    }

    public void createNotification(UserEntity user, String message,
                                   NotificationType type, Long appointmentId) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedId(appointmentId);

       notification= notificationRepository.save(notification);

        // Also send email for important notifications
        /*if (type != NotificationType.OTHER) {
            emailService.sendNotificationEmail(user.getEmail(), message);
        }*/
    }

    @Override
    public Response getUnreadNotifications(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));


            List<NotificationEntity> notifications = notificationRepository
                    .findByUserAndIsReadAndType(user,false,NotificationType.APPOINTMENT_REQUEST);

            return  Response.success( "Notifications fetched").withNotificationList(DtoConverter.convertNotificationEntityListToNotificationDtoList(notifications));
        } catch (Exception e) {
            return  Response.error("Error fetching notifications",500);
        }
    }

    @Override
    public Response markAsRead(Long notificationId, String token) {
        try {
            token=token.substring(7);
            String email = jwtTokenProvider.extractUsername(token);
            UserEntity user = userRepository.findByEmail(email);


            NotificationEntity notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

            if (!notification.getUser().equals(user)) {
                return Response.error("unauthorized",400);
            }

            notification.setRead(true);
            notificationRepository.save(notification);

            return Response.success( "Notification marked as read");
        } catch (Exception e) {
            return Response.error( "Error updating notification",500);
        }
    }

    @Override
    public Response getNotificationCount(String token) {
        try {
            token=token.substring(7);
            String email = jwtTokenProvider.extractUsername(token);
            UserEntity user = userRepository.findByEmail(email);


            int count = notificationRepository.countByUserAndIsReadAndType(user, false,NotificationType.APPOINTMENT_REQUEST);

            return Response.success( "Count fetched").withCount(count);
        } catch (Exception e) {
            return Response.error("Error fetching count",500);
        }
    }

















}
