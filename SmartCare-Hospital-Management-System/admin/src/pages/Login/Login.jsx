import React, { useState } from "react";

import { FaTimes } from "react-icons/fa";
import LoginForm from "../../components/Login/LoginForm";

const Login = () => {
  const [state, setState] = useState("Admin");

  const [loading, setLoading] = useState(false);
  const [otpBar, setOtpBar] = useState(false);
  const [otp, setOtp] = useState(["", "", "", "", "", ""]);
  const [remember, setRemember] = useState(false);

  const handleOtpChange = (e, index) => {
    const value = e.target.value;
    const updatedOtp = [...otp];
    updatedOtp[index] = value;
    setOtp(updatedOtp);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="flex flex-col gap-4 m-auto items-start p-8 w-full max-w-md border rounded-xl text-gray-600 text-sm shadow-lg bg-white">
        <h2 className="text-2xl font-semibold m-auto text-gray-800">
          <span className="text-primary">{state}</span> Login
        </h2>

        <LoginForm />

        {state === "Admin" ? (
          <p className="m-auto text-gray-600">
            Doctor Login?{" "}
            <span
              onClick={() => setState("Doctor")}
              className="text-primary underline cursor-pointer"
            >
              Click here
            </span>
          </p>
        ) : (
          <p className="m-auto text-gray-600">
            Admin Login?{" "}
            <span
              onClick={() => setState("Admin")}
              className="text-primary underline cursor-pointer"
            >
              Click here
            </span>
          </p>
        )}
      </div>
    </div>
  );
};

export default Login;
