package org.ai.appointmentbackend.dto;
import org.ai.appointmentbackend.enumpack.NotificationType;

import java.time.LocalDateTime;

public class NotificationDto {

    private Long id;
    private String message;
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean isRead = false;
    private UserDto userDto;
    private NotificationType type;
    private Long relatedId;

    public NotificationDto() {
    }

    public NotificationDto(Long id, String message, LocalDateTime createdAt, boolean isRead, UserDto userDto, NotificationType type, Long relatedId) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.userDto = userDto;
        this.type = type;
        this.relatedId = relatedId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }
}
