package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.entity.ForgotPasswordToken;
import org.ai.appointmentbackend.entity.UserEntity;


public interface ForgotPasswordService {

    ForgotPasswordToken createToken(UserEntity user, String id, String otp, String sendTo);
    ForgotPasswordToken findByUser(Long userId) ;
}
