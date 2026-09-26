import dayjs from "dayjs";

const BookingSlots = ({
  slots,
  selectedDate,
  selectedTime,
  setSelectedDate,
  setSelectedTime,
  handleBookAppointment,
  loading,
}) => {
  const now = dayjs();

  const filteredSlots = slots.filter((slot) => {
    // For today's date, filter times that are after now
    if (slot.date === now.format("YYYY-MM-DD")) {
      const availableTimes = slot.times.filter((time) => {
        const slotTime = dayjs(`${slot.date} ${time}`, "YYYY-MM-DD HH:mm");
        return slotTime.isAfter(now);
      });
      return availableTimes.length > 0;
    }
    // For other days, just check if there are any times
    return slot.times.length > 0;
  });

  return (
    <div className="sm:ml-72 sm:pl-4 mt-4 font-medium text-gray-700">
      <p>Booking slots</p>
      <div className="flex gap-3 items-center w-full overflow-x-scroll mt-4">
        {filteredSlots.length > 0 ? (
          filteredSlots.map((slot) => (
            <div
              onClick={() => {
                if (!loading) {
                  setSelectedDate(slot.date);
                  setSelectedTime(null);
                }
              }}
              key={slot.date}
              className={`text-center py-6 min-w-16 rounded-full cursor-pointer transition-colors ${
                selectedDate === slot.date
                  ? "bg-primary text-white"
                  : "border border-gray-200 hover:border-primary"
              } ${loading ? "opacity-50 pointer-events-none" : ""}`}
            >
              <p>{slot.dayName}</p>
              <p>{slot.dayNumber}</p>
            </div>
          ))
        ) : (
          <p className="text-sm font-light text-gray-400">
            No available booking slots
          </p>
        )}
      </div>

      {/* Time Selection */}
      {selectedDate && (
        <div className="flex items-center gap-3 w-full overflow-x-scroll mt-4">
          {(() => {
            const selectedSlot = slots.find((s) => s.date === selectedDate);
            if (!selectedSlot) return null;

            const filteredTimes = selectedSlot.times.filter((time) => {
              const slotTime = dayjs(
                `${selectedDate} ${time}`,
                "YYYY-MM-DD HH:mm"
              );

              if (selectedDate === now.format("YYYY-MM-DD")) {
                return slotTime.isAfter(now);
              }
              return true;
            });

            return filteredTimes.map((time) => (
              <p
                onClick={() => setSelectedTime(time)}
                key={time}
                className={`text-sm font-light flex-shrink-0 px-5 py-2 rounded-full cursor-pointer ${
                  selectedTime === time
                    ? "bg-primary text-white"
                    : "text-gray-400 border border-gray-300"
                }`}
              >
                {time.toLowerCase()}
              </p>
            ));
          })()}
        </div>
      )}

      <button
        onClick={handleBookAppointment}
        disabled={!selectedDate || !selectedTime || loading}
        className={`bg-primary text-white text-sm font-light px-14 py-3 rounded-full my-6 transition-opacity ${
          !selectedDate || !selectedTime || loading
            ? "opacity-50 cursor-not-allowed"
            : "hover:opacity-90"
        }`}
      >
        {loading ? (
          <span className="flex items-center justify-center gap-2">
            <svg
              className="animate-spin h-4 w-4 text-white"
              viewBox="0 0 24 24"
            >
              <circle
                className="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                strokeWidth="4"
              ></circle>
              <path
                className="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
              ></path>
            </svg>
            Booking...
          </span>
        ) : (
          "Book an appointment"
        )}
      </button>
    </div>
  );
};

export default BookingSlots;
