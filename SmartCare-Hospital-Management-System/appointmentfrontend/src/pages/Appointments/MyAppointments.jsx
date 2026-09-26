import React, { useContext, useEffect, useState } from "react";
import { AppContext } from "../../context/AppContext";
import { Oval } from "react-loader-spinner";
import axios from "axios";

const MyAppointments = () => {
  const {
    appointments,
    getUserAppointments,
    cancelAppointment,
    appointmentStripe,
    rescheduleAppointment,
    token,
    backendUrl,
  } = useContext(AppContext);

  const [loading, setLoading] = useState(true);
  const [rescheduleData, setRescheduleData] = useState(null);
  const [showRescheduleForm, setShowRescheduleForm] = useState(false);
  const [newDate, setNewDate] = useState("");
  const [newTime, setNewTime] = useState("");
  const [availableSlots, setAvailableSlots] = useState([]);
  const [loadingSlots, setLoadingSlots] = useState(false);
  const [cancellingAppointmentId, setCancellingAppointmentId] = useState(null);
  const [payingAppointmentId, setPayingAppointmentId] = useState(null);
  const [reschedulingAppointmentId, setReschedulingAppointmentId] =
    useState(null);
  const [error, setError] = useState(null);

  const slotDateFormat = (dateString) => {
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString("en-GB", {
        day: "2-digit",
        month: "short",
        year: "numeric",
      });
    } catch (e) {
      console.error("Error formatting date:", e);
      return dateString; // fallback to raw string if formatting fails
    }
  };

  const fetchAvailableSlots = async (doctorId, date) => {
    try {
      setLoadingSlots(true);
      setNewTime("");
      setError(null);

      const { data } = await axios.get(
        `${backendUrl}/api/admin/doctors/${doctorId}/availability`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          timeout: 10000, // 10 seconds timeout
        }
      );

      if (!data?.doctorDto?.availableSlots) {
        throw new Error("Invalid data structure received");
      }

      const slotsData = data.doctorDto.availableSlots;
      const formattedSlots = [];
      const slotsForDate = slotsData[date] || [];

      slotsForDate.forEach((time) => {
        try {
          const [hours, minutes] = time.split(":");
          const hourNum = parseInt(hours, 10);
          const period = hourNum >= 12 ? "PM" : "AM";
          const displayHour = hourNum % 12 === 0 ? 12 : hourNum % 12;
          formattedSlots.push({
            display: `${displayHour}:${minutes} ${period}`,
            backend: time,
          });
        } catch (e) {
          console.error("Error formatting time slot:", time, e);
        }
      });

      formattedSlots.sort((a, b) => a.backend.localeCompare(b.backend));
      setAvailableSlots(formattedSlots);
    } catch (error) {
      console.error("Error fetching available slots:", error);
      setError("Failed to load available time slots. Please try again.");
      setAvailableSlots([]);
    } finally {
      setLoadingSlots(false);
    }
  };

  const handleReschedule = async (appointment) => {
    try {
      setReschedulingAppointmentId(appointment.id);
      setRescheduleData(appointment);
      const initialDate = appointment.slotDate.split("T")[0];
      setNewDate(initialDate);
      setNewTime("");
      await fetchAvailableSlots(appointment.doctorId, initialDate);
      setShowRescheduleForm(true);
    } catch (error) {
      console.error("Error preparing reschedule:", error);
      setError("Failed to prepare reschedule. Please try again.");
    } finally {
      setReschedulingAppointmentId(null);
    }
  };

  const handleDateChange = async (e) => {
    const selectedDate = e.target.value;
    setNewDate(selectedDate);
    if (rescheduleData && selectedDate) {
      await fetchAvailableSlots(rescheduleData.doctorId, selectedDate);
    }
  };

  const submitReschedule = async () => {
    if (!rescheduleData || !newDate || !newTime) {
      setError("Please select both date and time");
      return;
    }

    try {
      const selectedSlot = availableSlots.find(
        (slot) => slot.display === newTime
      );

      if (!selectedSlot) {
        throw new Error("Invalid time slot selected");
      }

      await rescheduleAppointment(rescheduleData.id, {
        newDate: newDate,
        newTime: selectedSlot.backend,
      });

      setShowRescheduleForm(false);
      setNewDate("");
      setNewTime("");
      await getUserAppointments();
    } catch (error) {
      console.error("Reschedule failed:", error);
      setError("Reschedule failed. Please try again.");
    }
  };

  const handlePayment = async (appointmentId) => {
    try {
      setPayingAppointmentId(appointmentId);
      await appointmentStripe(appointmentId);
      await getUserAppointments();
    } catch (error) {
      console.error("Payment failed:", error);
      setError("Payment failed. Please try again.");
    } finally {
      setPayingAppointmentId(null);
    }
  };

  const handleCancelAppointment = async (appointmentId) => {
    try {
      setCancellingAppointmentId(appointmentId);
      await cancelAppointment(appointmentId);
      await getUserAppointments();
    } catch (error) {
      console.error("Cancellation failed:", error);
      setError("Cancellation failed. Please try again.");
    } finally {
      setCancellingAppointmentId(null);
    }
  };

  const loadAppointments = async () => {
    try {
      setLoading(true);
      setError(null);
      await getUserAppointments();
    } catch (error) {
      console.error("Failed to load appointments:", error);
      setError("Failed to load appointments. Please refresh the page.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (token) {
      loadAppointments();
    }
  }, [token]);

  return (
    <div className="p-4">
      <p className="pb-3 mt-12 text-lg font-medium text-gray-600 border-b">
        My appointments
      </p>

      {error && (
        <div className="p-4 mb-4 text-red-600 bg-red-100 rounded">
          {error}
          <button
            onClick={() => setError(null)}
            className="float-right font-bold"
          >
            ×
          </button>
        </div>
      )}

      {showRescheduleForm && rescheduleData && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg w-full max-w-md">
            <h3 className="text-lg font-medium mb-4">Reschedule Appointment</h3>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                New Date
              </label>
              <input
                type="date"
                className="w-full p-2 border rounded"
                min={new Date().toISOString().split("T")[0]}
                onChange={handleDateChange}
                value={newDate}
                required
              />
            </div>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                New Time
              </label>
              {loadingSlots ? (
                <div className="flex items-center justify-center p-4">
                  <Oval height={24} width={24} color="#4fa94d" />
                </div>
              ) : availableSlots.length > 0 ? (
                <select
                  className="w-full p-2 border rounded"
                  value={newTime}
                  onChange={(e) => setNewTime(e.target.value)}
                  required
                >
                  <option value="">Select a time slot</option>
                  {availableSlots.map((slot, index) => (
                    <option key={index} value={slot.display}>
                      {slot.display}
                    </option>
                  ))}
                </select>
              ) : (
                <p className="text-gray-500">
                  No available slots for this date
                </p>
              )}
            </div>
            <div className="flex justify-end gap-2">
              <button
                onClick={() => {
                  setShowRescheduleForm(false);
                  setError(null);
                }}
                className="px-4 py-2 text-sm text-gray-600 border rounded hover:bg-gray-100"
              >
                Cancel
              </button>
              <button
                onClick={submitReschedule}
                disabled={!newDate || !newTime}
                className={`px-4 py-2 text-sm text-white rounded flex items-center justify-center min-w-32 ${
                  !newDate || !newTime
                    ? "bg-gray-400"
                    : "bg-primary hover:bg-primary-dark"
                }`}
              >
                {reschedulingAppointmentId === rescheduleData.id ? (
                  <Oval height={20} width={20} color="white" />
                ) : (
                  "Confirm Reschedule"
                )}
              </button>
            </div>
          </div>
        </div>
      )}

      <div className="mt-4">
        {loading ? (
          <div className="flex justify-center items-center py-12">
            <Oval height={40} width={40} color="#4fa94d" />
          </div>
        ) : appointments.length === 0 ? (
          <div className="text-center py-8">
            <p className="text-gray-500">You have no appointments yet.</p>
            <button
              onClick={loadAppointments}
              className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              Refresh
            </button>
          </div>
        ) : (
          appointments.map((item, index) => (
            <div
              key={`${item.id}-${index}`}
              className="grid grid-cols-[1fr_2fr] gap-4 sm:flex sm:gap-6 py-4 border-b"
            >
              <div className="w-36 h-36 bg-[#EAEFFF] flex items-center justify-center">
                {item.image ? (
                  <img
                    className="w-full h-full object-cover"
                    src={`data:image/png;base64,${item.image}`}
                    alt="Doctor"
                    onError={(e) => {
                      e.target.onerror = null;
                      e.target.src = "placeholder-doctor.png"; // fallback image
                    }}
                  />
                ) : (
                  <div className="text-gray-400">No Image</div>
                )}
              </div>
              <div className="flex-1 text-sm text-[#5E5E5E]">
                <p className="text-[#262626] text-base font-semibold">
                  {item.doctorName}
                </p>
                <p>Age: {item.age}</p>
                <p className="text-[#464646] font-medium mt-1">Status:</p>
                <p
                  className={`font-medium ${
                    item.approvalStatus === "PENDING"
                      ? "text-yellow-600"
                      : item.approvalStatus === "APPROVED"
                      ? "text-blue-600"
                      : item.appointmentStatus === "COMPLETED"
                      ? "text-green-600"
                      : item.appointmentStatus === "CANCELLED"
                      ? "text-red-600"
                      : item.approvalStatus === "REJECTED"
                      ? "text-red-600"
                      : ""
                  }`}
                >
                  {item.appointmentStatus === "CANCELLED" ? (
                    <span className="text-red-600 font-medium">
                      {item.appointmentStatus}
                    </span>
                  ) : (
                    <span
                      className={`font-medium ${
                        item.approvalStatus === "PENDING"
                          ? "text-yellow-600"
                          : item.approvalStatus === "APPROVED"
                          ? "text-blue-600"
                          : item.approvalStatus === "REJECTED"
                          ? "text-red-600"
                          : ""
                      }`}
                    >
                      {item.approvalStatus}
                      {item.approvalStatus === "PENDING" &&
                        " (Waiting for doctor approval)"}
                    </span>
                  )}
                </p>
                <p className="mt-1">
                  <span className="text-sm text-[#3C3C3C] font-medium">
                    Date & Time:
                  </span>
                  <span className="ml-1">
                    {slotDateFormat(item.slotDate)}, {item.time}
                  </span>
                </p>
                {item.payment && (
                  <p className="mt-1">
                    <span className="text-sm text-[#3C3C3C] font-medium">
                      Payment Status:
                    </span>
                    <span className="ml-1 text-green-600">Paid</span>
                  </p>
                )}
              </div>
              <div className="flex flex-col gap-2 justify-end text-sm text-center">
                {item.approvalStatus === "APPROVED" &&
                  item.appointmentStatus === "SCHEDULED" && (
                    <>
                      {!item.payment ? (
                        <button
                          onClick={() => handlePayment(item.id)}
                          disabled={payingAppointmentId === item.id}
                          className={`text-[#696969] sm:min-w-48 py-2 border rounded flex items-center justify-center ${
                            payingAppointmentId === item.id
                              ? "bg-gray-200"
                              : "hover:bg-[#5f6fff] hover:text-white"
                          }`}
                        >
                          {payingAppointmentId === item.id ? (
                            <Oval height={20} width={20} color="#696969" />
                          ) : (
                            "Pay Online"
                          )}
                        </button>
                      ) : (
                        <button className="sm:min-w-48 py-2 border rounded text-[#696969] bg-[#EAEFFF]">
                          Payment Completed
                        </button>
                      )}
                      <button
                        onClick={() => handleReschedule(item)}
                        disabled={reschedulingAppointmentId === item.id}
                        className={`text-[#696969] sm:min-w-48 py-2 border rounded flex items-center justify-center ${
                          reschedulingAppointmentId === item.id
                            ? "bg-gray-200"
                            : "hover:bg-gray-200"
                        }`}
                      >
                        {reschedulingAppointmentId === item.id ? (
                          <Oval height={20} width={20} color="#696969" />
                        ) : (
                          "Reschedule"
                        )}
                      </button>
                      <button
                        onClick={() => handleCancelAppointment(item.id)}
                        disabled={cancellingAppointmentId === item.id}
                        className={`text-[#696969] sm:min-w-48 py-2 border rounded flex items-center justify-center ${
                          cancellingAppointmentId === item.id
                            ? "bg-gray-200"
                            : "hover:bg-red-600 hover:text-white"
                        }`}
                      >
                        {cancellingAppointmentId === item.id ? (
                          <Oval height={20} width={20} color="#696969" />
                        ) : (
                          "Cancel Appointment"
                        )}
                      </button>
                    </>
                  )}

                {item.appointmentStatus === "COMPLETED" && (
                  <button className="sm:min-w-48 py-2 border border-green-500 rounded text-green-500">
                    Appointment Completed
                  </button>
                )}

                {item.appointmentStatus === "CANCELLED" && (
                  <button className="sm:min-w-48 py-2 border border-red-500 rounded text-red-500">
                    Appointment Cancelled
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default MyAppointments;
