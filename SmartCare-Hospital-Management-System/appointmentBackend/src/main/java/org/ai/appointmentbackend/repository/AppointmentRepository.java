package org.ai.appointmentbackend.repository;

import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.entity.PatientEntity;
import org.ai.appointmentbackend.enumpack.AppointmentStatus;
import org.ai.appointmentbackend.enumpack.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity,Long> {

    List<AppointmentEntity> findByPatientId(Long patientId);
    List<AppointmentEntity> findByDoctorId(Long doctorId);
    List<AppointmentEntity> findByDoctorIdAndApprovalStatusNotIn(Long doctorId, List<ApprovalStatus> statuses);
    List<AppointmentEntity> findByAppointmentStatus(AppointmentStatus appointmentStatus);


    List<AppointmentEntity> findByDoctorIdAndApprovalStatus(Long id, ApprovalStatus approvalStatus);


    List<AppointmentEntity> findByDoctorIdAndAppointmentStatus(Long id, AppointmentStatus appointmentStatus);


    int countByDoctorIdAndApprovalStatusAndAppointmentApprovalBetween(
            Long doctorId,
            ApprovalStatus approvalStatus,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );


    // Today's pending appointments count
    int countByDoctorIdAndApprovalStatusAndDate(
            Long doctorId,
            ApprovalStatus approvalStatus,
            LocalDate date);

    // Today's booked slots count
    int countByDoctorIdAndDateAndApprovalStatus(
            Long doctorId,
            LocalDate date,
            ApprovalStatus approvalStatus);


    // Weekly earnings (last 7 days including today)
    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM AppointmentEntity a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentStatus = 'COMPLETED' " +
            "AND a.date BETWEEN :startDate AND CURRENT_DATE " +
            "GROUP BY a.date " +
            "ORDER BY a.date")
    List<Long> findWeeklyEarningsByDoctorId(
             Long doctorId,
          LocalDate startDate);

    // Get distinct patients with their details
    @Query("SELECT DISTINCT a.patient FROM AppointmentEntity a WHERE a.doctor.id = :doctorId")
    List<PatientEntity> findDistinctPatientsByDoctor( Long doctorId);

    List<AppointmentEntity> findAllByOrderByDateDesc();


    List<AppointmentEntity> findByIsCompleted(boolean isCompleted);


    int countByDate(LocalDate date);



    // Count appointments by status
    @Query("SELECT a.appointmentStatus, COUNT(a) FROM AppointmentEntity a GROUP BY a.appointmentStatus")
    List<Object[]> countAppointmentsByStatus();

    // Count appointments by approval status
    @Query("SELECT a.approvalStatus, COUNT(a) FROM AppointmentEntity a GROUP BY a.approvalStatus")
    List<Object[]> countAppointmentsByApprovalStatus();

}
