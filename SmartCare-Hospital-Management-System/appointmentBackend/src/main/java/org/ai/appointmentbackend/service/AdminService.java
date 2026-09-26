package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.Response;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public interface AdminService {

    Response setDoctorAvailability(Long doctorId, Map<String, Set<String>> weeklySlots);
    Response getDoctorAvailability(Long doctorId);

}
