package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.enumpack.AppointmentStatus;
import org.ai.appointmentbackend.request.AppointmentRequest;
import org.ai.appointmentbackend.request.RescheduleRequest;
import org.springframework.stereotype.Service;


@Service
public interface AppointmentService {


    //Doctor related appointment
    Response completeAppointment(Long id,Long doctorId);
    Response approveAppointment(Long appointmentId,String token);
    Response rejectAppointment(Long appointmentId, String token);


    Response bookAppointment(AppointmentRequest appointment,String token);
    Response cancelAppointment(Long appointmentId,String token);
    Response rescheduleAppointment(RescheduleRequest appointment, Long appointmentId,String token);
    Response getAppointmentsForPatient(String token);
    Response getAvailableSlots(Long doctorId, String date);





    Response getAppointmentsForDoctor(String email);
    Response getAllAppointments();
     AppointmentEntity getAppointmentById(Long id);


}
