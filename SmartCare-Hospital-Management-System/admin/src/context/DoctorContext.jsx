import { createContext, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

export const DoctorContext = createContext();

const DoctorContextProvider = (props) => {
  const backendUrl = import.meta.env.VITE_BACKEND_URL;

  const [dToken, setDToken] = useState(localStorage.getItem("dToken") || null);
  const [doctorId, setDoctorId] = useState(0);
  const [appointments, setAppointments] = useState([]);
  const [dashData, setDashData] = useState(false);
  const [profileData, setProfileData] = useState(false);
  const [pendingAppointments, setPendingAppointments] = useState([]);

  // Getting Doctor appointment data from Database using API
  const getAppointments = async (doctorId) => {
    try {
      const { data } = await axios.get(
        `${backendUrl}/api/doctor/appointments`,
        {
          headers: {
            Authorization: `Bearer ${dToken}`, // Use Bearer scheme if that's the expected format
          },
        }
      );

      if (data.statusCode == 200) {
        setAppointments(data.appointmentDtos.reverse());
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    }
  };

  // Getting Doctor profile data from Database using API
  const getProfileData = async () => {
    try {
      const { data } = await axios.get(
        backendUrl + `/api/doctors/doctor/get-user-profile`,
        {
          headers: {
            Authorization: `Bearer ${dToken}`, // Use Bearer scheme if that's the expected format
          },
        }
      );

      console.log(data.doctorDto);
      setProfileData(data.doctorDto);
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    }
  };

  // Function to cancel doctor appointment using API
  const cancelAppointment = async (appointmentId) => {
    try {
      const { data } = await axios.post(
        backendUrl + `/api/appointments/${appointmentId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${dToken}`, // Use Bearer scheme if that's the expected format
          },
        }
      );
      if (data.statusCode == 200) {
        toast.success(data.message);
        getAppointments();
        // after creating dashboard
        getDashData();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      toast.error(error.message);
      console.log(error);
    }
  };

  // Function to Mark appointment completed using API
  const completeAppointment = async (appointmentId) => {
    try {
      const { data } = await axios.put(
        `${backendUrl}/api/doctors/appointments/${appointmentId}/complete`,
        {}, // <-- this tells axios to send no body
        {
          headers: {
            Authorization: `Bearer ${dToken}`,
          },
        }
      );

      if (data.success) {
        toast.success(data.message);
        // Later after creating getDashData Function
        getDashData();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      toast.error(error.message);
      console.log(error);
    }
  };

  //getPendingAppointments
  const getPendingAppointments = async () => {
    try {
      const { data } = await axios.get(
        `${backendUrl}/api/doctors/pendingAppointments`,
        { headers: { Authorization: `Bearer ${dToken}` } }
      );
      setPendingAppointments(data.appointmentDtos);
    } catch (error) {
      console.error("Error fetching pending appointments:", error);
      toast.error(
        error.response?.data?.message || "Failed to fetch pending appointments"
      );
    }
  };

  //ApproveAppointments
  const approveAppointment = async (appointmentId) => {
    try {
      const { data } = await axios.put(
        `${backendUrl}/api/doctors/appointments/${appointmentId}/approve`,
        null,
        { headers: { Authorization: `Bearer ${dToken}` } }
      );

      if (data.success) {
        toast.success(
          data.appointmentDto.approvalStatus == "APPROVED"
            ? "Appointment approved!"
            : "Appointment rejected"
        );
        getPendingAppointments();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.error("Approval error:", error);
      toast.error(
        error.response?.data?.message || "Failed to process approval"
      );
    }
  };

  //RejectAppointment
  const rejectAppointment = async (appointmentId) => {
    try {
      const { data } = await axios.put(
        `${backendUrl}/api/doctors/appointments/${appointmentId}/reject`,
        null,
        { headers: { Authorization: `Bearer ${dToken}` } }
      );

      if (data.success) {
        toast.success(
          data.appointmentDto.approvalStatus == "REJECTED"
            ? "Appointment rejected!"
            : "Appointment approved"
        );
        getPendingAppointments();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.error("Approval error:", error);
      toast.error(
        error.response?.data?.message || "Failed to process approval"
      );
    }
  };

  // Getting Doctor dashboard data using API
  const getDashData = async () => {
    try {
      const { data } = await axios.get(
        backendUrl + "/api/doctors/doctor/dashboard",
        {
          headers: {
            Authorization: `Bearer ${dToken}`,
          },
        }
      );

      if (data.success) {
        setDashData(data.doctorDashboard);
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);
      toast.error(data.message);
    }
  };

  const updateProfile = async () => {
    try {
      const updateData = {
        address: profileData.address,
        fees: profileData.fees,
        aboutDoctor: profileData.aboutDoctor,
        availability: profileData.availability,
      };

      const { data } = await axios.put(
        `${backendUrl}/api/doctors/doctor/update-profile`,
        updateData,
        {
          headers: {
            Authorization: `Bearer ${dToken}`,
          },
        }
      );

      if (data.statusCode === 200) {
        toast.success(data.message);
        await getProfileData();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      toast.error(error.response?.data?.message || error.message);
      console.error(error);
    }
  };

  const value = {
    dToken,
    setDToken,
    backendUrl,
    dashData,
    getDashData,
    doctorId,
    setDoctorId,
    pendingAppointments,
    setPendingAppointments,

    appointments,
    getAppointments,
    cancelAppointment,
    completeAppointment,
    approveAppointment,
    rejectAppointment,
    getPendingAppointments,

    profileData,
    setProfileData,
    getProfileData,
    updateProfile,
  };

  return (
    <DoctorContext.Provider value={value}>
      {props.children}
    </DoctorContext.Provider>
  );
};

export default DoctorContextProvider;
