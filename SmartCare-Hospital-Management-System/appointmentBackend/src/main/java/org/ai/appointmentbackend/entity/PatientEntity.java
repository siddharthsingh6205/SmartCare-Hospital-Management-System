package org.ai.appointmentbackend.entity;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "patients")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private int age;
      private String gender;
      private String contactNumber;
      private String address;
      private String medicalHistory;

      private String dob;

      @CreationTimestamp
      @Column(updatable = false)
      private LocalDateTime createdAt;

      @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
      private List<AppointmentEntity> appointments=new ArrayList<>();

      @OneToOne
      @JoinColumn(name = "user_id",referencedColumnName = "id")
      private UserEntity user;


      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getGender() {
            return gender;
      }

      public void setGender(String gender) {
            this.gender = gender;
      }

      public int getAge() {
            return age;
      }

      public void setAge(int age) {
            this.age = age;
      }

      public String getContactNumber() {
            return contactNumber;
      }

      public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
      }

      public String getAddress() {
            return address;
      }

      public void setAddress(String address) {
            this.address = address;
      }

      public String getMedicalHistory() {
            return medicalHistory;
      }

      public void setMedicalHistory(String medicalHistory) {
            this.medicalHistory = medicalHistory;
      }

      public String getDob() {
            return dob;
      }

      public void setDob(String dob) {
            this.dob = dob;
      }

      public LocalDateTime getCreatedAt() {
            return createdAt;
      }

      public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
      }

      public List<AppointmentEntity> getAppointments() {
            return appointments;
      }

      public void setAppointments(List<AppointmentEntity> appointments) {
            this.appointments = appointments;
      }

      public UserEntity getUser() {
            return user;
      }

      public void setUser(UserEntity user) {
            this.user = user;
      }
}
