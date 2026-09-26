package org.ai.appointmentbackend.service;

import jakarta.mail.MessagingException;
import org.ai.appointmentbackend.dto.PaymentDetails;
import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.enumpack.Role;

public interface EmailService {

     void sendEmail(String to, String subject, String body) throws MessagingException;
    void sendAppointmentConfirmation(AppointmentEntity appointment) throws MessagingException;
    void sendAppointmentComplete(AppointmentEntity appointment) throws MessagingException;
    void sendAppointmentRejection(AppointmentEntity appointment) throws MessagingException;

    void sendPaymentConfirmation(AppointmentEntity appointment, PaymentDetails paymentDetails) throws MessagingException;

    void sendDoctorNotification(AppointmentEntity appointment, PaymentDetails paymentDetails) throws MessagingException;
   void sendAppointmentCancellation(AppointmentEntity appointment, boolean refund, boolean byDoctor) throws MessagingException;


    void sendDoctorRegistraionEmail(UserEntity user, String password) throws MessagingException;

    void sendAdminConfirmationEmail(UserEntity user, Role role) throws MessagingException;

    void sendAdminRegistrationEmail(UserEntity user, String password) throws MessagingException;
}
