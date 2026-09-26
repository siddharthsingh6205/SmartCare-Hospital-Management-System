import { createContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";

export const AppContext = createContext();

const AppContextProvider = (props) => {
  // Constants
  const currencySymbol = "$";
  const backendUrl = import.meta.env.VITE_BACKEND_URL;

  // State variables
  const [userData, setUserData] = useState(null);
  const [docInfo, setDocInfo] = useState(null);
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [appointments, setAppointments] = useState([]);
  const [patientId, setPatientId] = useState(
    localStorage.getItem("patientId") ? localStorage.getItem("patientId") : ""
  );
  const [token, setToken] = useState(
    localStorage.getItem("token") ? localStorage.getItem("token") : ""
  );

  // Hooks
  const navigate = useNavigate();
  const location = useLocation();

  // ===================== AUTHENTICATION FUNCTIONS =====================

  /**
   * Register a new patient
   * @param {Object} formDataToSend - Patient registration data
   * @param {File} image - Patient profile image
   */
  const registerPatient = async (formDataToSend, image) => {
    setLoading(true);
    try {
      const { data } = await axios.post(
        backendUrl + "/api/auth/patient",
        formDataToSend,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      if (data.statusCode === 200) {
        toast.success("Registration successful! Please login.😀");
        navigate("/login");
      }
    } catch (err) {
      console.log(err.message);
      console.log(err.response?.data?.message);
      const errorMessage = "Registration failed Please try again😔";
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Login an existing patient
   * @param {Object} formDataToSend - Login credentials
   * @param {boolean} remember - Remember user credentials
   */
  const loginPatient = async (formDataToSend, remember) => {
    setLoading(true);
    try {
      const { data } = await axios.post(
        backendUrl + "/api/login",
        formDataToSend
      );

      if (data.statusCode === 200) {
        if (remember) {
          localStorage.setItem("rememberedEmail", formDataToSend.email);
          localStorage.setItem("rememberedPassword", formDataToSend.password);
        } else {
          localStorage.removeItem("rememberedEmail");
          localStorage.removeItem("rememberedPassword");
        }

        setToken(data.token);
        setPatientId(data.userDto.patientId);
        localStorage.setItem("token", data.token);
        localStorage.setItem("patientId", data.userDto.patientId);

        toast.success("Login successful!");
        navigate("/");
      } else {
        throw new Error(data.message || "Login failed");
      }
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || err.message || "Login failed";
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // ===================== PASSWORD RECOVERY FUNCTIONS =====================

  /**
   * Send OTP to user's email for password recovery
   * @param {string} email - User's email address
   * @returns {boolean} - Success status
   */
  const sendOtp = async (email) => {
    try {
      const { data } = await axios.post(
        `${backendUrl}/api/forgotpassword/send-otp`,
        {},
        { params: { email } }
      );
      if (data.statusCode == 200) {
        toast.success(data.message);
        return true;
      } else {
        toast.error(data.message || "Failed to send otp your email");
        return false;
      }
    } catch (error) {
      console.error(error);
      toast.error(
        error.response?.data?.message ||
          "An error occurred while sending email to the user"
      );
      return false;
    }
  };

  /**
   * Verify OTP entered by user
   * @param {string} email - User's email address
   * @param {string} otp - OTP code
   * @returns {boolean} - Verification status
   */
  const verifyOtp = async (email, otp) => {
    try {
      const { data } = await axios.post(
        `${backendUrl}/api/forgotpassword/verify-otp`,
        {},
        { params: { email, otp } }
      );
      if (data.statusCode == 200) {
        toast.success(data.message);
        return true;
      } else {
        toast.error(data.message || "Wrong OTP provided");
        return false;
      }
    } catch (error) {
      console.error(error);
      toast.error(
        error.response?.data?.message || "An error occurred verify otp"
      );
      return false;
    }
  };

  /**
   * Reset user password
   * @param {string} email - User's email address
   * @param {string} otp - Verified OTP code
   * @param {string} newPassword - New password
   * @returns {boolean} - Success status
   */
  const resetPassword = async (email, otp, newPassword) => {
    try {
      const { data } = await axios.post(
        `${backendUrl}/api/forgotpassword/reset-password`,
        {},
        { params: { email, newPassword, otp } }
      );
      if (data.statusCode == 200) {
        toast.success(data.message);
        return true;
      } else {
        toast.error(data.message || "Wrong OTP provided");
        return false;
      }
    } catch (error) {
      console.error(error);
      toast.error(error.response?.data?.message || "Error resetting password");
      return false;
    }
  };

  // ===================== USER PROFILE FUNCTIONS =====================

  /**
   * Load user profile data from backend
   */
  const loadUserProfileData = async () => {
    try {
      const { data } = await axios.get(backendUrl + `/api/patients`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      console.log(data);
      console.log(token);

      if (data.success) {
        setUserData(data.patientDto);
      }
    } catch (error) {
      console.log(error.message);
    }
    console.log(userData);
  };

  /**
   * update user profile data from backend
   */
  const updateUserProfileData = async (updatedData) => {
    setLoading(true);
    try {
      const { data } = await axios.put(
        backendUrl + `/api/patients/${patientId}`,
        updatedData,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (data.success) {
        setUserData(data.patientDto);
        loadUserProfileData();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    } finally {
      setLoading(false);
    }
  };

  // ===================== DOCTOR FUNCTIONS =====================

  /**
   * Fetch all doctors data from backend
   */
  const getDoctosData = async () => {
    setLoading(true);
    try {
      const { data } = await axios.get(backendUrl + "/api/doctors");

      if (data.statusCode === 200) {
        if (data.doctorDtos && data.doctorDtos.length > 0) {
          setDoctors(data.doctorDtos);
        } else {
          setDoctors([]);
        }
      } else {
        toast.error(data.message || "Failed to fetch doctors data");
        setDoctors([]);
      }
    } catch (error) {
      console.error("Error fetching doctors:", error);
      toast.error(
        error.response?.data?.message ||
          error.message ||
          "Failed to load doctors. Please try again later."
      );
      setDoctors([]);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Fetch specific doctor's information
   * @param {string} docId - Doctor ID
   */
  const fetchDocInfo = async (docId) => {
    try {
      setLoading(true);
      const { data } = await axios.get(
        `${backendUrl}/api/doctors/getDoctor/${docId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (data.statusCode == 200) {
        setDocInfo(data.doctorDto);
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    } finally {
      setLoading(false);
    }
  };

  // ===================== APPOINTMENT FUNCTIONS =====================

  /**
   * Book a new appointment
   * @param {Object} bookingData - Appointment details
   */
  const bookAppointment = async (bookingData) => {
    console.log(bookingData);
    try {
      const { data } = await axios.post(
        backendUrl + "/api/appointments",
        bookingData,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (data.success) {
        toast.success(data.message);
        getDoctosData();
        navigate("/my-appointments");
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);

      toast.error(error.message);
    }
  };

  /**
   * Fetch user's appointments
   */
  const getUserAppointments = async () => {
    try {
      const { data } = await axios.get(
        backendUrl + `/api/patients/appointments`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setAppointments(data.appointmentDtos.reverse());
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    }
  };

  /**
   * Cancel an existing appointment
   * @param {string} appointmentId - ID of appointment to cancel
   */
  const cancelAppointment = async (appointmentId) => {
    try {
      const { data } = await axios.post(
        backendUrl + `/api/appointments/${appointmentId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (data.statusCode == 200) {
        toast.success(data.message);
        getUserAppointments();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    }
  };

  // ===================== PAYMENT FUNCTIONS =====================

  /**
   * Initiate Stripe payment for appointment
   * @param {string} appointmentId - ID of appointment to pay for
   */
  const appointmentStripe = async (appointmentId) => {
    try {
      const { data } = await axios.post(
        backendUrl + "/api/pay",
        { appointmentId },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      console.log("The data is : " + data.statusCode);
      if (data.statusCode == 200) {
        const { session_url } = data.data;
        window.location.replace(session_url);
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);
      toast.error(error.response?.data?.message || error.message);
    }
  };

  /**
   * Verify Stripe payment status
   * @param {boolean} success - Payment success status
   * @param {string} appointmentId - ID of appointment
   */
  const verifyStripe = async (success, appointmentId) => {
    try {
      const { data } = await axios.post(
        backendUrl + "/api/verify",
        { success, appointmentId },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (data.statusCode == 200) {
        toast.success(data.message);
        navigate("/my-appointments");
        console.log("appointments" + data.appointmentDto);
      } else {
        toast.error(data.message);
        console.log(data.message);
      }
    } catch (error) {
      toast.error(error.message);
      console.log(error);
    }
  };

  // Add to your existing AppContext
  const rescheduleAppointment = async (appointmentId, rescheduleData) => {
    try {
      const { data } = await axios.put(
        backendUrl + `/api/appointments/${appointmentId}/reschedule`,
        rescheduleData,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (data.success) {
        toast.success("Appointment rescheduled successfully!");
        getUserAppointments();
        return true;
      } else {
        toast.error(data.message);
        return false;
      }
    } catch (error) {
      console.error("Reschedule error:", error);
      toast.error(
        error.response?.data?.message || "Failed to reschedule appointment"
      );
      return false;
    }
  };

  // ===================== EFFECT HOOKS =====================

  useEffect(() => {
    // Skip auth-related effects on auth pages
    const isAuthPage = ["/login", "/register", "/forgotpassword"].some((path) =>
      location.pathname.includes(path)
    );

    if (token && !isAuthPage) {
      loadUserProfileData();
    }
  }, [token, location.pathname]);

  useEffect(() => {
    // Skip doctor loading on auth pages
    const isAuthPage = ["/login", "/register", "/forgotpassword"].some((path) =>
      location.pathname.includes(path)
    );

    if (!isAuthPage) {
      getDoctosData();
    }
  }, [location.pathname]);

  // ===================== CONTEXT VALUE =====================

  const value = {
    // Authentication
    registerPatient,
    loginPatient,
    sendOtp,
    verifyOtp,
    resetPassword,

    // Doctors
    doctors,
    getDoctosData,
    docInfo,
    setDocInfo,
    fetchDocInfo,

    // Appointments
    bookAppointment,
    getUserAppointments,
    cancelAppointment,
    appointments,
    rescheduleAppointment,

    // Payments
    appointmentStripe,
    verifyStripe,

    // State
    loading,
    currencySymbol,
    backendUrl,
    token,
    setToken,
    userData,
    setUserData,
    loadUserProfileData,
    updateUserProfileData,
    setPatientId,
    patientId,
  };

  return (
    <AppContext.Provider value={value}>{props.children}</AppContext.Provider>
  );
};

export default AppContextProvider;
