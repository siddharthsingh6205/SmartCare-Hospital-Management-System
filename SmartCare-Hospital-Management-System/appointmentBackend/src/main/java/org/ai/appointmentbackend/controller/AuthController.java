package org.ai.appointmentbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.PatientEntity;
import org.ai.appointmentbackend.request.LoginRequest;
import org.ai.appointmentbackend.service.AuthService;
import org.ai.appointmentbackend.service.ForgotPasswordHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AuthController {

    private  AuthService authService;
    private  ForgotPasswordHandlerService forgotPasswordHandlerService;

    @Autowired
    public AuthController(AuthService authService, ForgotPasswordHandlerService forgotPasswordHandlerService) {
        this.authService = authService;
        this.forgotPasswordHandlerService = forgotPasswordHandlerService;
    }

    public AuthController() {
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        Response response=authService.LoginUser(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping(value = "/auth/patient", consumes = {"multipart/form-data"})
    public ResponseEntity<Response> patientRegister(@RequestPart("patient") String patientString,  @RequestPart(value = "image", required = false) MultipartFile imageFile) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        PatientEntity patient=objectMapper.readValue(patientString,PatientEntity.class);
        System.out.println(patient);
        Response response = authService.RegisterPatient(patient,imageFile);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/forgotpassword/send-otp")
    public ResponseEntity<Response> sendForgetPasswordOtp(@RequestParam String email){
        Response response=forgotPasswordHandlerService.sendForgetPasswordOtp(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/forgotpassword/verify-otp")
    public ResponseEntity<Response> verifyOtp(@RequestParam String email, @RequestParam String otp)   {
        Response response = forgotPasswordHandlerService.verifyOtp(email,otp);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/forgotpassword/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String otp) {
        Response response = forgotPasswordHandlerService.resetPassword(email,newPassword,otp);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}
