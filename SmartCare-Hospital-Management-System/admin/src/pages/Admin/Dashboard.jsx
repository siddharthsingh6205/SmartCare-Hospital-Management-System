import React, { useContext, useEffect } from "react";
import { assets } from "../../assets/assets";
import { AdminContext } from "../../context/AdminContext";
import { AppContext } from "../../context/AppContext";
import {
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  LineChart,
  Line,
} from "recharts";

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#8884D8"];

const Dashboard = () => {
  const { aToken, getDashData, dashData } = useContext(AdminContext);
  const { slotDateFormat } = useContext(AppContext);

  useEffect(() => {
    if (aToken) {
      getDashData();
    }
  }, [aToken]);

  if (!dashData) return <div className="m-5">Loading...</div>;

  // Prepare data for charts
  const appointmentStatusData = [
    {
      name: "Completed",
      value: dashData.appointmentStatusCount?.COMPLETED || 0,
    },
    { name: "Pending", value: dashData.appointmentStatusCount?.SCHEDULED || 0 },
    {
      name: "Cancelled",
      value: dashData.appointmentStatusCount?.CANCELLED || 0,
    },
  ];

  const approvalStatusData = [
    { name: "Approved", value: dashData.approvalStatusCount?.APPROVED || 0 },
    { name: "Pending", value: dashData.approvalStatusCount?.PENDING || 0 },
    { name: "Rejected", value: dashData.approvalStatusCount?.REJECTED || 0 },
  ];

  const monthlyRevenueData =
    dashData.monthlyRevenue?.map((item) => ({
      name: item.month,
      revenue: item.amount,
    })) || [];

  const doctorPerformanceData =
    dashData.topDoctors?.map((doctor) => ({
      name: doctor.name,
      appointments: doctor.appointmentCount,
      revenue: doctor.totalEarnings,
    })) || [];

  return (
    <div className="m-5">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">
        Dashboard Overview
      </h1>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-5 mb-8">
        <div className="bg-white p-6 rounded-lg shadow-md border-l-4 border-blue-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 font-medium">Total Doctors</p>
              <p className="text-3xl font-bold text-gray-800">
                {dashData.doctorCount}
              </p>
            </div>
            <div className="p-3 bg-blue-100 rounded-full">
              <img className="w-8" src={assets.doctor_icon} alt="Doctors" />
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md border-l-4 border-green-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 font-medium">Total Patients</p>
              <p className="text-3xl font-bold text-gray-800">
                {dashData.patientCount}
              </p>
            </div>
            <div className="p-3 bg-green-100 rounded-full">
              <img className="w-8" src={assets.patients_icon} alt="Patients" />
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md border-l-4 border-purple-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 font-medium">Total Revenue</p>
              <p className="text-3xl font-bold text-gray-800">
                ₹{dashData.totalRevenue || 0}
              </p>
            </div>
            <div className="p-3 bg-purple-100 rounded-full">
              <img className="w-8" src={assets.revenue_icon} alt="Revenue" />
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md border-l-4 border-yellow-500">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 font-medium">Total Appointments</p>
              <p className="text-3xl font-bold text-gray-800">
                {dashData.appointmentCount}
              </p>
            </div>
            <div className="p-3 bg-yellow-100 rounded-full">
              <img
                className="w-8"
                src={assets.appointments_icon}
                alt="Appointments"
              />
            </div>
          </div>
          <p className="text-sm text-gray-500 mt-2">
            {dashData.todaysAppointments || 0} today
          </p>
        </div>
      </div>

      {/* Appointment Status Distribution */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-lg font-semibold text-gray-800 mb-4">
          Appointment Status
        </h2>
        <div className="h-80">
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={appointmentStatusData}
                cx="50%"
                cy="50%"
                labelLine={false}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
                nameKey="name"
                label={({ name, percent }) =>
                  `${name}: ${(percent * 100).toFixed(0)}%`
                }
              >
                {appointmentStatusData.map((entry, index) => (
                  <Cell
                    key={`cell-${index}`}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <Tooltip formatter={(value) => [value, "Appointments"]} />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Second Row of Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        {/* Doctor Performance */}
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">
            Top Performing Doctors
          </h2>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={doctorPerformanceData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis yAxisId="left" orientation="left" stroke="#8884d8" />
                <YAxis yAxisId="right" orientation="right" stroke="#82ca9d" />
                <Tooltip
                  formatter={(value, name) =>
                    name === "revenue"
                      ? [`₹${value}`, "Revenue"]
                      : [value, "Appointments"]
                  }
                />
                <Legend />
                <Bar
                  yAxisId="left"
                  dataKey="appointments"
                  fill="#8884d8"
                  name="Appointments"
                />
                <Bar
                  yAxisId="right"
                  dataKey="revenue"
                  fill="#82ca9d"
                  name="Revenue"
                />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Approval Status */}
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">
            Approval Status
          </h2>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={approvalStatusData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                  nameKey="name"
                  label={({ name, percent }) =>
                    `${name}: ${(percent * 100).toFixed(0)}%`
                  }
                >
                  {approvalStatusData.map((entry, index) => (
                    <Cell
                      key={`cell-${index}`}
                      fill={COLORS[index % COLORS.length]}
                    />
                  ))}
                </Pie>
                <Tooltip formatter={(value) => [value, "Appointments"]} />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Recent Appointments */}
      <div className="bg-white rounded-lg shadow-md overflow-hidden mb-8">
        <div className="flex items-center justify-between px-6 py-4 border-b">
          <div className="flex items-center gap-3">
            <img src={assets.list_icon} alt="" className="w-5" />
            <h2 className="text-lg font-semibold text-gray-800">
              Recent Appointments
            </h2>
          </div>
          <button className="text-sm text-blue-600 hover:text-blue-800">
            View All
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Patient
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Doctor
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Date & Time
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Amount
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {dashData.latestAppointments.slice(0, 5).map((item, index) => (
                <tr key={index} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-shrink-0 h-10 w-10">
                        <img
                          className="h-10 w-10 rounded-full"
                          src={`data:image/jpeg;base64,${item.patientImage}`}
                          alt=""
                        />
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          {item.patientName}
                        </div>
                        <div className="text-sm text-gray-500">
                          {item.patientEmail}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">
                      {item.doctorName}
                    </div>
                    <div className="text-sm text-gray-500">
                      {item.doctorSpecialization}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">
                      {slotDateFormat(item.slotDate)}
                    </div>
                    <div className="text-sm text-gray-500">{item.time}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ₹{item.amount}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {item.cancelled ? (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                        Cancelled
                      </span>
                    ) : item.completed ? (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                        Completed
                      </span>
                    ) : item.approvalStatus === "APPROVED" ? (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                        Approved
                      </span>
                    ) : item.approvalStatus === "PENDING" ? (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">
                        Pending
                      </span>
                    ) : item.approvalStatus === "REJECTED" ? (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-yellow-800">
                        Rejected
                      </span>
                    ) : (
                      <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                        Unknown
                      </span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">
            Payment Status
          </h3>
          <div className="space-y-4">
            <div className="flex justify-between">
              <span className="text-gray-600">Total Paid</span>
              <span className="font-medium">
                ₹{dashData.paymentStatus?.paid || 0}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">Pending Payments</span>
              <span className="font-medium">
                ₹{dashData.paymentStatus?.pending || 0}
              </span>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">
            Doctor Specializations
          </h3>
          <div className="space-y-3">
            {dashData.specializationDistribution?.map((spec, index) => (
              <div key={index}>
                <div className="flex justify-between mb-1">
                  <span className="text-gray-600">{spec.name}</span>
                  <span className="font-medium">{spec.count}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className="bg-blue-600 h-2 rounded-full"
                    style={{
                      width: `${(spec.count / dashData.doctorCount) * 100}%`,
                    }}
                  ></div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">
            System Health
          </h3>
          <div className="space-y-4">
            <div className="flex justify-between">
              <span className="text-gray-600">Active Users Today</span>
              <span className="font-medium">
                {dashData.systemHealth?.activeUsers || 0}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">Appointment Rate</span>
              <span className="font-medium">
                {dashData.systemHealth?.appointmentRate &&
                !isNaN(Number(dashData.systemHealth.appointmentRate))
                  ? `${Number(dashData.systemHealth.appointmentRate).toFixed(
                      1
                    )}%`
                  : "0%"}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
