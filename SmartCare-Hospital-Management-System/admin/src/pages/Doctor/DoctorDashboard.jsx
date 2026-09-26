import React, { useContext, useEffect, useState } from "react";
import { DoctorContext } from "../../context/DoctorContext";
import { assets } from "../../assets/assets";
import { AppContext } from "../../context/AppContext";
import { Bar, Pie, Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  PointElement,
  LineElement,
} from "chart.js";

// Register ChartJS components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  PointElement,
  LineElement
);

const DoctorDashboard = () => {
  const {
    dToken,
    dashData,
    getDashData,
    cancelAppointment,
    completeAppointment,
  } = useContext(DoctorContext);
  const { slotDateFormat, currency } = useContext(AppContext);
  const [activeTab, setActiveTab] = useState("overview");
  useEffect(() => {
    if (!dToken) {
      // Check storage as fallback
      const storedToken = localStorage.getItem("doctorToken");
      if (storedToken) {
        getDashData();
      } else {
        navigate("/doctor/login");
      }
    } else {
      getDashData();
    }
  }, [dToken]);

  if (!dToken && !localStorage.getItem("doctorToken")) {
    return <div>Redirecting to login...</div>;
  }

  if (!dashData) return <div className="m-5">Loading...</div>;

  // Chart data preparations
  const appointmentStatusData = {
    labels: ["Completed", "Cancelled", "Pending"],
    datasets: [
      {
        label: "Appointments",
        data: [
          dashData.completedAppointments || 0,
          dashData.cancelledAppointments || 0,
          dashData.pendingAppointments || 0,
        ],
        backgroundColor: [
          "rgba(75, 192, 192, 0.6)",
          "rgba(255, 99, 132, 0.6)",
          "rgba(255, 206, 86, 0.6)",
        ],
        borderColor: [
          "rgba(75, 192, 192, 1)",
          "rgba(255, 99, 132, 1)",
          "rgba(255, 206, 86, 1)",
        ],
        borderWidth: 1,
      },
    ],
  };

  const weeklyEarningsData = {
    labels: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
    datasets: [
      {
        label: "Earnings",
        data: dashData.weeklyEarnings || [0, 0, 0, 0, 0, 0, 0],
        backgroundColor: "rgba(54, 162, 235, 0.5)",
        borderColor: "rgba(54, 162, 235, 1)",
        borderWidth: 2,
        tension: 0.4,
      },
    ],
  };

  const slotStatusData = {
    labels: ["Available", "Pending", "Approved"],
    datasets: [
      {
        label: "Slots Status",
        data: [
          dashData.availableSlotsCount || 0,
          dashData.pendingSlotsCount || 0,
          dashData.approvedSlotsCount || 0,
        ],
        backgroundColor: [
          "rgba(54, 162, 235, 0.6)",
          "rgba(255, 206, 86, 0.6)",
          "rgba(75, 192, 192, 0.6)",
        ],
        borderColor: [
          "rgba(54, 162, 235, 1)",
          "rgba(255, 206, 86, 1)",
          "rgba(75, 192, 192, 1)",
        ],
        borderWidth: 1,
      },
    ],
  };

  const todayStatsData = {
    labels: ["Approved", "Rejected", "Pending"],
    datasets: [
      {
        label: "Today's Actions",
        data: [
          dashData.todayApproved || 0,
          dashData.todayRejected || 0,
          dashData.todayPending || 0,
        ],
        backgroundColor: [
          "rgba(75, 192, 192, 0.6)",
          "rgba(255, 99, 132, 0.6)",
          "rgba(255, 206, 86, 0.6)",
        ],
        borderColor: [
          "rgba(75, 192, 192, 1)",
          "rgba(255, 99, 132, 1)",
          "rgba(255, 206, 86, 1)",
        ],
        borderWidth: 1,
      },
    ],
  };

  return (
    <div className="m-5">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Doctor Dashboard</h1>
        <div className="flex space-x-2">
          <button
            onClick={() => setActiveTab("overview")}
            className={`px-4 py-2 rounded-lg ${
              activeTab === "overview"
                ? "bg-primary text-white"
                : "bg-gray-200 text-gray-700"
            }`}
          >
            Overview
          </button>
          <button
            onClick={() => setActiveTab("appointments")}
            className={`px-4 py-2 rounded-lg ${
              activeTab === "appointments"
                ? "bg-primary text-white"
                : "bg-gray-200 text-gray-700"
            }`}
          >
            Appointments
          </button>
          <button
            onClick={() => setActiveTab("slots")}
            className={`px-4 py-2 rounded-lg ${
              activeTab === "slots"
                ? "bg-primary text-white"
                : "bg-gray-200 text-gray-700"
            }`}
          >
            Slots
          </button>
        </div>
      </div>

      {activeTab === "overview" && (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
            <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
              <div className="flex items-center">
                <div className="p-3 rounded-full bg-blue-100 mr-4">
                  <img
                    className="w-8"
                    src={assets.earning_icon}
                    alt="Earnings"
                  />
                </div>
                <div>
                  <p className="text-gray-500 text-sm">Total Earnings</p>
                  <p className="text-2xl font-bold text-gray-800">
                    {currency} {dashData.earnings}
                  </p>
                </div>
              </div>
            </div>

            <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
              <div className="flex items-center">
                <div className="p-3 rounded-full bg-green-100 mr-4">
                  <img
                    className="w-8"
                    src={assets.appointments_icon}
                    alt="Appointments"
                  />
                </div>
                <div>
                  <p className="text-gray-500 text-sm">Total Appointments</p>
                  <p className="text-2xl font-bold text-gray-800">
                    {dashData.appointments}
                  </p>
                </div>
              </div>
            </div>

            <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
              <div className="flex items-center">
                <div className="p-3 rounded-full bg-purple-100 mr-4">
                  <img
                    className="w-8"
                    src={assets.patients_icon}
                    alt="Patients"
                  />
                </div>
                <div>
                  <p className="text-gray-500 text-sm">Total Patients</p>
                  <p className="text-2xl font-bold text-gray-800">
                    {dashData.patients}
                  </p>
                </div>
              </div>
            </div>

            <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
              <div className="flex items-center">
                <div className="p-3 rounded-full bg-yellow-100 mr-4">
                  <img
                    className="w-10 h-10"
                    src={assets.slot_icon}
                    alt="Slots"
                  />
                </div>
                <div>
                  <p className="text-gray-500 text-sm">Available Slots</p>
                  <p className="text-2xl font-bold text-gray-800">
                    {dashData.availableSlotsCount}
                  </p>
                </div>
              </div>
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
            <div className="bg-white p-6 rounded-xl shadow-md">
              <h3 className="text-lg font-semibold mb-4">
                Appointments Status
              </h3>
              <div className="h-64">
                <Pie
                  data={appointmentStatusData}
                  options={{
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                      legend: {
                        position: "right",
                      },
                    },
                  }}
                />
              </div>
            </div>

            <div className="bg-white p-6 rounded-xl shadow-md">
              <h3 className="text-lg font-semibold mb-4">Weekly Earnings</h3>
              <div className="h-64">
                <Line
                  data={weeklyEarningsData}
                  options={{
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                      y: {
                        beginAtZero: true,
                      },
                    },
                  }}
                />
              </div>
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="bg-white p-6 rounded-xl shadow-md">
              <h3 className="text-lg font-semibold mb-4">Today's Activity</h3>
              <div className="h-64">
                <Bar
                  data={todayStatsData}
                  options={{
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                      y: {
                        beginAtZero: true,
                      },
                    },
                  }}
                />
              </div>
            </div>

            <div className="bg-white p-6 rounded-xl shadow-md">
              <h3 className="text-lg font-semibold mb-4">Slots Status</h3>
              <div className="h-64">
                <Pie
                  data={slotStatusData}
                  options={{
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                      legend: {
                        position: "right",
                      },
                    },
                  }}
                />
              </div>
            </div>
          </div>
        </>
      )}

      {activeTab === "appointments" && (
        <div className="bg-white rounded-xl shadow-md overflow-hidden">
          <div className="px-6 py-4 border-b">
            <h2 className="text-xl font-semibold text-gray-800">
              Recent Appointments
            </h2>
          </div>
          <div className="divide-y">
            {dashData.latestAppointments
              .slice()
              .reverse()
              .map((item, index) => (
                <div
                  className="flex items-center px-6 py-4 hover:bg-gray-50 transition-colors"
                  key={index}
                >
                  <img
                    className="rounded-full w-12 h-12 object-cover"
                    src={`data:image/jpeg;base64,${item.patientImage}`}
                    alt={item.patientName}
                  />
                  <div className="ml-4 flex-1">
                    <div className="flex justify-between items-start">
                      <div>
                        <p className="font-medium text-gray-900">
                          {item.patientName}
                        </p>
                        <p className="text-sm text-gray-500">
                          {slotDateFormat(item.slotDate)} at {item.time}
                        </p>
                      </div>
                      <div>
                        {item.cancelled ? (
                          <span className="px-2 py-1 text-xs rounded-full bg-red-100 text-red-800">
                            Cancelled
                          </span>
                        ) : item.completed ? (
                          <span className="px-2 py-1 text-xs rounded-full bg-green-100 text-green-800">
                            Completed
                          </span>
                        ) : (
                          <div className="flex space-x-2">
                            <button
                              onClick={() => completeAppointment(item.id)}
                              className="p-2 rounded-full bg-green-100 text-green-600 hover:bg-green-200 transition-colors"
                            >
                              <img
                                className="w-5"
                                src={assets.tick_icon}
                                alt="Approve"
                              />
                            </button>
                            <button
                              onClick={() => cancelAppointment(item.id)}
                              className="p-2 rounded-full bg-red-100 text-red-600 hover:bg-red-200 transition-colors"
                            >
                              <img
                                className="w-5"
                                src={assets.cancel_icon}
                                alt="Reject"
                              />
                            </button>
                          </div>
                        )}
                      </div>
                    </div>
                    <div className="mt-2 flex justify-between text-sm">
                      <span className="text-gray-600">
                        Amount: {currency}
                        {item.amount}
                      </span>
                      <span
                        className={`font-medium ${
                          item.payment ? "text-green-600" : "text-yellow-600"
                        }`}
                      >
                        {item.payment
                          ? item.cancelled
                            ? "Payment returned"
                            : "Paid"
                          : ""}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
          </div>
        </div>
      )}

      {activeTab === "slots" && (
        <div className="bg-white rounded-xl shadow-md overflow-hidden">
          <div className="px-6 py-4 border-b">
            <h2 className="text-xl font-semibold text-gray-800">
              Slot Management
            </h2>
          </div>
          <div className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
              <div className="border rounded-lg p-4">
                <h3 className="font-medium text-gray-700 mb-2">
                  Available Slots
                </h3>
                <div className="space-y-2 max-h-60 overflow-y-auto">
                  {dashData.availableSlots &&
                    Object.entries(dashData.availableSlots).map(
                      ([date, times]) => (
                        <div key={date} className="mb-3">
                          <p className="font-medium text-sm text-gray-600">
                            {date}
                          </p>
                          <div className="flex flex-wrap gap-2 mt-1">
                            {Array.from(times).map((time, i) => (
                              <span
                                key={i}
                                className="px-2 py-1 text-xs bg-blue-100 text-blue-800 rounded"
                              >
                                {time}
                              </span>
                            ))}
                          </div>
                        </div>
                      )
                    )}
                </div>
              </div>

              <div className="border rounded-lg p-4">
                <h3 className="font-medium text-gray-700 mb-2">
                  Pending Approval
                </h3>
                <div className="space-y-2 max-h-60 overflow-y-auto">
                  {dashData.pendingSlots &&
                    Object.entries(dashData.pendingSlots).map(
                      ([date, times]) => (
                        <div key={date} className="mb-3">
                          <p className="font-medium text-sm text-gray-600">
                            {date}
                          </p>
                          <div className="flex flex-wrap gap-2 mt-1">
                            {Array.from(times).map((time, i) => (
                              <span
                                key={i}
                                className="px-2 py-1 text-xs bg-yellow-100 text-yellow-800 rounded"
                              >
                                {time}
                              </span>
                            ))}
                          </div>
                        </div>
                      )
                    )}
                </div>
              </div>

              <div className="border rounded-lg p-4">
                <h3 className="font-medium text-gray-700 mb-2">
                  Approved Slots
                </h3>
                <div className="space-y-2 max-h-60 overflow-y-auto">
                  {dashData.approvedSlots &&
                    Object.entries(dashData.approvedSlots).map(
                      ([date, times]) => (
                        <div key={date} className="mb-3">
                          <p className="font-medium text-sm text-gray-600">
                            {date}
                          </p>
                          <div className="flex flex-wrap gap-2 mt-1">
                            {Array.from(times).map((time, i) => (
                              <span
                                key={i}
                                className="px-2 py-1 text-xs bg-green-100 text-green-800 rounded"
                              >
                                {time}
                              </span>
                            ))}
                          </div>
                        </div>
                      )
                    )}
                </div>
              </div>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="font-medium text-gray-700 mb-3">
                Slot Statistics
              </h3>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div className="bg-white p-3 rounded-lg shadow-sm">
                  <p className="text-sm text-gray-500">Total Available</p>
                  <p className="text-xl font-bold">
                    {dashData.availableSlotsCount || 0}
                  </p>
                </div>
                <div className="bg-white p-3 rounded-lg shadow-sm">
                  <p className="text-sm text-gray-500">Pending Approval</p>
                  <p className="text-xl font-bold">
                    {dashData.pendingSlotsCount || 0}
                  </p>
                </div>
                <div className="bg-white p-3 rounded-lg shadow-sm">
                  <p className="text-sm text-gray-500">Approved</p>
                  <p className="text-xl font-bold">
                    {dashData.approvedSlotsCount || 0}
                  </p>
                </div>
                <div className="bg-white p-3 rounded-lg shadow-sm">
                  <p className="text-sm text-gray-500">Booked Today</p>
                  <p className="text-xl font-bold">
                    {dashData.todayBookedSlots || 0}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DoctorDashboard;
