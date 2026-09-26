package org.ai.appointmentbackend.service;

import lombok.AllArgsConstructor;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.PatientEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.mapper.DtoConverter;
import org.ai.appointmentbackend.repository.PatientRepository;
import org.ai.appointmentbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImplementation implements PatientService{

    private  PatientRepository patientRepository;

    private  UserRepository userRepository;

    @Autowired
    public PatientServiceImplementation(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public PatientServiceImplementation() {
    }

    @Override
    public Response getAllPatients() {
       Response response=new Response();
       try {
           List<PatientEntity> patients = patientRepository.findAll();
           if (patients.isEmpty()) {
               return Response.error("No patients found in the system",404);

           } else {

               return Response.success("Patients retrieved successfully").withPatientList(DtoConverter.convertPatientEntityListToPatientDtoList(patients));

           }
       }catch (Exception e) {

           return Response.error("Unable to retrieve patients at this time. Please try again later.",500);
       }

    }

    @Override
    public Response getPatientById(Long id) {

        try {
            if (id == null || id <= 0) {
                return Response.error("Invalid patient ID provided",400);

            }
            Optional<UserEntity> users=userRepository.findById(id);
            if(users.isPresent()) {
                PatientEntity patient=patientRepository.findByUserId(users.get().getId());

                return Response.success("Patient details retrieved successfully").withPatient(DtoConverter.convertPatientEntityToPatientDto(patient));
            }else{

                return Response.error("Patient with ID " + id + " not found",500);
            }

        }catch (Exception e) {

            return Response.error("Error retrieving patient information. Please try again.",500);
        }

    }

    @Override
    public Response updatePatient(Long id,PatientEntity updatedPatient) {
        Response response=new Response();
        try {
            if (id == null || id <= 0) {

               return Response.error("Invalid patient ID provided",400);

            }
            PatientEntity existingPatient = patientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

            if (updatedPatient.getAge() != 0) {
                if (updatedPatient.getAge() < 0 || updatedPatient.getAge() > 120) {

                    return Response.error("Invalid age value provided",400);

                }
                existingPatient.setAge(updatedPatient.getAge());
            }
            if (updatedPatient.getGender() != null) {
                existingPatient.setGender(updatedPatient.getGender());
            }
            if (updatedPatient.getContactNumber() != null) {
                String trimmedNumber = updatedPatient.getContactNumber().trim();
                if (!trimmedNumber.matches("^[0-9\\-\\+\\s]+$")) {

                    return Response.error("Invalid contact number format",400);

                }
                existingPatient.setContactNumber(trimmedNumber);
            }
            if (updatedPatient.getAddress() != null) {
                existingPatient.setAddress(updatedPatient.getAddress().trim());
            }
            if (updatedPatient.getMedicalHistory() != null) {
                existingPatient.setMedicalHistory(updatedPatient.getMedicalHistory().trim());
            }
            if(updatedPatient.getDob() != null) {
                existingPatient.setDob(updatedPatient.getDob().trim());
            }
            if(updatedPatient.getUser()!=null) {
                UserEntity existingUser = existingPatient.getUser();
                UserEntity updatedUser = updatedPatient.getUser();

                if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
                    String trimmedName = updatedUser.getName().trim();
                    if (!trimmedName.matches("^[a-zA-Z\\s]+$")) {

                        return Response.error("Name can only contain letters and spaces",400);
                        }
                    existingUser.setName(trimmedName);
                }
            }

           PatientEntity patientEntity=patientRepository.save(existingPatient);
            response.setPatientDto(DtoConverter.convertPatientEntityToPatientDto(patientEntity));

            return Response.success("Patient information updated successfully");
        }catch (RuntimeException e) {
            response.setStatusCode(404);
            return Response.error("Invalid patient ID provided",400);
        }



        catch (Exception e) {

            return Response.error("Unable to update patient information. Please try again later.",500);
        }

    }



}
