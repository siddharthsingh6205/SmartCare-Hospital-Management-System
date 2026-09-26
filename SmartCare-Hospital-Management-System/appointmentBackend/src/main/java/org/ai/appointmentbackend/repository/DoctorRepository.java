package org.ai.appointmentbackend.repository;

import org.ai.appointmentbackend.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity,Long> {


    List<DoctorEntity> findBySpecializationContainingIgnoreCase(String specialization);

    DoctorEntity findByUserEmail(String userEmail);


    DoctorEntity findByUserId(Long id);

    int countBySpecializationContainingIgnoreCase(String specialization);
}
