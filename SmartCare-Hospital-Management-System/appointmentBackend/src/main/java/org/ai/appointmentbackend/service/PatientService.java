package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.PatientEntity;

public interface PatientService {

    Response getAllPatients();
    Response getPatientById(Long id);
    Response updatePatient(Long id,PatientEntity patientEntity);


}
