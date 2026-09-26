package org.ai.appointmentbackend.repository;

import org.ai.appointmentbackend.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity,Long> {

    PatientEntity findByUserEmail(String email);

    PatientEntity findByUserId(Long id);
}
