package org.ai.appointmentbackend.controller;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ai.appointmentbackend.configuration.JwtTokenProvider;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.PatientEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.repository.UserRepository;
import org.ai.appointmentbackend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private  PatientService patientService;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    @Autowired
    public PatientController(PatientService patientService, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.patientService = patientService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public PatientController() {
    }


    @GetMapping("/allPatients")
    public ResponseEntity<Response> getAllPatients(){
        Response response=patientService.getAllPatients();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping()
    public ResponseEntity<Response> getPatientById(@RequestHeader("Authorization") String authorization){
        authorization=authorization.substring(7);
        String email=jwtTokenProvider.extractUsername(authorization);
        UserEntity user=userRepository.findByEmail(email);
        Response response=patientService.getPatientById(user.getId());
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }



    @PutMapping("/{id}")
    public ResponseEntity<Response> updatePatient(@PathVariable Long id,
                                                  @RequestBody PatientEntity patientEntity){
        Response response=patientService.updatePatient(id,patientEntity);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }





}
