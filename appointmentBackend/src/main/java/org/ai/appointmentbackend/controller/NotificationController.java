package org.ai.appointmentbackend.controller;

import org.ai.appointmentbackend.configuration.JwtTokenProvider;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.repository.UserRepository;
import org.ai.appointmentbackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Response> getUnreadNotifications(
            @RequestHeader("Authorization") String token) {
        token=token.substring(7);
        String email=jwtTokenProvider.extractUsername(token);
        UserEntity user=userRepository.findByEmail(email);

        Response response = notificationService.getUnreadNotifications(user.getId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Response> markAsRead(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        Response response = notificationService.markAsRead(id, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Response> getNotificationCount(
            @RequestHeader("Authorization") String token) {
        Response response = notificationService.getNotificationCount(token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }






}
