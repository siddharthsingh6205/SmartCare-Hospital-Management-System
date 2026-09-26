package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.DoctorEntity;

import java.util.Map;
import java.util.Set;

public interface DoctorService {

    Response fetchAllDoctors();
    Response updateDoctor(String email, DoctorEntity doctorEntity);
    Response getDoctorsBySpecialization(String specialization);

    Response isAuthHeader(String authHeader);
    Response fetchDoctorById(Long doctorId);
    Response getDoctor(String email);

    Response getAppointmentRequests(String token);


    Response getDoctorSchedule(String token);

    Response getPendingDoctorAppointments(String token);
}
