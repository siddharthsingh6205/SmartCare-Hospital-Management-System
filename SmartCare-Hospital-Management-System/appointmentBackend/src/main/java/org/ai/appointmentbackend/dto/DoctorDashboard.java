package org.ai.appointmentbackend.dto;


import java.util.List;
import java.util.Map;
import java.util.Set;

public class DoctorDashboard {

    private Integer appointments;
    private Integer completedAppointments;
    private Integer cancelledAppointments;
    private Integer pendingAppointments;
    private Integer patients;
    private Long earnings;
    private Integer availableSlotsCount;
    private Integer pendingSlotsCount;
    private Integer approvedSlotsCount;
    private Map<String, Set<String>> availableSlots;
    private Map<String, Set<String>> pendingSlots;
    private Map<String, Set<String>> approvedSlots;
    private List<AppointmentDto> latestAppointments;
    private Integer todayApproved;
    private Integer todayRejected;
    private Integer todayPending;
    private Integer todayBookedSlots;
    private List<Long> weeklyEarnings;



    public Long getEarnings() {
        return earnings;
    }

    public void setEarnings(Long earnings) {
        this.earnings = earnings;
    }

    public Integer getAppointments() {
        return appointments;
    }

    public void setAppointments(Integer appointments) {
        this.appointments = appointments;
    }

    public Integer getPatients() {
        return patients;
    }

    public void setPatients(Integer patients) {
        this.patients = patients;
    }

    public Integer getCompletedAppointments() {
        return completedAppointments;
    }

    public void setCompletedAppointments(Integer completedAppointments) {
        this.completedAppointments = completedAppointments;
    }

    public Integer getPendingAppointments() {
        return pendingAppointments;
    }

    public void setPendingAppointments(Integer pendingAppointments) {
        this.pendingAppointments = pendingAppointments;
    }

    public Integer getCancelledAppointments() {
        return cancelledAppointments;
    }

    public void setCancelledAppointments(Integer cancelledAppointments) {
        this.cancelledAppointments = cancelledAppointments;
    }

    public Integer getAvailableSlotsCount() {
        return availableSlotsCount;
    }

    public void setAvailableSlotsCount(Integer availableSlotsCount) {
        this.availableSlotsCount = availableSlotsCount;
    }

    public Integer getPendingSlotsCount() {
        return pendingSlotsCount;
    }

    public void setPendingSlotsCount(Integer pendingSlotsCount) {
        this.pendingSlotsCount = pendingSlotsCount;
    }

    public Integer getApprovedSlotsCount() {
        return approvedSlotsCount;
    }

    public void setApprovedSlotsCount(Integer approvedSlotsCount) {
        this.approvedSlotsCount = approvedSlotsCount;
    }

    public Integer getTodayApproved() {
        return todayApproved;
    }

    public void setTodayApproved(Integer todayApproved) {
        this.todayApproved = todayApproved;
    }

    public Integer getTodayRejected() {
        return todayRejected;
    }

    public void setTodayRejected(Integer todayRejected) {
        this.todayRejected = todayRejected;
    }

    public Integer getTodayPending() {
        return todayPending;
    }

    public void setTodayPending(Integer todayPending) {
        this.todayPending = todayPending;
    }

    public Integer getTodayBookedSlots() {
        return todayBookedSlots;
    }

    public void setTodayBookedSlots(Integer todayBookedSlots) {
        this.todayBookedSlots = todayBookedSlots;
    }

    public List<Long> getWeeklyEarnings() {
        return weeklyEarnings;
    }

    public void setWeeklyEarnings(List<Long> weeklyEarnings) {
        this.weeklyEarnings = weeklyEarnings;
    }

    public Map<String, Set<String>> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Map<String, Set<String>> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Map<String, Set<String>> getPendingSlots() {
        return pendingSlots;
    }

    public void setPendingSlots(Map<String, Set<String>> pendingSlots) {
        this.pendingSlots = pendingSlots;
    }

    public Map<String, Set<String>> getApprovedSlots() {
        return approvedSlots;
    }

    public void setApprovedSlots(Map<String, Set<String>> approvedSlots) {
        this.approvedSlots = approvedSlots;
    }

    public List<AppointmentDto> getLatestAppointments() {
        return latestAppointments;
    }

    public void setLatestAppointments(List<AppointmentDto> latestAppointments) {
        this.latestAppointments = latestAppointments;
    }
}
