package org.ai.appointmentbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ai.appointmentbackend.entity.AddressEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private Long id;
    private String specialization;
    private String contactNumber;
    private String availability;
    private String experience;
    private String degree;
    private Long fees;
    private String aboutDoctor;
    private AddressDto address;
    private UserDto user;
    private Map<String, Set<String>> availableSlots;
    private Map<String, Set<String>> pendingSlots = new HashMap<>();
    private Map<String, Set<String>> approvedSlots = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Long getFees() {
        return fees;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }

    public String getAboutDoctor() {
        return aboutDoctor;
    }

    public void setAboutDoctor(String aboutDoctor) {
        this.aboutDoctor = aboutDoctor;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
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
}
