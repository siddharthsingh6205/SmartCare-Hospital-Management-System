import axios from "axios";
import React, { useContext, useEffect, useRef } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { toast } from "react-toastify";
import { AppContext } from "../../context/AppContext";

const Verify = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const success = searchParams.get("success");
  const appointmentId = searchParams.get("appointmentId");
  const { token, verifyStripe } = useContext(AppContext);
  const navigate = useNavigate();
  const hasVerified = useRef(false);
  useEffect(() => {
    const verifyPayment = async () => {
      if (token && appointmentId && success && !hasVerified.current) {
        hasVerified.current = true; // Mark as verified
        const result = await verifyStripe(success, appointmentId);
        console.log("Appointment:", result); // Properly logged object
        navigate("/my-appointments");
      }
    };

    verifyPayment();
  }, [token, appointmentId, success, verifyStripe, navigate]);

  return (
    <div className="min-h-[60vh] flex items-center justify-center">
      <div className="w-20 h-20 border-4 border-gray-300 border-t-4 border-t-primary rounded-full animate-spin"></div>
    </div>
  );
};

export default Verify;
