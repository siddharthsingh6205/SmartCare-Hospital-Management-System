package org.ai.appointmentbackend.repository;

import org.ai.appointmentbackend.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity,Long> {
    AdminEntity findByUserId(Long id);
}
