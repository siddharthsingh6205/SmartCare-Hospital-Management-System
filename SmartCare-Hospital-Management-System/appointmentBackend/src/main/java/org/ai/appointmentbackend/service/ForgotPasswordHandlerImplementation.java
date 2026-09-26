package org.ai.appointmentbackend.service;

import org.ai.appointmentbackend.dto.Response;
import org.ai.appointmentbackend.entity.ForgotPasswordToken;
import org.ai.appointmentbackend.entity.UserEntity;
import org.ai.appointmentbackend.otpUtils.GenerateOtp;
import org.ai.appointmentbackend.repository.ForgotPasswordRepository;
import org.ai.appointmentbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ForgotPasswordHandlerImplementation implements ForgotPasswordHandlerService {

private final UserRepository userRepository;
    private final ForgotPasswordService forgotPasswordService;
private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordHandlerImplementation(UserRepository userRepository, ForgotPasswordRepository forgotPasswordRepository, ForgotPasswordService forgotPasswordService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.forgotPasswordService = forgotPasswordService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }





    public Response sendForgetPasswordOtp(String email) {
        Response response = new Response();
        try {
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                response.setStatusCode(404);
                response.setMessage("User not found with provided email.");
                return response;
            }
            String otp = GenerateOtp.generateOtp();
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());
            if (token == null) {
                token = forgotPasswordService.createToken(user, id, otp, email);
            }
            emailService.sendEmail(email,"Your Forgot Password Verification Code\n\n","Your verification code is " + token.getOtp());

            return Response.success("Password reset OTP sent successfully");
        } catch (Exception e) {
            return Response.error("Error sending password reset OTP: " + e.getMessage(),500);
        }

    }

    public Response verifyOtp(String email, String otp) {
        Response response = new Response();
        try {
            UserEntity userAccount = userRepository.findByEmail(email);
            if (userAccount == null) {

                return Response.error("User not found with provided email.",404);

            }

            ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findByUser(userAccount.getId());
            if (forgotPasswordToken == null) {

                return Response.error("Invalid or expired token.",404);

            }

            if (forgotPasswordToken.getOtp().equals(otp)) {
                return Response.success("OTP verified successfully.");
            } else {
                return Response.error("Wrong OTP provided.",400);
            }
        } catch (Exception e) {
            return Response.error("Error verifying OTP: " + e.getMessage(),500);
        }

    }

    public Response resetPassword(String email, String newPassword, String otp) {
        Response response = new Response();
        try {
            if (newPassword.length() <= 4) {

                return Response.error("Password must be at least 4 characters long.",404);

            }

            boolean isVerified = OtpCheck(email, otp);
            if (isVerified) {
                UserEntity userAccount = userRepository.findByEmail(email);
                if (userAccount == null) {

                    return Response.error("User not found with provided email.",404);

                }

                updatePassword(userAccount, newPassword);
                return Response.success("Password updated successfully!");
            } else {

                return Response.error("Wrong OTP provided.",400);
            }
        } catch (Exception e) {

            return Response.error("Error resetting password: " + e.getMessage(),500);
        }

    }

    public void updatePassword(UserEntity userAccount, String newPassword) {
        try {
            userAccount.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userAccount);
        } catch (Exception e) {
            throw new RuntimeException("Error updating password: " + e.getMessage());
        }
    }

    public boolean OtpCheck(String email, String otp) {
        Response response = new Response();
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            response.setStatusCode(404);
            response.setMessage("User not found with provided email.");
        }
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findByUser(user.getId());
        if (forgotPasswordToken == null) {
            response.setStatusCode(404);
            response.setMessage("Invalid or expired token.");
        }
        return forgotPasswordToken.getOtp().equals(otp);
    }
}
