import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";

import ForgotPasswordForm from "../../components/ForgotPassword/ForgotPasswordForm";
import OtpInput from "../../components/ForgotPassword/OtpInput";
import PasswordResetForm from "../../components/ForgotPassword/PasswordResetForm";
import SuccessMessage from "../../components/ForgotPassword/SuccessMessage";
import { useForgotPassword } from "../../hooks/ForGotPassword/useForgotPassword";
import psImg from "../../assets/ps.png";
const ForgotPassword = () => {
  const navigate = useNavigate();
  const {
    handleChangeOtp,
    handleKeyDown,
    handleResetPassword,
    handleSendOtp,
    handleVerifyOtp,
    email,
    setEmail,
    step,
    setStep,
    loading,
    otp,
    setOtp,
    newPassword,
    setNewPassword,
    setConfirmPassword,
    confirmPassword,
  } = useForgotPassword();

  return (
    <div className="bg-gray-50 min-h-screen w-full flex items-center justify-center">
      <div className="flex items-center justify-center w-full max-w-6xl mx-auto">
        <div className="hidden md:block md:w-1/2">
          <img src={psImg} alt="Password reset" className="w-full" />
        </div>

        <div className="w-full md:w-1/2 flex justify-center px-4">
          {step === 1 && (
            <ForgotPasswordForm
              email={email}
              setEmail={setEmail}
              loading={loading}
              handleSendOtp={handleSendOtp}
            />
          )}
          {step === 2 && (
            <OtpInput
              otp={otp}
              handleChangeOtp={handleChangeOtp}
              handleKeyDown={handleKeyDown}
              loading={loading}
              handleVerifyOtp={handleVerifyOtp}
            />
          )}
          {step === 3 && (
            <PasswordResetForm
              newPassword={newPassword}
              setNewPassword={setNewPassword}
              confirmPassword={confirmPassword}
              setConfirmPassword={setConfirmPassword}
              loading={loading}
              handleResetPassword={handleResetPassword}
            />
          )}
          {step === 4 && <SuccessMessage navigate={navigate} />}
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;
