package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.entity.ForgotPasswordToken;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.repository.ForgotPasswordRepository;
import org.springframework.stereotype.Service;




@Service
public class ForgotPasswordServiceImplementation implements ForgotPasswordService{

    private final ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordServiceImplementation(ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    @Override
    public ForgotPasswordToken createToken(UserEntity user, String id, String otp, String sendTo) {
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setUserEntity(user);
        forgotPasswordToken.setOtp(otp);
        forgotPasswordToken.setId(id);
        forgotPasswordToken.setEmail(sendTo);
        return forgotPasswordRepository.save(forgotPasswordToken);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserEntity_id(userId);
    }


}
