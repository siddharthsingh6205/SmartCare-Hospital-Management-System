import { useContext, useEffect, useState } from "react";
import { AppContext } from "../../context/AppContext";
import { useParams } from "react-router-dom";
import dayjs from "dayjs";

export const useAppointment = () => {
  const { docId } = useParams();
  const {
    doctors,
    fetchDocInfo,
    docInfo,
    bookAppointment,
    patientId,
    getDoctosData,
    loading,
  } = useContext(AppContext);

  const [availableSlots, setAvailableSlots] = useState({});
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState(null);

  // Format slots data for UI display
  const formatSlotsData = (slots) => {
    return Object.entries(slots)
      .sort(([dateA], [dateB]) => dayjs(dateA).diff(dayjs(dateB)))
      .map(([date, times]) => ({
        date,
        dayName: dayjs(date).format("ddd"),
        dayNumber: dayjs(date).date(),
        times: times.sort(),
      }));
  };

  // Prepare booking data for API
  const prepareBookingData = () => {
    if (!selectedDate || !selectedTime) {
      throw new Error("Please select both date and time");
    }

    return {
      doctorId: docId,
      date: selectedDate,
      time: selectedTime,
      patientId: patientId,
    };
  };

  useEffect(() => {
    if (doctors.length > 0) {
      fetchDocInfo(docId);
    }
  }, [doctors, docId]);

  useEffect(() => {
    if (docInfo?.availableSlots) {
      setAvailableSlots(docInfo.availableSlots);
      // Auto-select first available date if none selected
      if (!selectedDate && Object.keys(docInfo.availableSlots).length > 0) {
        setSelectedDate(Object.keys(docInfo.availableSlots)[0]);
      }
    }
  }, [docInfo]);
  return {
    docInfo,
    slots: formatSlotsData(availableSlots),
    selectedDate,
    selectedTime,
    setSelectedDate,
    setSelectedTime,
    prepareBookingData,
    loading,
    docId,
  };
};
