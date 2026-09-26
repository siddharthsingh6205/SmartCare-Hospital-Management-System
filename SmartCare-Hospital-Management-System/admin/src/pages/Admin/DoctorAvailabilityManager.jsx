import React, { useState, useEffect, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Button,
  Card,
  Divider,
  TimePicker,
  Select,
  Row,
  Col,
  message,
} from "antd";
import {
  SaveOutlined,
  ArrowLeftOutlined,
  DeleteOutlined,
} from "@ant-design/icons";
import dayjs from "dayjs";
import weekday from "dayjs/plugin/weekday";
import { AdminContext } from "../../context/AdminContext";

dayjs.extend(weekday);

const DoctorAvailabilityManager = () => {
  const { doctorId } = useParams();
  const navigate = useNavigate();
  const { doctor, getDoctorAvailability, updateDoctorAvailability } =
    useContext(AdminContext);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [slots, setSlots] = useState({
    Monday: [],
    Tuesday: [],
    Wednesday: [],
    Thursday: [],
    Friday: [],
    Saturday: [],
    Sunday: [],
  });
  const [newSlot, setNewSlot] = useState({
    day: "Monday",
    startTime: null,
    endTime: null,
  });

  // Convert API slots format to day-based format
  const convertApiSlotsToDays = (apiSlots = {}) => {
    const days = {
      Monday: [],
      Tuesday: [],
      Wednesday: [],
      Thursday: [],
      Friday: [],
      Saturday: [],
      Sunday: [],
    };

    Object.entries(apiSlots).forEach(([date, timeSlots]) => {
      const dayOfWeek = dayjs(date).format("dddd");
      if (days[dayOfWeek] && timeSlots) {
        days[dayOfWeek] = [...new Set([...days[dayOfWeek], ...timeSlots])].sort(
          (a, b) => a.localeCompare(b)
        );
      }
    });

    return days;
  };

  // Convert day-based format back to API format
  const convertDaysToApiSlots = (daysSlots) => {
    const result = {};
    const today = dayjs();

    for (let i = 0; i < 7; i++) {
      const date = today.add(i, "day").format("YYYY-MM-DD");
      const dayName = dayjs(date).format("dddd");
      const daySlots = daysSlots[dayName] || [];

      if (daySlots.length > 0) {
        result[date] = daySlots;
      }
    }

    return result;
  };

  useEffect(() => {
    const fetchAvailability = async () => {
      try {
        setLoading(true);
        await getDoctorAvailability(doctorId);
      } catch (error) {
        message.error("Failed to fetch doctor availability");
      } finally {
        setLoading(false);
      }
    };

    fetchAvailability();
  }, [doctorId]);

  useEffect(() => {
    if (doctor?.availableSlots) {
      setSlots(convertApiSlotsToDays(doctor.availableSlots));
    }
  }, [doctor]);

  const handleAddTimeRange = () => {
    if (!newSlot.startTime || !newSlot.endTime) {
      message.warning("Please select both start and end time");
      return;
    }

    const start = newSlot.startTime.format("HH:mm");
    const end = newSlot.endTime.format("HH:mm");

    // Generate 30-minute intervals between start and end
    const timeSlots = [];
    let currentTime = newSlot.startTime;

    while (currentTime.isBefore(newSlot.endTime)) {
      timeSlots.push(currentTime.format("HH:mm"));
      currentTime = currentTime.add(30, "minute");
    }

    setSlots((prev) => ({
      ...prev,
      [newSlot.day]: [...new Set([...prev[newSlot.day], ...timeSlots])].sort(),
    }));

    setNewSlot({ ...newSlot, startTime: null, endTime: null });
  };

  const handleRemoveSlot = (day, time) => {
    setSlots((prev) => ({
      ...prev,
      [day]: prev[day].filter((t) => t !== time),
    }));
  };

  const handleSaveSlots = async () => {
    try {
      setSaving(true);
      const slotsToSave = convertDaysToApiSlots(slots);
      console.log(slotsToSave);
      await updateDoctorAvailability(doctorId, slotsToSave);
      message.success("Availability saved successfully");
      await getDoctorAvailability(doctorId);
    } catch (error) {
      message.error(
        error.response?.data?.message || "Failed to save availability"
      );
    } finally {
      setSaving(false);
    }
  };

  const daysOfWeek = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ];

  return (
    <div className="p-4">
      <Button
        type="text"
        icon={<ArrowLeftOutlined />}
        onClick={() => navigate("/doctor-list")}
        className="mb-4"
      >
        Back to Doctors
      </Button>

      <Card
        title={`Manage Availability for Dr. ${doctor?.user?.name || ""}`}
        loading={loading}
        extra={
          <Button
            type="primary"
            icon={<SaveOutlined />}
            onClick={handleSaveSlots}
            loading={saving}
          >
            Save Changes
          </Button>
        }
      >
        <div className="mb-6">
          <h3 className="text-lg font-medium mb-4">Add Time Slots</h3>
          <div className="flex flex-wrap gap-4 items-end">
            <div>
              <label className="block text-sm font-medium mb-1">Day</label>
              <Select
                value={newSlot.day}
                onChange={(day) => setNewSlot({ ...newSlot, day })}
                style={{ width: 150 }}
                options={daysOfWeek.map((day) => ({ value: day, label: day }))}
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">From</label>
              <TimePicker
                format="HH:mm"
                minuteStep={30}
                value={newSlot.startTime}
                onChange={(time) => setNewSlot({ ...newSlot, startTime: time })}
                style={{ width: 120 }}
                disabledHours={() => [
                  0, 1, 2, 3, 4, 5, 6, 7,8, 18, 19, 20, 21, 22, 23,
                ]}
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">To</label>
              <TimePicker
                format="HH:mm"
                minuteStep={30}
                value={newSlot.endTime}
                onChange={(time) => setNewSlot({ ...newSlot, endTime: time })}
                style={{ width: 120 }}
                disabledHours={() => [
                  0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23,
                ]}
              />
            </div>

            <Button type="primary" onClick={handleAddTimeRange}>
              Add Time Range
            </Button>
          </div>
        </div>

        <Divider orientation="left">Weekly Schedule</Divider>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {daysOfWeek.map((day) => (
            <Card key={day} title={day} size="small" className="h-full">
              {slots[day]?.length > 0 ? (
                <div className="space-y-2">
                  {slots[day].map((time) => (
                    <div
                      key={`${day}-${time}`}
                      className="flex justify-between items-center p-2 bg-gray-50 rounded"
                    >
                      <span className="font-medium">{time}</span>
                      <Button
                        type="text"
                        danger
                        icon={<DeleteOutlined />}
                        onClick={() => handleRemoveSlot(day, time)}
                        size="small"
                      />
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-gray-500">No slots added</p>
              )}
            </Card>
          ))}
        </div>
      </Card>
    </div>
  );
};

export default DoctorAvailabilityManager;
