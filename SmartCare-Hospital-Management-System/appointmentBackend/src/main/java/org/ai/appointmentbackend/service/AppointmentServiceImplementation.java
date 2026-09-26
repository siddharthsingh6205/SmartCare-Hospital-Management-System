package org.ai.appointmentbackend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ai.appointmentbackend.configuration.JwtTokenProvider;
import org.ai.appointmentbackend.dto.AppointmentDto;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.entity.DoctorEntity;
import org.ai.appointmentbackend.entity.PatientEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.enumpack.AppointmentStatus;
import org.ai.appointmentbackend.enumpack.ApprovalStatus;
import org.ai.appointmentbackend.enumpack.NotificationType;
import org.ai.appointmentbackend.mapper.DtoConverter;
import org.ai.appointmentbackend.repository.AppointmentRepository;
import org.ai.appointmentbackend.repository.DoctorRepository;
import org.ai.appointmentbackend.repository.PatientRepository;
import org.ai.appointmentbackend.repository.UserRepository;
import org.ai.appointmentbackend.request.AppointmentRequest;
import org.ai.appointmentbackend.request.RescheduleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class AppointmentServiceImplementation implements AppointmentService{
    private  AppointmentRepository appointmentRepository;
    private  PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private NotificationService notificationService;
    private EmailService emailService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AppointmentServiceImplementation(AppointmentRepository appointmentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, NotificationService notificationService, EmailService emailService, JwtTokenProvider jwtTokenProvider) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AppointmentServiceImplementation() {
    }

    private String extractUserNameFromToken(String token) {
        token=token.substring(7);
        return jwtTokenProvider.extractUsername(token);
    }


    //Completed appointment
    @Override
    public Response completeAppointment(Long id,Long doctorId) {


        try {
            Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(id);


            if (optionalAppointment.isPresent()) {
                AppointmentEntity appointment = optionalAppointment.get();


                if (!appointment.getDoctor().getId().equals(doctorId)) {
                    return Response.error("You are not authorized to reject this appointment",400);
                }

                appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
                appointment.setCompleted(true);
                appointment=appointmentRepository.save(appointment);

                // Free up the slot
                String slotDate = appointment.getDate().toString();
                String slotTime = appointment.getTime().toString();
                removeFromApprovedSlots(appointment.getDoctor(), slotDate, slotTime);

                emailService.sendAppointmentComplete(appointment);

                // Notify patient
                notificationService.createNotification(
                        appointment.getPatient().getUser(),
                        "Your appointment with Dr. " + appointment.getDoctor().getUser().getName() + " has been completed",
                        NotificationType.APPOINTMENT_COMPLETED,
                        appointment.getId()
                );


                return Response.success(" appointment has been successfully completed").withAppointment((DtoConverter.convertAppointmentEntityToAppointmentDto(appointment)));

            } else {

                return Response.error("We couldn't find the appointment you're trying to cancel",404);
            }
        } catch (Exception e) {

            return Response.error("We couldn't process your cancellation request. Please try again later.",404);
        }

    }

    private void removeFromApprovedSlots(DoctorEntity doctor, String slotDate, String slotTime) {
        if (doctor.getApprovedSlots() != null &&
                doctor.getApprovedSlots().containsKey(slotDate)) {

            doctor.getApprovedSlots().get(slotDate).remove(slotTime);

            // Remove date entry if no more approved slots
            if (doctor.getApprovedSlots().get(slotDate).isEmpty()) {
                doctor.getApprovedSlots().remove(slotDate);
            }

            doctorRepository.save(doctor);
        }
    }



    //approve the appointment
    public Response approveAppointment(Long appointmentId,String token){

        try{
            token=token.substring(7);
            String email=jwtTokenProvider.extractUsername(token);
            DoctorEntity doctor=doctorRepository.findByUserEmail(email);
            AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));




            if (!appointment.getDoctor().getId().equals(doctor.getId())) {

                return Response.error("You are not authorized to approve this appointment",400);

            }

            String slotDate = appointment.getDate().toString();
            String slotTime = appointment.getTime().toString();
            // Move from pending to approved
            moveSlotFromPendingToApproved(appointment.getDoctor(), slotDate, slotTime);

            appointment.setApprovalStatus(ApprovalStatus.APPROVED);
            appointment.setAppointmentApproval(LocalDateTime.now());
            appointment=appointmentRepository.save(appointment);

            // Notify patient
            notificationService.createNotification(
                    appointment.getPatient().getUser(),
                    "Your appointment with Dr. " + appointment.getDoctor().getUser().getName() + " has been approved",
                    NotificationType.APPOINTMENT_APPROVED,
                    appointment.getId()
            );

            // Send confirmation email with payment link
            emailService.sendAppointmentConfirmation(appointment);


            return Response.success("Appointment approved successfully").withAppointment(DtoConverter.convertAppointmentEntityToAppointmentDto(appointment));


        } catch (Exception e) {

            return Response.error("Failed to approve appointment: " + e.getMessage(),400);
        }

    }

    private void moveSlotFromPendingToApproved(DoctorEntity doctor, String slotDate, String slotTime) {
        // Remove from pending
        if (doctor.getPendingSlots() != null &&
                doctor.getPendingSlots().containsKey(slotDate)) {

            doctor.getPendingSlots().get(slotDate).remove(slotTime);

            // Remove date entry if no more pending slots
            if (doctor.getPendingSlots().get(slotDate).isEmpty()) {
                doctor.getPendingSlots().remove(slotDate);
            }
        }

        // Add to approved
        Map<String, Set<String>> approvedSlots = doctor.getApprovedSlots();
        if (approvedSlots == null) {
            approvedSlots = new HashMap<>();
        }

        if (!approvedSlots.containsKey(slotDate)) {
            approvedSlots.put(slotDate, new HashSet<>());
        }

        approvedSlots.get(slotDate).add(slotTime);
        doctor.setApprovedSlots(approvedSlots);

        doctorRepository.save(doctor);
    }

    //reject appointment
    @Override
    public Response rejectAppointment(Long appointmentId, String token) {
        try {

            token=token.substring(7);
            String email=jwtTokenProvider.extractUsername(token);
            DoctorEntity doctor=doctorRepository.findByUserEmail(email);


            AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));




            if (!appointment.getDoctor().getId().equals(doctor.getId())) {
              return Response.error("You are not authorized to reject this appointment",400);
            }

            String slotDate = appointment.getDate().toString();
            String slotTime = appointment.getTime().toString();

            // Remove from pending and make available again
            removeFromPendingAndMakeAvailable(appointment.getDoctor(), slotDate, slotTime);

            appointment.setApprovalStatus(ApprovalStatus.REJECTED);
            appointment.setAppointmentApproval(LocalDateTime.now());
            appointment=appointmentRepository.save(appointment);

            emailService.sendAppointmentRejection(appointment);

            // Notify patient
            notificationService.createNotification(
                    appointment.getPatient().getUser(),
                    "Your appointment with Dr. " + appointment.getDoctor().getUser().getName() + " has been rejected",
                    NotificationType.APPOINTMENT_REJECTED,
                    appointment.getId()
            );


            return Response.success("Appointment rejected successfully").withAppointment(DtoConverter.convertAppointmentEntityToAppointmentDto(appointment));

        } catch (Exception e) {

            return Response.error("Failed to reject appointment: " + e.getMessage(),500);
        }

    }


    private void removeFromPendingAndMakeAvailable(DoctorEntity doctor, String slotDate, String slotTime) {
        // Remove from pending
        if (doctor.getPendingSlots() != null &&
                doctor.getPendingSlots().containsKey(slotDate)) {

            doctor.getPendingSlots().get(slotDate).remove(slotTime);

            // Remove date entry if no more pending slots
            if (doctor.getPendingSlots().get(slotDate).isEmpty()) {
                doctor.getPendingSlots().remove(slotDate);
            }
        }

        // Add back to available
        Map<String, Set<String>> availableSlots = doctor.getAvailableSlots();
        if (availableSlots == null) {
            availableSlots = new HashMap<>();
        }

        if (!availableSlots.containsKey(slotDate)) {
            availableSlots.put(slotDate, new HashSet<>());
        }

        availableSlots.get(slotDate).add(slotTime);
        doctor.setAvailableSlots(availableSlots);

        doctorRepository.save(doctor);
    }



    //Book Appointment related Functionalities
    @Override
    public Response bookAppointment(AppointmentRequest appointmentRequest,String token) {


        try {

            String email=extractUserNameFromToken(token);
            PatientEntity patient=patientRepository.findByUserEmail(email);
            if(patient==null) {
                return Response.error("Patient not found",404);

            }
            if (appointmentRequest.getDate() == null || appointmentRequest.getTime() == null) {

                return Response.error("Please select both appointment date and time",404);
            }


            // Fetch doctor and patient entities
            DoctorEntity doctor = doctorRepository.findById(appointmentRequest.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            String slotDate = String.valueOf(appointmentRequest.getDate());  // format: "day_month_year"
            String slotTime = String.valueOf(appointmentRequest.getTime());


            // Check if slot is available
            if (!isSlotAvailable(doctor, slotDate, slotTime)) {

                return Response.error("This time slot is not available",400);

            }


            AppointmentEntity appointmentEntity = new AppointmentEntity();
            appointmentEntity.setPatient(patient);
            appointmentEntity.setDoctor(doctor);
            appointmentEntity.setDate(appointmentRequest.getDate());
            appointmentEntity.setTime(appointmentRequest.getTime());
            appointmentEntity.setAppointmentStatus(AppointmentStatus.SCHEDULED);
            appointmentEntity.setAmount(doctor.getFees());
            appointmentEntity.setApprovalStatus(ApprovalStatus.PENDING);
            appointmentEntity.setPayment(false);

            AppointmentEntity savedAppointment = appointmentRepository.save(appointmentEntity);

            System.out.println(savedAppointment);

            // Add to pending slots
            addToPendingSlots(doctor, slotDate, slotTime);

            // Notify doctor
            notificationService.createNotification(
                    doctor.getUser(),
                    "New appointment request from " + patient.getUser().getName(),
          NotificationType.APPOINTMENT_REQUEST,
                    savedAppointment.getId()
            );

            return Response.success("Appointment request submitted. Waiting for doctor confirmation.").withAppointment(DtoConverter.convertAppointmentEntityToAppointmentDto(savedAppointment));



        } catch (Exception e) {

           return Response.error(e.getMessage(),404);

        }

    }

    private boolean isSlotAvailable(DoctorEntity doctor, String slotDate, String slotTime) {
        // Check if the slot exists in available slots and not in pending/approved
        return doctor.getAvailableSlots() != null &&
                doctor.getAvailableSlots().containsKey(slotDate) &&
                doctor.getAvailableSlots().get(slotDate).contains(slotTime) &&
                !isSlotPending(doctor, slotDate, slotTime) &&
                !isSlotApproved(doctor, slotDate, slotTime);
    }

    private boolean isSlotPending(DoctorEntity doctor, String slotDate, String slotTime) {
        return doctor.getPendingSlots() != null &&
                doctor.getPendingSlots().containsKey(slotDate) &&
                doctor.getPendingSlots().get(slotDate).contains(slotTime);
    }

    private boolean isSlotApproved(DoctorEntity doctor, String slotDate, String slotTime) {
        return doctor.getApprovedSlots() != null &&
                doctor.getApprovedSlots().containsKey(slotDate) &&
                doctor.getApprovedSlots().get(slotDate).contains(slotTime);
    }

    private void addToPendingSlots(DoctorEntity doctor, String slotDate, String slotTime) {
        Map<String, Set<String>> pendingSlots = doctor.getPendingSlots();
        if (pendingSlots == null) {
            pendingSlots = new HashMap<>();
        }

        if (!pendingSlots.containsKey(slotDate)) {
            pendingSlots.put(slotDate, new HashSet<>());
        }

        pendingSlots.get(slotDate).add(slotTime);
        doctor.setPendingSlots(pendingSlots);

        // Remove from available slots
        removeFromAvailableSlots(doctor, slotDate, slotTime);

        doctorRepository.save(doctor);
    }


//CancelAppointment
@Override
public Response cancelAppointment(Long appointmentId, String token) {
    try {
        token = token.substring(7);
        String email = jwtTokenProvider.extractUsername(token);

        Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            return Response.error("We couldn't find the appointment you're trying to cancel", 404);
        }

        AppointmentEntity appointment = optionalAppointment.get();

        // Check if user is either the patient or the doctor of this appointment
        PatientEntity patient = patientRepository.findByUserEmail(email);
        DoctorEntity doctor = doctorRepository.findByUserEmail(email);

        boolean isPatient = patient != null && appointment.getPatient().getId().equals(patient.getId());
        boolean isDoctor = doctor != null && appointment.getDoctor().getId().equals(doctor.getId());

        if (!isPatient && !isDoctor) {
            return Response.error("Unauthorized to cancel this appointment", 403);
        }

        // Check if already cancelled
        if (appointment.getAppointmentStatus() == AppointmentStatus.CANCELLED) {
            return Response.error("This appointment was already cancelled", 400);
        }

        // Process refund if payment was made and appointment is in future
        boolean refundInitiated = false;
        if (appointment.isPayment() && isTimeSlotInFuture(appointment.getDate(), appointment.getTime())) {
            refundInitiated =true;
            if (refundInitiated) {
                appointment.setPayment(false);
            }
        }





        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointment = appointmentRepository.save(appointment);

        // Free up the slot if it was approved
        if (appointment.getApprovalStatus() == ApprovalStatus.APPROVED) {
            String slotDate = appointment.getDate().toString();
            String slotTime = appointment.getTime().toString();
            removeFromApprovedSlots(appointment.getDoctor(), slotDate, slotTime);

            // Add back to available slots if not past the appointment time
            if (isTimeSlotInFuture(appointment.getDate(), appointment.getTime())) {
                Map<String, Set<String>> availableSlots = appointment.getDoctor().getAvailableSlots();
                if (availableSlots == null) {
                    availableSlots = new HashMap<>();
                }

                if (!availableSlots.containsKey(slotDate)) {
                    availableSlots.put(slotDate, new HashSet<>());
                }

                availableSlots.get(slotDate).add(slotTime);
                appointment.getDoctor().setAvailableSlots(availableSlots);
                doctorRepository.save(appointment.getDoctor());
            }
        }
        // Send cancellation emails
        boolean cancelledByPatient = isPatient;
        emailService.sendAppointmentCancellation(appointment, refundInitiated, !cancelledByPatient);


        // Notify both parties
        String cancelerName = isPatient ? "You" : "Dr. " + doctor.getUser().getName();
        String messageForPatient = "Your appointment with Dr. " + appointment.getDoctor().getUser().getName() +
                " has been cancelled by " + (isPatient ? "you" : "the doctor");
        String messageForDoctor = "Your appointment with " + appointment.getPatient().getUser().getName() +
                " has been cancelled by " + (isDoctor ? "you" : "the patient");

        notificationService.createNotification(
                appointment.getPatient().getUser(),
                messageForPatient,
                NotificationType.APPOINTMENT_CANCELLED,
                appointment.getId()
        );

        notificationService.createNotification(
                appointment.getDoctor().getUser(),
                messageForDoctor,
                NotificationType.APPOINTMENT_CANCELLED,
                appointment.getId()
        );

        // Send appropriate emails with refund status


        String successMessage = "Appointment cancelled successfully";
        if (refundInitiated) {
            successMessage += ". Refund will be processed within 5-7 business days.";
        }

        return Response.success(successMessage)
                .withAppointment(DtoConverter.convertAppointmentEntityToAppointmentDto(appointment));





    } catch (Exception e) {
        return Response.error("We couldn't process your cancellation request. Please try again later.", 500);
    }
}

    private boolean processRefund(AppointmentEntity appointment) {

        try {

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTimeSlotInFuture(LocalDate date, LocalTime time) {
        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
        return appointmentDateTime.isAfter(LocalDateTime.now());
    }

    private void removeFromAvailableSlots(DoctorEntity doctor, String slotDate, String slotTime) {
        if (doctor.getAvailableSlots() != null &&
                doctor.getAvailableSlots().containsKey(slotDate)) {

            doctor.getAvailableSlots().get(slotDate).remove(slotTime);

            // Remove date entry if no more slots
            if (doctor.getAvailableSlots().get(slotDate).isEmpty()) {
                doctor.getAvailableSlots().remove(slotDate);
            }
        }
    }




    @Override
    public Response rescheduleAppointment( RescheduleRequest rescheduleRequest,Long appointmentId,String token) {

        try {
            token=token.substring(7);
            String email=jwtTokenProvider.extractUsername(token);
            PatientEntity patient=patientRepository.findByUserEmail(email);
            if(patient==null) {
                return Response.error("Patient not found",404);
            }

            AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));

            if (!appointment.getPatient().getId().equals(patient.getId())) {

                return Response.error("Unauthorized to reschedule this appointment",400);

            }

            // Validate new slot
            DoctorEntity doctor = appointment.getDoctor();
            String newSlotDate = rescheduleRequest.getNewDate().toString();
            String newSlotTime = rescheduleRequest.getNewTime().toString();

            if (!isSlotAvailable(doctor, newSlotDate, newSlotTime)) {

                return Response.error("The requested time slot is not available",400);

            }

            // Free up old slot if approved
            if (appointment.getApprovalStatus() == ApprovalStatus.APPROVED) {
                String oldSlotDate = appointment.getDate().toString();
                String oldSlotTime = appointment.getTime().toString();

                doctor.getApprovedSlots().get(oldSlotDate).remove(oldSlotTime);
                if (doctor.getApprovedSlots().get(oldSlotDate).isEmpty()) {
                    doctor.getApprovedSlots().remove(oldSlotDate);
                }

                if (appointment.getDate().isAfter(LocalDate.now()) ||
                        (appointment.getDate().isEqual(LocalDate.now()) &&
                                appointment.getTime().isAfter(LocalTime.now()))) {
                    doctor.getAvailableSlots().computeIfAbsent(oldSlotDate, k -> new HashSet<>()).add(oldSlotTime);
                }
            }

            // Update appointment
            appointment.setDate(rescheduleRequest.getNewDate());
            appointment.setTime(rescheduleRequest.getNewTime());
            appointment.setApprovalStatus(ApprovalStatus.PENDING);
            appointmentRepository.save(appointment);

            // Mark new slot as pending
            doctor.getPendingSlots().computeIfAbsent(newSlotDate, k -> new HashSet<>()).add(newSlotTime);
            doctor.getAvailableSlots().get(newSlotDate).remove(newSlotTime);
            if (doctor.getAvailableSlots().get(newSlotDate).isEmpty()) {
                doctor.getAvailableSlots().remove(newSlotDate);
            }
            doctorRepository.save(doctor);

            // Notify doctor
            notificationService.createNotification(
                    doctor.getUser(),
                    appointment.getPatient().getUser().getName() + " has requested to reschedule their appointment",
                    NotificationType.APPOINTMENT_RESCHEDULE,
                    appointment.getId()
            );


            return Response.success("Appointment rescheduled successfully. Waiting for doctor confirmation.").withAppointment(DtoConverter.convertAppointmentEntityToAppointmentDto(appointment));


        } catch (Exception e) {

            return Response.error("Failed to reschedule appointment: " + e.getMessage(),400);
        }

    }

    @Override
    public Response getAppointmentsForPatient(String token) {
        try {
            String email=extractUserNameFromToken(token);
            PatientEntity patient=patientRepository.findByUserEmail(email);
            if(patient==null) {
                return Response.error("Patient not found",404);
            }

            List<AppointmentEntity> appointments = appointmentRepository.findByPatientId(patient.getId());

            if (appointments.isEmpty()) {

                return Response.success("You don't have any upcoming appointments");

            }


            return Response.success("Your appointments were retrieved successfully").withAppointmentList(DtoConverter.convertAppointmentEntityListToAppointmentDtoList(appointments));


        } catch (Exception e) {

            return Response.error (e.getMessage(),500);
        }

    }


    @Override
    public Response getAvailableSlots(Long doctorId, String date) {

        try {
            DoctorEntity doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            Map<String, Set<String>> availableSlots = new HashMap<>();

            if (date != null) {
                Set<String> slots = doctor.getAvailableSlots().getOrDefault(date, Collections.emptySet());
                if (!slots.isEmpty()) {
                    availableSlots.put(date, slots);
                }
            } else {
                availableSlots.putAll(doctor.getAvailableSlots());
            }


            return Response.success("Available slots retrieved").withDoctor(DtoConverter.convertDoctorEntityToDoctorDto(doctor));


        } catch (Exception e) {

            return Response.error("Failed to get available slots: " + e.getMessage(),500);
        }

    }




    @Override
    public Response getAppointmentsForDoctor(String email) {

        try {
            DoctorEntity doctor=doctorRepository.findByUserEmail(email);

            List<AppointmentEntity> appointments = appointmentRepository.findByDoctorId(doctor.getId());

            if (appointments.isEmpty()) {

                return Response.success("You don't have any scheduled appointments").withAppointmentList(Collections.emptyList());

            }


            return Response.success("Your patient appointments were retrieved successfully").withAppointmentList(DtoConverter.convertAppointmentEntityListToAppointmentDtoList(appointments));

        } catch (Exception e) {

            return Response.error("We're having trouble retrieving your schedule. Please try again later.",500);
        }

    }


    @Override
    public Response getAllAppointments() {
        Response response = new Response();
        try {
            List<AppointmentEntity> appointments = appointmentRepository.findAll();

            if (appointments.isEmpty()) {
                return Response.success("You don't have any  appointments").withAppointmentList(Collections.emptyList());

            }
            return Response.success("Your patient appointments were retrieved successfully").withAppointmentList(DtoConverter.convertAppointmentEntityListToAppointmentDtoList(appointments));
        } catch (Exception e) {
            return Response.error("We're having trouble retrieving your schedule. Please try again later.",500);
        }

    }

    @Override
    public AppointmentEntity getAppointmentById(Long id) {
        Optional<AppointmentEntity> appointment = appointmentRepository.findById(id);
        return appointment.orElse(null);

    }




}
