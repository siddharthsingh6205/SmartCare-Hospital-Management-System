package org.ai.appointmentbackend.dto;

public class SystemHealth {
    private Integer activeUsers;
    private Double appointmentRate;

    public Integer getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Integer activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Double getAppointmentRate() {
        return appointmentRate;
    }

    public void setAppointmentRate(Double appointmentRate) {
        this.appointmentRate = appointmentRate;
    }
}
