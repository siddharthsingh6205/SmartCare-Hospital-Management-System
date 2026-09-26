package org.ai.appointmentbackend.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.AdminEntity;
import org.ai.appointmentbackend.entity.DoctorEntity;
import org.ai.appointmentbackend.mapper.DtoConverter;
import org.ai.appointmentbackend.repository.DoctorRepository;
import org.ai.appointmentbackend.service.AdminService;
import org.ai.appointmentbackend.service.AuthService;
import org.ai.appointmentbackend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private  AuthService authService;
    private    DashboardService dashboardService;
    private  AdminService adminService;
    private  DoctorRepository doctorRepository;

    public AdminController() {
    }

    @Autowired
    public AdminController(AuthService authService, DashboardService dashboardService,
                           AdminService adminService, DoctorRepository doctorRepository) {
        this.authService = authService;
        this.dashboardService = dashboardService;
        this.adminService = adminService;
        this.doctorRepository = doctorRepository;
    }

    @PostMapping(value = "/register/doctor", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> doctorRegister(@RequestPart("doctor") String doctorString,   @RequestPart(value = "image", required = false) MultipartFile imageFile) throws Exception {
        ObjectMapper objectMapper=new ObjectMapper();
        DoctorEntity doctorEntity=objectMapper.readValue(doctorString,DoctorEntity.class);
        Response response = authService.RegisterDoctor(doctorEntity,imageFile);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping(value = "/register/admin", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> adminRegister(  @RequestPart("admin") String adminString,  @RequestPart(value = "image", required = false) MultipartFile imageFile) throws Exception {
        ObjectMapper objectMapper=new ObjectMapper();
        AdminEntity adminEntity=objectMapper.readValue(adminString,AdminEntity.class);
        Response response = authService.RegisterAdmin(adminEntity,imageFile);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/dashboard")
    public ResponseEntity<Response> adminDashboard( ) throws Exception {
          Response response=dashboardService.getDashboardData();
          return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/allAdmins")
    public ResponseEntity<Response> getAllAdmin( ) throws Exception {
        Response response=dashboardService.getAllAdmins();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PostMapping("/doctors/{doctorId}/availability")
    public ResponseEntity<Response> setDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestBody Map<String, Set<String>> weeklySlots) {

        Response response = adminService.setDoctorAvailability(doctorId, weeklySlots);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/doctors/{doctorId}/availability")
    public ResponseEntity<Response> getDoctorAvailability(@PathVariable Long doctorId) {
        Response response = adminService.getDoctorAvailability(doctorId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }




}
