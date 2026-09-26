package org.ai.appointmentbackend.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ai.appointmentbackend.enumpack.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String message;
    private boolean success;
    private int statusCode;
    private Role role;

    private AdminDto adminDto;
    private AppointmentDto appointmentDto;
    private DoctorDto doctorDto;
    private DoctorDashboard doctorDashboard;
    private PatientDto patientDto;
    private UserDto userDto;
    private NotificationDto notificationDto;
    private List<PatientDto>patientDtos;
    private List<AdminDto>adminDtos;
    private List<DoctorDto>doctorDtos;
    private List<AppointmentDto>appointmentDtos;
    private List<NotificationDto>notificationDtos;
    private DashboardData dashboardData;

    private Object data;


    //others
    private String token;
    private String expirationTime;
    private int count;



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public DashboardData getDashboardData() {
        return dashboardData;
    }

    public void setDashboardData(DashboardData dashboardData) {
        this.dashboardData = dashboardData;
    }

    public List<AppointmentDto> getAppointmentDtos() {
        return appointmentDtos;
    }

    public void setAppointmentDtos(List<AppointmentDto> appointmentDtos) {
        this.appointmentDtos = appointmentDtos;
    }

    public List<DoctorDto> getDoctorDtos() {
        return doctorDtos;
    }

    public void setDoctorDtos(List<DoctorDto> doctorDtos) {
        this.doctorDtos = doctorDtos;
    }

    public List<AdminDto> getAdminDtos() {
        return adminDtos;
    }

    public void setAdminDtos(List<AdminDto> adminDtos) {
        this.adminDtos = adminDtos;
    }

    public List<PatientDto> getPatientDtos() {
        return patientDtos;
    }

    public void setPatientDtos(List<PatientDto> patientDtos) {
        this.patientDtos = patientDtos;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public PatientDto getPatientDto() {
        return patientDto;
    }

    public void setPatientDto(PatientDto patientDto) {
        this.patientDto = patientDto;
    }

    public DoctorDashboard getDoctorDashboard() {
        return doctorDashboard;
    }

    public void setDoctorDashboard(DoctorDashboard doctorDashboard) {
        this.doctorDashboard = doctorDashboard;
    }

    public DoctorDto getDoctorDto() {
        return doctorDto;
    }

    public void setDoctorDto(DoctorDto doctorDto) {
        this.doctorDto = doctorDto;
    }

    public AppointmentDto getAppointmentDto() {
        return appointmentDto;
    }

    public void setAppointmentDto(AppointmentDto appointmentDto) {
        this.appointmentDto = appointmentDto;
    }

    public AdminDto getAdminDto() {
        return adminDto;
    }

    public void setAdminDto(AdminDto adminDto) {
        this.adminDto = adminDto;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    // Success factory method
    public static Response success(String message) {
        Response response = new Response();
        response.setMessage(message);
        response.setSuccess(true);
        response.setStatusCode(200);
        return response;
    }

    // Error factory method
    public static Response error(String message, int statusCode) {
        Response response = new Response();
        response.setMessage(message);
        response.setSuccess(false);
        response.setStatusCode(statusCode);
        return response;
    }


    //Response with adminDto
    public Response withAdmin(AdminDto adminDto) {
        this.adminDto = adminDto;
        this.adminDtos = null;
        return this;
    }

    //Response with adminDtolist
    public Response withAdminDto(List<AdminDto> adminDtoList) {
        this.adminDto = null;
        this.adminDtos = adminDtoList;
        return this;
    }

    //Response with doctorDto
    public Response withDoctor(DoctorDto doctorDto) {
        this.doctorDto = doctorDto;
        this.doctorDtos = null;
        return this;
    }

    //Response with doctorDtoList
    public Response withDoctorList(List<DoctorDto> doctorDtoList) {
        this.doctorDto = null;
        this.doctorDtos = doctorDtoList;
        return this;
    }

    //Response with patientDto
    public Response withPatient(PatientDto patient) {
        this.patientDto = patient;
        this.patientDtos = null;
        return this;
    }

    //Response with patientDtoList
    public Response withPatientList(List<PatientDto> patientDtoList) {
        this.patientDto = null;
        this.patientDtos = patientDtoList;
        return this;
    }

    public Response withUser(UserDto userDto) {
        this.userDto = userDto;
        return this;
    }


    //Response with appointmentdto
    public Response withAppointment(AppointmentDto appointment) {
        this.appointmentDto = appointment;
        this.appointmentDtos = null;
        return this;
    }



    //Response with appointmentdtoslist
    public Response withAppointmentList(List<AppointmentDto> appointmentDtoList) {
        this.appointmentDto = null;
        this.appointmentDtos = appointmentDtoList;
        return this;
    }

    //Response with Token and Role
    public Response withTokenAndRole(String token, Role role) {
        this.token = token;
        this.role = role;
        return this;
    }

    public Response withExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }

    public Response withNotification(NotificationDto notification) {
        this.notificationDto = notification;
        this.notificationDtos = null;
        return this;

    }
    public Response withNotificationList(List<NotificationDto> notificationDtoList) {
        this.notificationDto = null;
        this.notificationDtos = notificationDtoList;
        return this;
    }

    public Response withCount(int count){
        this.count = count;
        return this;
    }
    public Response withDashBoardData(DashboardData dashboardData){
        this.dashboardData = dashboardData;
        return this;
    }
    public Response withDoctorDashboard(DoctorDashboard dashboardData){
        this.doctorDashboard = dashboardData;
        return this;
    }






}
