package org.ai.appointmentbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.*;

@Entity
@Table(name = "doctors")
@AllArgsConstructor
@NoArgsConstructor
public class DoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String specialization;
    private String contactNumber;
    private String availability="unavailable";
    private String experience;
    private String Degree;
    private Long fees;
    private String aboutDoctor;

    @Embedded
    private AddressEntity address1;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;



    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore

    private List<AppointmentEntity> appointments = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "available_slots", joinColumns = @JoinColumn(name = "doctor_id"))
    @MapKeyColumn(name = "slot_date")
    @Column(name = "slot_time")
    @JsonIgnore
    private Map<String, Set<String>> availableSlots = new HashMap<>();

    // Booked slots (pending approval)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "pending_slots", joinColumns = @JoinColumn(name = "doctor_id"))
    @MapKeyColumn(name = "slot_date")
    @Column(name = "slot_time")
    @JsonIgnore
    private Map<String, Set<String>> pendingSlots = new HashMap<>();

    // Approved slots
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "approved_slots", joinColumns = @JoinColumn(name = "doctor_id"))
    @MapKeyColumn(name = "slot_date")
    @Column(name = "slot_time")
    @JsonIgnore
    private Map<String, Set<String>> approvedSlots = new HashMap<>();

    @Transient
    public Map<String, Set<String>> getAvailableSlotsForFrontend() {
        return availableSlots != null ?
                availableSlots :
                Collections.emptyMap();
    }

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
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAboutDoctor() {
        return aboutDoctor;
    }

    public void setAboutDoctor(String aboutDoctor) {
        this.aboutDoctor = aboutDoctor;
    }

    public Long getFees() {
        return fees;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }

    public AddressEntity getAddress1() {
        return address1;
    }

    public void setAddress1(AddressEntity address1) {
        this.address1 = address1;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }

    public Map<String, Set<String>> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Map<String, Set<String>> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Map<String, Set<String>> getApprovedSlots() {
        return approvedSlots;
    }

    public void setApprovedSlots(Map<String, Set<String>> approvedSlots) {
        this.approvedSlots = approvedSlots;
    }

    public Map<String, Set<String>> getPendingSlots() {
        return pendingSlots;
    }

    public void setPendingSlots(Map<String, Set<String>> pendingSlots) {
        this.pendingSlots = pendingSlots;
    }
}
