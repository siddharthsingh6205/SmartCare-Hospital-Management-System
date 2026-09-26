package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.DoctorEntity;
import org.ai.appointmentbackend.enumpack.NotificationType;
import org.ai.appointmentbackend.mapper.DtoConverter;
import org.ai.appointmentbackend.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AdminServiceImplementation implements AdminService{
    private DoctorRepository doctorRepository;
    private NotificationService notificationService;

    @Autowired
    public AdminServiceImplementation(DoctorRepository doctorRepository, NotificationService notificationService) {
        this.doctorRepository = doctorRepository;
        this.notificationService = notificationService;
    }

    public AdminServiceImplementation() {
    }

    @Override
    public Response setDoctorAvailability(Long doctorId, Map<String, Set<String>> weeklySlots) {
        try {
            DoctorEntity doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            // Validate slots (9am to 6pm, proper intervals, etc.)
            if (!validateSlots(weeklySlots)) {
                return Response.error("Invalid time slots provided",400);
            }


            doctor.setAvailableSlots(weeklySlots);
            doctor=doctorRepository.save(doctor);

            // Notify doctor
            notificationService.createNotification(
                    doctor.getUser(),
                    "Your weekly availability has been updated by admin",
                    NotificationType.OTHER,
                    doctorId
            );

          return Response.success("Doctor availability has been updated").withDoctor(DtoConverter.convertDoctorEntityToDoctorDto(doctor));

        } catch (Exception e) {

            return Response.error("Failed to update doctor availability: " + e.getMessage(),400);
        }

    }

    private boolean validateSlots(Map<String, Set<String>> slots) {
        if (slots == null || slots.isEmpty()) {
            return false;
        }



        // Define the valid time range (9 AM to 6 PM)
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        // Define valid time interval (30 minutes)
        int validIntervalMinutes = 30;

        // Maximum days in advance (e.g., 60 days)
        int maxDaysInAdvance = 60;

        LocalDate today = LocalDate.now();

        //one daya many time
        for (Map.Entry<String, Set<String>> entry : slots.entrySet()) {
            String dateStr = entry.getKey();
            Set<String> timeSlots = entry.getValue();

            // Validate date format (YYYY-MM-DD) and that it's not in the past
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);

                // Check if date is in the past
                if (date.isBefore(today)) {
                    return false;
                }

                // Check if date is too far in the future
                if (date.isAfter(today.plusDays(maxDaysInAdvance))) {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }

            if (timeSlots == null || timeSlots.isEmpty()) {
                return false;
            }

            // Check for duplicate times
            if (timeSlots.size() != new HashSet<>(timeSlots).size()) {
                return false;
            }

            for (String timeStr : timeSlots) {
                try {
                    // Parse the time string (expected format HH:mm)
                    LocalTime time = LocalTime.parse(timeStr);

                    // Check basic time format
                    if (!timeStr.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                        return false;
                    }

                    // Check if time is within working hours (9 AM - 6 PM)
                    if (time.isBefore(startTime) || !time.isBefore(endTime)) {
                        return false;
                    }

                    // Check if time is in 30-minute intervals
                    if (time.getMinute() % validIntervalMinutes != 0) {
                        return false;
                    }

                    // Check if seconds are zero (shouldn't be included)
                    if (time.getSecond() != 0) {
                        return false;
                    }

                } catch (Exception e) {
                    return false;
                }
            }
        }

        return true;
    }


    @Override
    public Response getDoctorAvailability(Long doctorId) {

        try {
            DoctorEntity doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

      return Response.success("Doctor availability successfully retrieved").withDoctor(DtoConverter.convertDoctorEntityToDoctorDto(doctor));

        } catch (Exception e) {

            return Response.error("Failed to get doctor availability: " + e.getMessage(),400);
        }

    }
}
