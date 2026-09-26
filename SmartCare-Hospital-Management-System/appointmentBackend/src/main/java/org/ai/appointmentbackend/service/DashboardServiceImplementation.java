package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.*;
import org.ai.appointmentbackend.entity.AdminEntity;
import org.ai.appointmentbackend.entity.AppointmentEntity;
import org.ai.appointmentbackend.entity.DoctorEntity;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.enumpack.AppointmentStatus;
import org.ai.appointmentbackend.enumpack.ApprovalStatus;
import org.ai.appointmentbackend.mapper.DtoConverter;
import org.ai.appointmentbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImplementation implements DashboardService{
    private DoctorRepository doctorRepository;
    private UserRepository userRepository;
    private AdminRepository adminRepository;
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;

    public DashboardServiceImplementation() {
    }
@Autowired
    public DashboardServiceImplementation(DoctorRepository doctorRepository, UserRepository userRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository, AdminRepository adminRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.adminRepository = adminRepository;
    }

    public Response getDashboardData() {


        DashboardData dashboardData = new DashboardData();

        try {
            dashboardData.setDoctorCount(doctorRepository.count());
            dashboardData.setPatientCount(patientRepository.count());
            dashboardData.setAdminCount(adminRepository.count());
            dashboardData.setAppointmentCount(appointmentRepository.count());
            dashboardData.setLatestAppointments(DtoConverter.convertAppointmentEntityListToAppointmentDtoList(appointmentRepository.findAllByOrderByDateDesc()));

            List<AppointmentEntity>appointmentEntities=appointmentRepository.findByIsCompleted(true);

            int earnings=0;
            for(AppointmentEntity appointmentEntity:appointmentEntities){
                earnings+=appointmentEntity.getAmount();
            }
            dashboardData.setTotalRevenue(earnings);
            dashboardData.setTodaysAppointments(appointmentRepository.countByDate(LocalDate.now()));

            // Set appointment status counts
            List<Object[]> statusCounts = appointmentRepository.countAppointmentsByStatus();
            Map<String, Long> appointmentStatusMap = statusCounts.stream()
                    .collect(Collectors.toMap(
                            arr -> ((AppointmentStatus) arr[0]).name(),
                            arr -> (Long) arr[1]
                    ));
            dashboardData.setAppointmentStatusCount(appointmentStatusMap);

            // Set approval status counts
            List<Object[]> approvalCounts = appointmentRepository.countAppointmentsByApprovalStatus();
            Map<String, Long> approvalStatusMap = approvalCounts.stream()
                    .collect(Collectors.toMap(
                            arr -> ((ApprovalStatus) arr[0]).name(),
                            arr -> (Long) arr[1]
                    ));
            dashboardData.setApprovalStatusCount(approvalStatusMap);

            List<DoctorEntity>doctorEntityList=doctorRepository.findAll();
            List<DoctorPerformance>doctorPerformanceList=new ArrayList<>();
            for(DoctorEntity doctorEntity:doctorEntityList){
                DoctorPerformance doctorPerformance=new DoctorPerformance();
                doctorPerformance.setName(doctorEntity.getUser().getName());
                List<AppointmentEntity>appointmentEntityList=appointmentRepository.findByDoctorId(doctorEntity.getId());
                doctorPerformance.setAppointmentCount(appointmentEntityList.size());
                List<AppointmentEntity>completedAppointments=appointmentRepository.findByDoctorIdAndAppointmentStatus(doctorEntity.getId(), AppointmentStatus.COMPLETED);
                Long earningsForDoctor=0L;
                for(AppointmentEntity appointment:completedAppointments){

                    earningsForDoctor+=appointment.getAmount();


                }
                doctorPerformance.setTotalEarnings(earningsForDoctor);

                doctorPerformanceList.add(doctorPerformance);

            }
            dashboardData.setTopDoctors(doctorPerformanceList);

            //Specialization count
            List<SpecializationCount>specializationCountList=new ArrayList<>();
            String [] specializations={"General physician","Gynecologist","Dermatologist","Pediatricians","Neurologist","Gastroenterologist"};
           for(int i=0;i<specializations.length;i++){
               SpecializationCount specializationCount=new SpecializationCount();
               specializationCount.setName(specializations[i]);
               specializationCount.setCount(doctorRepository.countBySpecializationContainingIgnoreCase(specializations[i]));
               specializationCountList.add(specializationCount);
           }
           dashboardData.setSpecializationDistribution(specializationCountList);



           //paymentstatus
            List<AppointmentEntity>completedAppointments=appointmentRepository.findByIsCompleted(true);
            List<AppointmentEntity>appointmentEntityList=appointmentRepository.findAll();
            double appointmentRate=Math.floor(((double) completedAppointments.size() /appointmentEntityList.size())*100);
           Long totalEarnings=0L;
           for(AppointmentEntity appointmentEntity:appointmentEntityList){
               totalEarnings+=appointmentEntity.getAmount();
           }
            Long pending=totalEarnings-earnings;
            PaymentStatus paymentStatus=new PaymentStatus();
            paymentStatus.setPaid((long) earnings);
            paymentStatus.setPending(pending);
            dashboardData.setPaymentStatus(paymentStatus);


//System Health
            SystemHealth systemHealth=new SystemHealth();
            List<UserEntity>activeUser=userRepository.findAll();
            systemHealth.setActiveUsers(activeUser.size());
            systemHealth.setAppointmentRate(appointmentRate);
            dashboardData.setSystemHealth(systemHealth);


            return Response.success("The admin dashboard data successfully retrieved").withDashBoardData(dashboardData);
        } catch (Exception e) {
          return Response.error("The admin dashboard not successfuly created",500);
        }

    }

    @Override
    public Response getAllAdmins() {

        try {
            List<AdminEntity> admins = adminRepository.findAll();

            if ( admins.isEmpty()) {

                return Response.success("No admins found in the system").withAdminDto(Collections.emptyList());

            } else {


                return Response.success("admins retrieved successfully").withAdminDto(DtoConverter.convertAdminEntityListToAdminDtoList(admins));
            }

        } catch (Exception e) {

            return Response.error("Failed to retrieve admins. Please try again later.",500).withAdminDto(Collections.emptyList());

        }

    }

}
