package org.ai.appointmentbackend.mapper;

import org.ai.appointmentbackend.dto.*;
import org.ai.appointmentbackend.entity.*;
import org.ai.appointmentbackend.enumpack.AppointmentStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public  class  DtoConverter {


    public static UserDto convertUserEntityToUserDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPassword(userEntity.getPassword());
        userDto.setRole(userEntity.getRole());
        userDto.setName(userEntity.getName());
        if (userEntity.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(userEntity.getImage());
            userDto.setImage(Base64.getDecoder().decode(base64Image)); // Store base64 in the DTO for frontend rendering
        }

        return userDto;
    }

    public static List<UserDto> convertUserEntityListToUserDtoList(List<UserEntity> UserEntityList) {
        return UserEntityList.stream().map(DtoConverter::convertUserEntityToUserDto).collect(Collectors.toList());
    }
    public static DoctorDto convertDoctorEntityToDoctorDto(DoctorEntity savedDoctor) {
        if (savedDoctor == null) return null;

        DoctorDto dto = new DoctorDto();
        // Set primitive fields
        dto.setId(savedDoctor.getId());
        dto.setSpecialization(savedDoctor.getSpecialization());
        dto.setContactNumber(savedDoctor.getContactNumber());
        dto.setAvailability(savedDoctor.getAvailability());
        dto.setExperience(savedDoctor.getExperience());
        dto.setDegree(savedDoctor.getDegree()); // Note: case sensitivity might matter
        dto.setFees(savedDoctor.getFees());
        dto.setAboutDoctor(savedDoctor.getAboutDoctor());

        // Convert User (only basic info)
        if (savedDoctor.getUser() != null) {
            dto.setUser(convertUserEntityToUserDto(savedDoctor.getUser()));
        }

        // Handle Address
        if(savedDoctor.getAddress1() != null) {
            AddressDto addressDto = new AddressDto(); // Initialize AddressDto
            addressDto.setLine1(savedDoctor.getAddress1().getLine1());
            addressDto.setLine2(savedDoctor.getAddress1().getLine2());
            dto.setAddress(addressDto); // Set the initialized AddressDto
        }

     if(savedDoctor.getAvailableSlots()!= null) {
         dto.setAvailableSlots(savedDoctor.getAvailableSlots());
     }
     if(savedDoctor.getApprovedSlots()!= null) {
         dto.setApprovedSlots(savedDoctor.getApprovedSlots());
     }
     if(savedDoctor.getPendingSlots()!= null) {
         dto.setPendingSlots(savedDoctor.getPendingSlots());
     }

        return dto;
    }

    public static List<DoctorDto> convertDoctorEntityListToDoctorDtoList(List<DoctorEntity> doctorEntityList) {
        return doctorEntityList.stream().map(DtoConverter::convertDoctorEntityToDoctorDto).collect(Collectors.toList());
    }



















    public static PatientDto convertPatientEntityToPatientDto(PatientEntity patientEntity) {

        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientEntity.getId());
        patientDto.setAge(patientEntity.getAge());
        patientDto.setGender(patientEntity.getGender());
        if (patientEntity.getUser() != null) {
            UserEntity userEntity = patientEntity.getUser();
            UserDto userDto = convertUserEntityToUserDto(userEntity);
            patientDto.setUser(userDto);
        }
         patientDto.setDob(patientEntity.getDob());
        patientDto.setAddress(patientEntity.getAddress());

        patientDto.setContactNumber(String.valueOf(patientEntity.getContactNumber()));
        patientDto.setMedicalHistory(patientEntity.getMedicalHistory());
        patientDto.setCreatedAt(patientEntity.getCreatedAt());

        return patientDto;



    }

    public static List<PatientDto> convertPatientEntityListToPatientDtoList(List<PatientEntity> patientEntityList) {
        return patientEntityList.stream().map(DtoConverter::convertPatientEntityToPatientDto).collect(Collectors.toList());
    }









    public static AdminDto convertAdminEntityToAdminDto(AdminEntity savedAdmin) {
        if (savedAdmin == null) {
            return null;
        }

        AdminDto adminDto = new AdminDto();
        adminDto.setId(savedAdmin.getId());
        if (savedAdmin.getUser() != null) {
            UserEntity userEntity = savedAdmin.getUser();
            UserDto userDto = convertUserEntityToUserDto(userEntity);
            adminDto.setUser(userDto);
        }
     return adminDto;


    }

    public static List<AdminDto> convertAdminEntityListToAdminDtoList(List<AdminEntity> adminDtoList) {
        return adminDtoList.stream().map(DtoConverter::convertAdminEntityToAdminDto).collect(Collectors.toList());
    }


    public static AppointmentDto convertAppointmentEntityToAppointmentDto(AppointmentEntity appointmentEntity) {
        if (appointmentEntity == null) {
            return null;
        }

        AppointmentDto appointmentDto = new AppointmentDto();
        // Mapping fields
        appointmentDto.setId(appointmentEntity.getId());
        appointmentDto.setDate(appointmentEntity.getDate());
        appointmentDto.setTime(appointmentEntity.getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        appointmentDto.setSlotDate(appointmentEntity.getDate().format(formatter));
        appointmentDto.setAppointmentStatus(appointmentEntity.getAppointmentStatus());
        appointmentDto.setCancelled(appointmentEntity.getAppointmentStatus() == AppointmentStatus.CANCELLED);
        appointmentDto.setCompleted(appointmentEntity.getAppointmentStatus() == AppointmentStatus.COMPLETED);
        appointmentDto.setImage(appointmentEntity.getDoctor().getUser().getImage());
        appointmentDto.setAge(appointmentEntity.getPatient().getAge());
        appointmentDto.setAmount(appointmentEntity.getAmount());
        appointmentDto.setPayment(appointmentEntity.isPayment());
        appointmentDto.setApprovalStatus(appointmentEntity.getApprovalStatus());
        appointmentDto.setPatientImage(appointmentEntity.getPatient().getUser().getImage());
        // Mapping Patient details
        if (appointmentEntity.getPatient() != null) {
            appointmentDto.setPatientId(appointmentEntity.getPatient().getId());
            appointmentDto.setPatientName(appointmentEntity.getPatient().getUser().getName());  // Assuming PatientEntity has a User with a name
        }

        // Mapping Doctor details
        if (appointmentEntity.getDoctor() != null) {
            appointmentDto.setDoctorId(appointmentEntity.getDoctor().getId());
            appointmentDto.setDoctorName(appointmentEntity.getDoctor().getUser().getName());  // Assuming DoctorEntity has a User with a name
        }

        return appointmentDto;
    }
    public static List<AppointmentDto> convertAppointmentEntityListToAppointmentDtoList(List<AppointmentEntity> appointmentEntityList) {
        return appointmentEntityList.stream().map(DtoConverter::convertAppointmentEntityToAppointmentDto).collect(Collectors.toList());
    }

    public static NotificationDto convertNotificationEntityToNotificationDto(NotificationEntity notificationEntity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notificationEntity.getId());
        notificationDto.setMessage(notificationEntity.getMessage());
        notificationDto.setCreatedAt(notificationEntity.getCreatedAt());
        notificationDto.setUserDto(convertUserEntityToUserDto(notificationEntity.getUser()));
        notificationDto.setRead(notificationEntity.isRead());
        notificationDto.setType(notificationEntity.getType());
        notificationDto.setRelatedId(notificationEntity.getRelatedId());
        return notificationDto;

    }

    public static List<NotificationDto> convertNotificationEntityListToNotificationDtoList(List<NotificationEntity> notificationEntityList) {
        return notificationEntityList.stream().map(DtoConverter::convertNotificationEntityToNotificationDto).collect(Collectors.toList());
    }
}
