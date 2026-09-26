package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.DoctorDashboard;
import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.entity.DoctorEntity;
import org.ai.appointmentbackend.entity.PatientEntity;
import org.ai.appointmentbackend.enumpack.AppointmentStatus;
import org.ai.appointmentbackend.enumpack.ApprovalStatus;
import org.ai.appointmentbackend.mapper.DtoConverter;
import org.ai.appointmentbackend.repository.AppointmentRepository;
import org.ai.appointmentbackend.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoctorDashBoardServiceImplementation implements DoctoDashBoardService {


    private AppointmentRepository appointmentRepository;

    private DoctorRepository doctorRepository;
    private DashboardService dashboardService;

    @Autowired
    public DoctorDashBoardServiceImplementation(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, DashboardService dashboardService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.dashboardService = dashboardService;
    }

    public DoctorDashBoardServiceImplementation() {
    }

    public Response getDoctorDashBoard(String email) {
                  DoctorDashboard doctorDashboard = new DoctorDashboard();
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        try{

            DoctorEntity doctor=doctorRepository.findByUserEmail(email);
            if(doctor==null){
                return Response.error("again login you are not authorized",500);
            }
            List<AppointmentEntity>appointments=appointmentRepository.findByDoctorIdAndApprovalStatusNotIn(doctor.getId(), Arrays.asList(ApprovalStatus.APPROVED, ApprovalStatus.PENDING));
            List<AppointmentEntity>completedAppointments=appointmentRepository.findByDoctorIdAndAppointmentStatus(doctor.getId(), AppointmentStatus.COMPLETED);
            List<AppointmentEntity>cancelledApppointments=appointmentRepository.findByDoctorIdAndApprovalStatus(doctor.getId(), ApprovalStatus.REJECTED);
            List<AppointmentEntity>pendingAppointments=appointmentRepository.findByDoctorIdAndApprovalStatus(doctor.getId(), ApprovalStatus.PENDING);
            List<PatientEntity>patientEntityList=appointmentRepository.findDistinctPatientsByDoctor(doctor.getId());


            int approveCount = appointmentRepository.countByDoctorIdAndApprovalStatusAndAppointmentApprovalBetween(
                    doctor.getId(), ApprovalStatus.APPROVED, startOfDay, endOfDay);

            int rejectCount = appointmentRepository.countByDoctorIdAndApprovalStatusAndAppointmentApprovalBetween(
                    doctor.getId(), ApprovalStatus.REJECTED, startOfDay, endOfDay);
            int todayPending=appointmentRepository.countByDoctorIdAndApprovalStatusAndDate(doctor.getId(),ApprovalStatus.PENDING, LocalDate.now());
            int bookedSlots=appointmentRepository.countByDoctorIdAndDateAndApprovalStatus(doctor.getId(), LocalDate.now(),ApprovalStatus.APPROVED);
            LocalDate startDate=LocalDate.now().minusDays(6);
            List<Long>weeklyEarnings=
                    appointmentRepository.findWeeklyEarningsByDoctorId(
                            doctor.getId(), startDate
                    );


            Long earnings=0L;
            for(AppointmentEntity appointment:completedAppointments){
                earnings+=appointment.getAmount();
            }

            //Total Earnings
            doctorDashboard.setEarnings(earnings);
            //pending slots count
            doctorDashboard.setPendingSlotsCount(doctor.getPendingSlots().size());
            //available slots count
            doctorDashboard.setAvailableSlotsCount(doctor.getAvailableSlots().size());
            //approved slots count
            doctorDashboard.setApprovedSlotsCount(doctor.getApprovedSlots().size());
            //pending slots
            doctorDashboard.setPendingSlots(doctor.getPendingSlots());
            //available slots
            doctorDashboard.setAvailableSlots(doctor.getAvailableSlots());
            //approved slots
            doctorDashboard.setApprovedSlots(doctor.getApprovedSlots());
            //latest appointments
            doctorDashboard.setLatestAppointments(DtoConverter.convertAppointmentEntityListToAppointmentDtoList(appointments));
            //Appointments count
            doctorDashboard.setAppointments(appointments.size());
            //completed appointments count
            doctorDashboard.setCompletedAppointments(completedAppointments.size());
            //cancelled appointments count
            doctorDashboard.setCancelledAppointments(cancelledApppointments.size());
            //pending appointments count
            doctorDashboard.setPendingAppointments(pendingAppointments.size());
            //patients size
            doctorDashboard.setPatients(patientEntityList.size());
            //today approved
            doctorDashboard.setTodayApproved(approveCount);
            //today rejected
            doctorDashboard.setTodayRejected(rejectCount);
            //today pending
            doctorDashboard.setTodayPending(todayPending);
            //weekly earnings
            doctorDashboard.setWeeklyEarnings(weeklyEarnings);
            return Response.success("All doctor related details fetched").withDoctorDashboard(doctorDashboard);


        } catch (Exception e) {
            return Response.error(e.getMessage(),500);

        }


    }
}