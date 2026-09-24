import React, { useContext } from "react";
import "react-toastify/dist/ReactToastify.css";
import { Route, Routes, Navigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { DoctorContext } from "./context/DoctorContext";
import { AdminContext } from "./context/AdminContext";
import Login from "./pages/Login/Login";
import ForgotPassword from "./pages/ForgotPassword/ForgotPassword";
import Navbar from "./components/common/Navbar";
import Sidebar from "./components/common/sidebar/Sidebar";

import Dashboard from "./pages/Admin/Dashboard";
import DoctorDashboard from "./pages/Doctor/DoctorDashboard";
import AllAppointments from "./pages/Admin/AllAppointments";
import DoctorsList from "./pages/Admin/DoctorsList";

import DoctorProfile from "./pages/Doctor/DoctorProfile";
import DoctorAppointments from "./pages/Doctor/DoctorAppointments";

import DoctorAvailabilityManager from "./pages/Admin/DoctorAvailabilityManager";
import AddDoctor from "./pages/Admin/AddDoctor";
import RememberMe from "./components/Login/RememberMe";

const App = () => {
  const { dToken } = useContext(DoctorContext);
  const { aToken } = useContext(AdminContext);

  return dToken || aToken ? (
    <div className="bg-[#F8F9FD]">
      <ToastContainer />
      <Navbar />
      <div className="flex items-start">
        <Sidebar />
        <Routes>
          <Route path="/" element={<></>} />
          <Route
            path="/admin-dashboard"
            element={
              aToken ? <Dashboard /> : <Navigate to="/doctor-dashboard" />
            }
          />
          <Route
            path="/doctor-dashboard"
            element={
              dToken ? <DoctorDashboard /> : <Navigate to="/admin-dashboard" />
            }
          />
          <Route
            path="/all-appointments"
            element={aToken ? <AllAppointments /> : <Navigate to="/" />}
          />
          <Route
            path="/doctor-list"
            element={aToken ? <DoctorsList /> : <Navigate to="/" />}
          />
          <Route
            path="/doctor-list/:doctorId/availability"
            element={
              aToken ? <DoctorAvailabilityManager /> : <Navigate to="/" />
            }
          />
          <Route
            path="/add-doctor"
            element={aToken ? <AddDoctor /> : <Navigate to="/" />}
          />

          <Route
            path="/doctor-appointments"
            element={dToken ? <DoctorAppointments /> : <Navigate to="/" />}
          />
          <Route
            path="/doctor-profile"
            element={dToken ? <DoctorProfile /> : <Navigate to="/" />}
          />
          <Route
            path="*"
            element={
              <Navigate
                to={dToken ? "/doctor-dashboard" : "/admin-dashboard"}
              />
            }
          />
        </Routes>
      </div>
    </div>
  ) : (
    <>
      <ToastContainer />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/forgotPassword" element={<ForgotPassword />} />
        <Route path="./RememberMe" element={<RememberMe />} />
      </Routes>
    </>
  );
};

export default App;
