package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.Response;
import org.springframework.stereotype.Service;

@Service
public interface DoctoDashBoardService {

     Response getDoctorDashBoard(String email);


}
