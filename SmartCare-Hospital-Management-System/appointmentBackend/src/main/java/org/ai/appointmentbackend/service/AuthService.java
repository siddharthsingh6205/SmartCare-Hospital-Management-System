package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.*;
import org.ai.appointmentbackend.request.LoginRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AuthService {

    Response RegisterPatient(PatientEntity patient, MultipartFile imageFile);
    Response LoginUser( LoginRequest loginRequest);
    Response RegisterDoctor( DoctorEntity doctor,MultipartFile imageFile);
    Response RegisterAdmin( AdminEntity admin,MultipartFile imageFile);


}
