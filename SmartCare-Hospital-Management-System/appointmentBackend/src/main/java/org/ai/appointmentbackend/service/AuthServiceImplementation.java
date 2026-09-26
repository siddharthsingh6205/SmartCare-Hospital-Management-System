package org.ai.appointmentbackend.service;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ai.appointmentbackend.configuration.JwtTokenProvider;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.dto.UserDto;
import org.ai.appointmentbackend.entity.*;
import org.ai.appointmentbackend.enumpack.Role;
import org.ai.appointmentbackend.mapper.DtoConverter;
import org.ai.appointmentbackend.repository.*;
import org.ai.appointmentbackend.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Service
public class AuthServiceImplementation implements AuthService {

    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;
    private  PatientRepository patientRepository;
    private  DoctorRepository doctorRepository;
    private  AdminRepository adminRepository;
    private  JwtTokenProvider jwtTokenProvider;
    private  EmailService emailService;
    private  AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder, PatientRepository patientRepository, DoctorRepository doctorRepository, AdminRepository adminRepository, JwtTokenProvider jwtTokenProvider, EmailService emailService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
    }

    public AuthServiceImplementation() {
    }

    //REGISTER PATIENT
    @Override
    public Response RegisterPatient(PatientEntity patient,MultipartFile imageFile) {
        try {
            return registerUser(patient.getUser(), Role.PATIENT,imageFile, user -> {
                PatientEntity savedPatient = new PatientEntity();
                savedPatient.setUser(user);
                savedPatient.setAge(patient.getAge());
                savedPatient.setGender(patient.getGender());
                savedPatient.setAddress(patient.getAddress());
                savedPatient.setContactNumber(patient.getContactNumber());
                savedPatient.setMedicalHistory(patient.getMedicalHistory());
                savedPatient.setDob(patient.getDob());
                savedPatient = patientRepository.save(savedPatient);

                return Response.success("The patient has been registered successfully").withPatient(DtoConverter.convertPatientEntityToPatientDto(savedPatient))
                        .withTokenAndRole(jwtTokenProvider.generateToken(user),Role.PATIENT)
                        ;
            });
        } catch (Exception e) {
            return Response.error("Registration failed: " + e.getMessage(), 500);
        }
    }


    private Response registerUser(UserEntity userEntity, Role role,MultipartFile imageFile, Function<UserEntity, Response> successHandler) {
        try {
            UserEntity existingUser = userRepository.findByEmail(userEntity.getEmail());
            if (existingUser != null) {
                return Response.error("User already registered with email please go to Login page.", 400);
            }

            UserEntity newUserEntity = saveUserEntity(userEntity, role, imageFile);
            return successHandler.apply(newUserEntity);

        } catch (Exception e) {
            return Response.error("Registration failed: " + e.getMessage(), 500);
        }
    }

    private UserEntity saveUserEntity(UserEntity userEntity, Role role, MultipartFile imageFile) throws IOException {
        userEntity.setRole(role);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        if (imageFile != null && !imageFile.isEmpty()) {
            userEntity.setImage(imageFile.getBytes());
            userEntity.setImageName(imageFile.getOriginalFilename());
            userEntity.setImageType(imageFile.getContentType());
        }
        return userRepository.save(userEntity);

    }


    @Override
    public Response RegisterAdmin(AdminEntity admin,MultipartFile imageFile) {
        try {
            String password1 = admin.getUser().getPassword();
            return registerUser(admin.getUser(), Role.ADMIN,imageFile,user -> {
                AdminEntity savedAdmin = new AdminEntity();
                savedAdmin.setUser(user);
                savedAdmin = adminRepository.save(savedAdmin);

                try {
                    sendRegistrationEmails(user, Role.ADMIN, password1);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                return Response.success("The admin has been registered successfully and the admin has been successfully added").withAdmin(DtoConverter.convertAdminEntityToAdminDto(savedAdmin))
                        .withTokenAndRole(jwtTokenProvider.generateToken(user),Role.ADMIN)
                        ;
            });
        } catch (Exception e) {
            return Response.error("Registration failed: " + e.getMessage(), 500);
        }
    }


    @Override
    public Response RegisterDoctor(DoctorEntity doctor,MultipartFile imageFile) {
        try {
            String plainTextPassword = doctor.getUser().getPassword();
            return registerUser(doctor.getUser(), Role.DOCTOR,imageFile, user -> {
                DoctorEntity savedDoctor = new DoctorEntity();

                savedDoctor.setUser(user);
                savedDoctor.setAboutDoctor(doctor.getAboutDoctor());
                savedDoctor.setDegree(doctor.getDegree());
                savedDoctor.setFees(doctor.getFees());
                savedDoctor.setExperience(doctor.getExperience());
                savedDoctor.setAddress1(doctor.getAddress1());



                savedDoctor.setSpecialization(doctor.getSpecialization());
                savedDoctor.setContactNumber(doctor.getContactNumber());
                savedDoctor.setAvailability(doctor.getAvailability());



                savedDoctor = doctorRepository.save(savedDoctor);

                try {
                    sendRegistrationEmails(user, Role.DOCTOR, plainTextPassword);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                return Response.success("The doctor has been registered successfully").withDoctor(DtoConverter.convertDoctorEntityToDoctorDto(savedDoctor))
                        .withTokenAndRole(jwtTokenProvider.generateToken(user),Role.DOCTOR)
                        ;
            });
        } catch (Exception e) {
            return Response.error("Registration failed: " + e.getMessage(), 500);
        }
    }




    private void sendRegistrationEmails(UserEntity user, Role role, String password) throws MessagingException {

        if(role==Role.ADMIN){
            emailService.sendAdminRegistrationEmail(user,password);


        }else if(role==Role.DOCTOR){
            emailService.sendDoctorRegistraionEmail(user,password);



        }

    }

    @Override
    public Response LoginUser(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );

            // Fetch user details
            UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail());

            // Generate JWT token
            String token = jwtTokenProvider.generateToken(userEntity);
            Date expirationDate = jwtTokenProvider.extractExpiration(token);

            // Convert UserEntity to UserDto
            UserDto userDto = DtoConverter.convertUserEntityToUserDto(userEntity);

            // Assign role-specific IDs based on the user's role
            switch (userDto.getRole()) {
                case PATIENT:
                    PatientEntity patientEntity = patientRepository.findByUserId(userEntity.getId());
                    userDto.setPatientId(patientEntity != null ? patientEntity.getId() : null);   // Assuming you store patient-related info in the UserEntity
                    break;
                case DOCTOR:
                    DoctorEntity doctorEntity = doctorRepository.findByUserId(userEntity.getId());
                    userDto.setDoctorId(doctorEntity != null ? doctorEntity.getId() : null); // Set doctor ID
                    break;
                case ADMIN:
                    // Fetch admin data based on email or user ID
                    AdminEntity adminEntity = adminRepository.findByUserId(userEntity.getId());
                    userDto.setAdminId(adminEntity != null ? adminEntity.getId() : null); // Set admin ID  // Handle admin-related info
                    break;
                default:
                    // If no recognized role, can set to null or throw an error
                    throw new IllegalArgumentException("Invalid role.");
            }



            return Response.success("The account has been logged in successfully.")
                    .withUser(userDto)
                    .withTokenAndRole(token,userDto.getRole())
                    .withExpirationTime(String.valueOf(expirationDate));

        } catch (Exception e) {

            return Response.error("Login failed: " + e.getMessage(),500);
        }


    }


}