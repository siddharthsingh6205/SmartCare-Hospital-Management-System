import { useContext, useState } from "react";
import { toast } from "react-toastify";
import { AppContext } from "../../context/AppContext";

export const useForgotPassword = () => {
  const [step, setStep] = useState(1);
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState(new Array(6).fill(""));
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const { sendOtp, verifyOtp, resetPassword } = useContext(AppContext);

  const handleSendOtp = async () => {
    setLoading(true);
    if (email.trim() === "") {
      toast.error("Please enter a valid email.");
      setLoading(false);
      return;
    }

    const success = await sendOtp(email);
    if (success) {
      localStorage.setItem("email", email);
      setStep(2);
    }
    setLoading(false);
  };

  const handleChangeOtp = (value, index) => {
    if (!/^\d$/.test(value) && value !== "") return;
    const updatedOtp = [...otp];
    updatedOtp[index] = value;
    setOtp(updatedOtp);

    if (value !== "" && index < 5) {
      document.getElementById(`otp-input-${index + 1}`).focus();
    }
  };

  const handleKeyDown = (e, index) => {
    if (e.key === "Backspace" && otp[index] === "" && index > 0) {
      document.getElementById(`otp-input-${index - 1}`).focus();
    }
  };

  const handleVerifyOtp = async () => {
    setLoading(true);
    const otpValue = otp.join("");
    if (otpValue.length < 6) {
      toast.error("Please enter the complete 6-digit OTP.");
      setLoading(false);
      return;
    }
    const email = localStorage.getItem("email");
    if (!email) {
      toast.error("Email not found. Please try again.");
      setLoading(false);
      return;
    }

    const success = await verifyOtp(email, otpValue);
    if (success) {
      localStorage.setItem("forgetotp", otpValue);
      setStep(3);
    }
    setLoading(false);
  };

  const handleResetPassword = async () => {
    setLoading(true);
    if (newPassword !== confirmPassword) {
      toast.error("Passwords do not match.");
      setLoading(false);
      return;
    }
    if (newPassword.trim() === "") {
      toast.error("Please enter a new password.");
      setLoading(false);
      return;
    }
    const email = localStorage.getItem("email");
    const forgetotp = localStorage.getItem("forgetotp");

    const success = await resetPassword(email, forgetotp, newPassword);
    if (success) {
      setStep(4);
    }
    setLoading(false);
  };

  return {
    email,
    setEmail,
    loading,
    handleChangeOtp,
    handleKeyDown,
    handleSendOtp,
    handleVerifyOtp,
    handleResetPassword,
    step,
    setStep,
    otp,
    setOtp,
    newPassword,
    confirmPassword,
    setConfirmPassword,
    setNewPassword,
  };
};
