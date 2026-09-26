import React, { useContext, useMemo } from "react";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Routes, Route, Navigate, useLocation, Outlet } from "react-router-dom";
import { AppContext } from "./context/AppContext";
import Register from "./pages/Register/Register";
import Login from "./pages/Login/Login";
import ForgotPassword from "./pages/ForgotPassword/ForgotPassword";
import About from "./pages/About/About";
import Contact from "./pages/Contact/Contact";
import Home from "./pages/Home/Home";
import Navbar from "./components/Common/Navbar";
import Footer from "./components/Common/Footer";
import Doctors from "./pages/Doctors/Doctors";
import MyAppointments from "./pages/Appointments/MyAppointments";
import Appointment from "./pages/Appointments/Appointment";
import Verify from "./pages/payment/Verify";
import MyProfile from "./pages/UserProfile/MyProfile";

const App = () => {
  const location = useLocation();

  // Memoize the auth page check
  const isAuthPage = useMemo(
    () =>
      ["/login", "/register", "/forgotPassword"].includes(location.pathname),
    [location.pathname]
  );

  return (
    <div className=" min-h-screen flex flex-col">
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
      {!isAuthPage && (
        <div className="mx-4 sm:mx-[10%] ">
          <Navbar />
        </div>
      )}

      <main className="flex-grow mx-4 sm:mx-[10%]">
        <Routes>
          {/* Public Routes */}
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/forgotPassword" element={<ForgotPassword />} />
          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/doctors" element={<Doctors />} />
          <Route path="/doctors/:specialization" element={<Doctors />} />
          <Route path="/" element={<Home />} />

          {/* Protected Routes */}
          <Route element={<ProtectedRoutes />}>
            <Route path="/appointment/:docId" element={<Appointment />} />
            <Route path="/my-appointments" element={<MyAppointments />} />
            <Route path="/my-profile" element={<MyProfile />} />
            <Route path="/verify" element={<Verify />} />
          </Route>

          {/* Catch-all route */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      {!isAuthPage && <Footer />}
    </div>
  );
};


const ProtectedRoutes = () => {
  const { token } = useContext(AppContext);
  const location = useLocation();

  if (!token) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <Outlet />;
};

export default React.memo(App);
