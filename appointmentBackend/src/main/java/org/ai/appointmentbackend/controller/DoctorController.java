package org.ai.appointmentbackend.controller;
import org.ai.appointmentbackend.configuration.JwtTokenProvider;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.DoctorEntity;
import org.ai.appointmentbackend.repository.DoctorRepository;
import org.ai.appointmentbackend.service.AppointmentService;
import org.ai.appointmentbackend.service.DoctoDashBoardService;
import org.ai.appointmentbackend.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private  DoctorService doctorService;
    private  JwtTokenProvider jwtTokenProvider;
    private  DoctoDashBoardService doctoDashBoardService;
    private AppointmentService appointmentService;
    private DoctorRepository doctorRepository;

@Autowired
    public DoctorController(DoctorService doctorService, JwtTokenProvider jwtTokenProvider, DoctoDashBoardService doctoDashBoardService, AppointmentService appointmentService, DoctorRepository doctorRepository) {
        this.doctorService = doctorService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.doctoDashBoardService = doctoDashBoardService;
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
    }

    public DoctorController() {
    }


    // Public endpoints
    @GetMapping
    public ResponseEntity<Response> getAllDoctors() {
        Response response = doctorService.fetchAllDoctors();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping(params = "specialization")
    public ResponseEntity<Response> getDoctorsBySpecialization(
            @RequestParam String specialization) {
        Response response = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/doctor/update-profile")
    public ResponseEntity<Response> updateDoctor(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody DoctorEntity updatedDoctor) {

        Response response1 = doctorService.isAuthHeader(authHeader);
        String email = jwtTokenProvider.extractUsername(response1.getToken());
        Response response = doctorService.updateDoctor(email, updatedDoctor);
        return ResponseEntity.status(response.getStatusCode()).body(response);


    }

    @GetMapping("/doctor/get-user-profile")
    public ResponseEntity<Response> getDoctorProfile(
            @RequestHeader("Authorization") String authHeader
           ) {

        Response response1 = doctorService.isAuthHeader(authHeader);
        String email = jwtTokenProvider.extractUsername(response1.getToken());
        Response response = doctorService.getDoctor(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);


    }





    @GetMapping("/getDoctor/{doctorId}")
    public ResponseEntity<Response> getDoctor(
            @PathVariable Long doctorId) {
        Response response = doctorService.fetchDoctorById(doctorId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


        // Doctor  accessible endpoints
    @GetMapping("/doctor/dashboard")
    public ResponseEntity<Response> getDashBoard(
            @RequestHeader("Authorization") String authHeader
        ) {
        Response response1 = doctorService.isAuthHeader(authHeader);
        String email = jwtTokenProvider.extractUsername(response1.getToken());
        Response response = doctoDashBoardService.getDoctorDashBoard(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }




    @GetMapping("/appointments/requests")
    public ResponseEntity<Response> getAppointmentRequests(
            @RequestHeader("Authorization") String token) {

        Response response = doctorService.getAppointmentRequests(token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/appointments/{appointmentId}/approve")
    public ResponseEntity<Response> approveAppointment(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token) {


        Response response = appointmentService.approveAppointment(appointmentId, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/appointments/{appointmentId}/reject")
    public ResponseEntity<Response> rejectAppointment(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token) {



        Response response = appointmentService.rejectAppointment(appointmentId, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/appointments/{appointmentId}/complete")
    public ResponseEntity<Response> completeAppointment(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token) {

        token=token.substring(7);
        String email = jwtTokenProvider.extractUsername(token);
        DoctorEntity doctor=doctorRepository.findByUserEmail(email);
        Response response = appointmentService.completeAppointment(appointmentId,doctor.getId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/schedule")
    public ResponseEntity<Response> getDoctorSchedule(
            @RequestHeader("Authorization") String token) {

        Response response = doctorService.getDoctorSchedule(token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/pendingAppointments")
    public ResponseEntity<Response> getPendingAppointments( @RequestHeader("Authorization") String token){

        Response response = doctorService.getPendingDoctorAppointments(token);
        return ResponseEntity.status(response.getStatusCode()).body(response);


    }




}
