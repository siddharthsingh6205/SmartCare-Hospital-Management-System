package org.ai.appointmentbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.ai.appointmentbackend.configuration.JwtTokenProvider;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.request.AppointmentRequest;
import org.ai.appointmentbackend.request.RescheduleRequest;
import org.ai.appointmentbackend.service.AppointmentService;
import org.ai.appointmentbackend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping ("/api")
public class AppointmentController {

    private  AppointmentService appointmentService;
    private  JwtTokenProvider jwtTokenProvider;
    private  PaymentService paymentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, JwtTokenProvider jwtTokenProvider, PaymentService paymentService) {
        this.appointmentService = appointmentService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.paymentService = paymentService;
    }

    public AppointmentController() {
    }



    @PostMapping("/pay")
    public ResponseEntity<Response> createStripeSession(@RequestBody Map<String, String> payload,
                                                        HttpServletRequest request) {

       Response response=paymentService.createStripeSession(payload, request);
       return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PostMapping("/verify")
    public ResponseEntity<Response> verifyStripePayment(@RequestBody Map<String, String> payload) {
        Response response = paymentService.verifyStripePayment(payload);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    // Book a new appointment (Patient only)
    @PostMapping("/appointments")
    public ResponseEntity<Response> bookAppointment(@RequestBody AppointmentRequest appointment,
                                                    @RequestHeader("Authorization") String token
                                                    ) {
        Response response = appointmentService.bookAppointment(appointment,token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Reschedule an appointment (Patient )
    @PutMapping("/appointments/{id}/reschedule")
    public ResponseEntity<Response> rescheduleAppointment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
          @RequestBody RescheduleRequest appointmentDetails) {
        Response response = appointmentService.rescheduleAppointment(appointmentDetails, id,token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Cancel an appointment (Patient or Admin)
    @PostMapping("/appointments/{id}")
    public ResponseEntity<Response> cancelAppointment(@PathVariable Long id,  @RequestHeader("Authorization") String token) {
        Response response = appointmentService.cancelAppointment(id,token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get appointments for a specific patient (Patient )
    @GetMapping("/patients/appointments")
    public ResponseEntity<Response> getPatientAppointments(  @RequestHeader("Authorization") String token) {
        Response response = appointmentService.getAppointmentsForPatient(token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }




    // Get available slots for a doctor
    @GetMapping("/availability/{doctorId}")
    public ResponseEntity<Response> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String date) {

        Response response = appointmentService.getAvailableSlots(doctorId, date);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    // Endpoint to get appointments for a doctor
    @GetMapping("/doctor/appointments")
    public ResponseEntity<Response> getDoctorAppointments(@RequestHeader("Authorization") String authHeader) {
        Response response;

        try {
            // Step 1: Extract the token from the Authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response = new Response();
                response.setStatusCode(400);
                response.setMessage("Token not provided or incorrect format");
                return ResponseEntity.status(response.getStatusCode()).body(response);
            }
            String token = authHeader.substring(7); // Remove the "Bearer " prefix

            // Step 2: Extract the doctor ID from the token
            String email = jwtTokenProvider.extractUsername(token);




            // Step 4: Call the service to fetch the appointments for the doctor
            response = appointmentService.getAppointmentsForDoctor(email);
        } catch (Exception e) {
            response = new Response();
            response.setStatusCode(500);
            response.setMessage("We're having trouble retrieving your schedule. Please try again later.");
        }

        // Return the response
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/appointments")
    public ResponseEntity<Response> getAllAppointments() {
        Response response = appointmentService.getAllAppointments();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }






}
