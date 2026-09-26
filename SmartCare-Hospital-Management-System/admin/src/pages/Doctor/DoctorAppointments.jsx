import React from "react";
import { useContext, useEffect, useState } from "react";
import { DoctorContext } from "../../context/DoctorContext";
import { AppContext } from "../../context/AppContext";

const DoctorAppointments = () => {
  const {
    dToken,
    pendingAppointments,
    approveAppointment,
    rejectAppointment,
    getPendingAppointments,
  } = useContext(DoctorContext);
  const { slotDateFormat, currency } = useContext(AppContext);

  const [loadingStates, setLoadingStates] = useState({});
  const [overallLoading, setOverallLoading] = useState(false);

  useEffect(() => {
    if (dToken) {
      setOverallLoading(true);
      getPendingAppointments().finally(() => setOverallLoading(false));
    }
  }, [dToken]);

  const handleAction = async (action, appointmentId) => {
    // If already processing any action for this appointment, return
    if (loadingStates[appointmentId]) return;

    setLoadingStates((prev) => ({ ...prev, [appointmentId]: action }));

    try {
      if (action === "approve") {
        await approveAppointment(appointmentId);
      } else if (action === "reject") {
        await rejectAppointment(appointmentId);
      }
      await getPendingAppointments();
    } catch (error) {
      console.error("Error performing action:", error);
    } finally {
      setLoadingStates((prev) => {
        const newState = { ...prev };
        delete newState[appointmentId];
        return newState;
      });
    }
  };

  return (
    <div className="w-full max-w-6xl m-5">
      <p className="mb-3 text-lg font-medium">All Appointments</p>

      <div className="bg-white border rounded text-sm max-h-[80vh] overflow-y-scroll">
        <div className="max-sm:hidden grid grid-cols-[0.5fr_2fr_1fr_1fr_1.5fr_2fr] gap-1 py-3 px-6 border-b">
          <p>#</p>
          <p>Patient</p>
          <div>Doctor</div>
          <p>Date & Time</p>
          <p>Fees</p>
          <p>Actions</p>
        </div>

        {overallLoading ? (
          <div className="p-4 text-center text-gray-500">
            <div className="inline-block animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-blue-500 mb-2"></div>
            <p>Loading appointments...</p>
          </div>
        ) : pendingAppointments && pendingAppointments.length > 0 ? (
          pendingAppointments.map((item, index) => {
            const isProcessing = loadingStates[item.id];
            const isApproving = isProcessing === "approve";
            const isRejecting = isProcessing === "reject";

            return (
              <div
                className="flex flex-wrap justify-between max-sm:gap-5 max-sm:text-base sm:grid grid-cols-[0.5fr_2fr_1fr_1fr_1.5fr_2fr] gap-1 items-center text-gray-500 py-3 px-6 border-b hover:bg-gray-50"
                key={index}
              >
                <p className="max-sm:hidden">{index + 1}</p>
                <div className="flex items-center gap-2">
                  <img
                    src={`data:image/jpeg;base64,${item.patientImage}`}
                    className="w-8 h-8 object-cover rounded-full"
                    alt="Patient"
                  />
                  <p className="font-medium">{item.patientName}</p>
                </div>
                <div className="font-medium">{item.doctorName}</div>

                <p>
                  {slotDateFormat(item.slotDate)}, {item.time}
                </p>
                <p className="font-medium">
                  {currency}
                  {item.amount}
                </p>
                <div className="flex gap-2 flex-wrap">
                  <button
                    onClick={() => handleAction("approve", item.id)}
                    disabled={isProcessing}
                    className={`px-3 py-1 rounded text-sm ${
                      isProcessing
                        ? isApproving
                          ? "bg-green-200 text-green-800"
                          : "bg-gray-200 text-gray-500 cursor-not-allowed"
                        : "bg-green-100 text-green-800 hover:bg-green-200"
                    }`}
                  >
                    {isApproving ? "Processing..." : "Approve"}
                  </button>

                  <button
                    onClick={() => handleAction("reject", item.id)}
                    disabled={isProcessing}
                    className={`px-3 py-1 rounded text-sm ${
                      isProcessing
                        ? isRejecting
                          ? "bg-red-200 text-red-800"
                          : "bg-gray-200 text-gray-500 cursor-not-allowed"
                        : "bg-red-100 text-red-800 hover:bg-red-200"
                    }`}
                  >
                    {isRejecting ? "Processing..." : "Reject"}
                  </button>
                </div>
              </div>
            );
          })
        ) : (
          <div className="p-4 text-center text-gray-500">
            No pending appointments for approval
          </div>
        )}
      </div>
    </div>
  );
};

export default DoctorAppointments;
