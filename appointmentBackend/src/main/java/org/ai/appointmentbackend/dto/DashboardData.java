package org.ai.appointmentbackend.dto;



import java.util.List;
import java.util.Map;


public class DashboardData{
    private Long doctorCount;
    private Long adminCount;
    private Long patientCount;
    private Long appointmentCount;
    private List<AppointmentDto> latestAppointments;
    private int totalRevenue;
    private int todaysAppointments;
    private Map<String, Long> appointmentStatusCount; // PENDING, COMPLETED, CANCELLED
    private Map<String, Long> approvalStatusCount; // APPROVED, PENDING, REJECTED
    private List<DoctorPerformance> topDoctors;
    private List<SpecializationCount> specializationDistribution;
    private PaymentStatus paymentStatus;



    private SystemHealth systemHealth;

    public Long getDoctorCount() {
        return doctorCount;
    }

    public void setDoctorCount(Long doctorCount) {
        this.doctorCount = doctorCount;
    }

    public Long getAdminCount() {
        return adminCount;
    }

    public void setAdminCount(Long adminCount) {
        this.adminCount = adminCount;
    }

    public Long getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(Long patientCount) {
        this.patientCount = patientCount;
    }

    public Long getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(Long appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public List<AppointmentDto> getLatestAppointments() {
        return latestAppointments;
    }

    public void setLatestAppointments(List<AppointmentDto> latestAppointments) {
        this.latestAppointments = latestAppointments;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(int totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTodaysAppointments() {
        return todaysAppointments;
    }

    public void setTodaysAppointments(int todaysAppointments) {
        this.todaysAppointments = todaysAppointments;
    }

    public Map<String, Long> getAppointmentStatusCount() {
        return appointmentStatusCount;
    }

    public void setAppointmentStatusCount(Map<String, Long> appointmentStatusCount) {
        this.appointmentStatusCount = appointmentStatusCount;
    }

    public Map<String, Long> getApprovalStatusCount() {
        return approvalStatusCount;
    }

    public void setApprovalStatusCount(Map<String, Long> approvalStatusCount) {
        this.approvalStatusCount = approvalStatusCount;
    }


    public List<SpecializationCount> getSpecializationDistribution() {
        return specializationDistribution;
    }

    public void setSpecializationDistribution(List<SpecializationCount> specializationDistribution) {
        this.specializationDistribution = specializationDistribution;
    }

    public List<DoctorPerformance> getTopDoctors() {
        return topDoctors;
    }

    public void setTopDoctors(List<DoctorPerformance> topDoctors) {
        this.topDoctors = topDoctors;
    }

    public SystemHealth getSystemHealth() {
        return systemHealth;
    }

    public void setSystemHealth(SystemHealth systemHealth) {
        this.systemHealth = systemHealth;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}