import axios from "axios";
import { createContext, useContext, useState } from "react";
import { toast } from "react-toastify";
import { DoctorContext } from "./DoctorContext";

export const AdminContext = createContext();

const AdminContextProvider = (props) => {
  const backendUrl = import.meta.env.VITE_BACKEND_URL;
  const [adminId, setAdminId] = useState(0);
  const [doctor, setDoctor] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [dashData, setDashData] = useState(null);
  const [admins, setAdmins] = useState([]);
  const [aToken, setAToken] = useState(
    localStorage.getItem("aToken") ? localStorage.getItem("aToken") : ""
  );

  //ADD ADMIN
  const registerAdmin = async (adminData, adminImage) => {
    try {
      if (!adminImage) {
        throw new Error("Image Not Selected");
      }

      const adminObj = {
        user: {
          name: adminData.name,
          email: adminData.email,
          password: adminData.password,
        },
      };

      const formData = new FormData();
      formData.append("admin", JSON.stringify(adminObj));
      formData.append("image", adminImage);

      const { data } = await axios.post(
        `${backendUrl}/api/admin/register/admin`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${aToken}`,
          },
        }
      );

      if (data.statusCode === 200) {
        toast.success(data.message);
        return { success: true };
      } else {
        throw new Error(data.message);
      }
    } catch (error) {
      toast.error(error.response?.data?.message || error.message);
      console.error("Admin registration error:", error);
      return { success: false, error: error.message };
    }
  };

  //ADD DOCTOR
  const registerDoctor = async (doctorData, doctorImage) => {
    try {
      if (!doctorImage) {
        throw new Error("Image Not Selected");
      }

      const doctorObj = {
        specialization: doctorData.speciality,
        contactNumber: doctorData.contactNumber,
        experience: doctorData.experience,
        degree: doctorData.degree,
        fees: Number(doctorData.fees),
        aboutDoctor: doctorData.about,
        address1: {
          line1: doctorData.address1,
          line2: doctorData.address2,
        },
        user: {
          name: doctorData.name,
          email: doctorData.email,
          password: doctorData.password,
        },
      };

      const formData = new FormData();
      formData.append("doctor", JSON.stringify(doctorObj));
      formData.append("image", doctorImage);

      const { data } = await axios.post(
        `${backendUrl}/api/admin/register/doctor`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${aToken}`,
          },
        }
      );

      if (data.statusCode === 200) {
        toast.success(data.message);
        return { success: true };
      } else {
        throw new Error(data.message);
      }
    } catch (error) {
      toast.error(error.response?.data?.message || error.message);
      console.error("Doctor registration error:", error);
      return { success: false, error: error.message };
    }
  };

  // Getting all Doctors data from Database using API
  const getAllDoctors = async () => {
    try {
      const { data } = await axios.get(backendUrl + "/api/doctors"); // Send GET request to the backend

      if (data && data.doctorDtos) {
        // Check if data and doctorDtos are availableA
        setDoctors(data.doctorDtos); // Update the state with doctors data
      } else {
        toast.error(data.message || "Failed to fetch doctors data"); // Display error if doctorDtos is missing
      }
    } catch (error) {
      // Log error for debugging and show error message to the user
      console.error(error);
      toast.error(
        error.response?.data?.message ||
          "An error occurred while fetching doctors"
      );
    }
  };

  // Getting all Admin data from Database using API
  const getAllAdmins = async () => {
    try {
      const { data } = await axios.get(backendUrl + "/api/admin/allAdmins", {
        headers: {
          Authorization: `Bearer ${aToken}`, // Use Bearer scheme if that's the expected format
        },
      }); // Send GET request to the backend

      if (data && data.adminDtos) {
        // Check if data and doctorDtos are availableA
        setAdmins(data.adminDtos); // Update the state with doctors data
      } else {
        toast.error(data.message || "Failed to fetch admin data"); // Display error if doctorDtos is missing
      }
    } catch (error) {
      // Log error for debugging and show error message to the user
      console.error(error);
      toast.error(
        error.response?.data?.message ||
          "An error occurred while fetching doctors"
      );
    }
  };

  // Function to change doctor availablity using API
  const changeAvailability = async (docId) => {
    try {
      const { data } = await axios.put(
        `${backendUrl}/api/doctors/${docId}/availability`,
        {},
        {
          headers: {
            Authorization: `Bearer ${aToken}`, // Use Bearer scheme if that's the expected format
          },
        }
      );
      if (data.statusCode == 200) {
        toast.success(data.message);
        getAllDoctors();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    }
  };

  // Getting all appointment data from Database using API
  const getAllAppointments = async () => {
    try {
      const { data } = await axios.get(backendUrl + "/api/appointments", {
        headers: {
          Authorization: `Bearer ${aToken}`, // Use Bearer scheme if that's the expected format
        },
      });

      if (data.statusCode === 200) {
        setAppointments(data.appointmentDtos.reverse());
      } else {
        toast.error(data.message || "Failed to fetch appointments");
      }
    } catch (error) {
      toast.error(error.message || "Something went wrong");
      console.log(error);
    }
  };

  // Function to cancel appointment using API
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

  const getDoctorAvailability = async (doctorId) => {
    try {
      const { data } = await axios.get(
        `${backendUrl}/api/admin/doctors/${doctorId}/availability`,
        {
          headers: { Authorization: `Bearer ${aToken}` },
        }
      );

      // Ensure the response structure matches what we expect
      if (data.success && data.doctorDto) {
        setDoctor(data.doctorDto);
      }
    } catch (error) {
      console.error("Failed to fetch doctor availability:", error);
      toast.error("Failed to get Doctor available slots");
    }
  };

  const updateDoctorAvailability = async (doctorId, weeklySlots) => {
    try {
      const { data } = await axios.post(
        `${backendUrl}/api/admin/doctors/${doctorId}/availability`,
        weeklySlots,
        {
          headers: {
            Authorization: `Bearer ${aToken}`,
            "Content-Type": "application/json",
          },
        }
      );

      // Handle successful update
      if (data.success) {
        setDoctor(data.doctorDto);
        // Show success message
        toast.success("Doctor availability updated successfully");
      }
    } catch (error) {
      console.error("Failed to fetch doctor availability:", error);
      toast.error("Failed to get Doctor available slots");
    }
  };

  // Getting Admin Dashboard data from Database using API
  const getDashData = async () => {
    try {
      const { data } = await axios.get(backendUrl + "/api/admin/dashboard", {
        headers: {
          Authorization: `Bearer ${aToken}`, // Use Bearer scheme if that's the expected format
        },
      });

      if (data.statusCode == 200) {
        setDashData(data.dashboardData);
      } else {
        toast.error(data.message || "Failed to load dashboard data");
      }
    } catch (error) {
      console.error(error);
      const errorMessage =
        error.response?.status === 403
          ? "You don't have permission to view this data. Please contact support."
          : error.message ||
            "Failed to load dashboard. Please try again later.";
      toast.error(errorMessage);
      setDashData(null); // Clear any existing data
    }
  };

  const value = {
    registerDoctor,
    registerAdmin,
    adminId,
    setAdminId,
    aToken,
    setAToken,
    rejectAppointment,

    doctors,
    getAllDoctors,
    changeAvailability,
    appointments,
    getAllAppointments,
    getDashData,
    dashData,
    admins,
    getAllAdmins,
    getDoctorAvailability,
    updateDoctorAvailability,
    doctor,
    setDoctor,
  };

  return (
    <AdminContext.Provider value={value}>
      {props.children}
    </AdminContext.Provider>
  );
};

export default AdminContextProvider;
