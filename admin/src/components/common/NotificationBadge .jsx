import React, { useContext, useEffect, useState } from "react";
import { Bell } from "lucide-react";
import { DoctorContext } from "../../context/DoctorContext";
import axios from "axios";

const NotificationBadge = () => {
  const { dToken, backendUrl } = useContext(DoctorContext);
  const [count, setCount] = useState(0);
  const [notifications, setNotifications] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);

  useEffect(() => {
    if (dToken) {
      const fetchData = async () => {
        try {
   
          const [countRes, notificationsRes] = await Promise.all([
            axios.get(`${backendUrl}/api/notifications/count`, {
              headers: { Authorization: `Bearer ${dToken}` },
            }),
            axios.get(`${backendUrl}/api/notifications`, {
              headers: { Authorization: `Bearer ${dToken}` },
            }),
          ]);

          setCount(countRes.data.count);
          setNotifications(notificationsRes.data.notificationDtos);
        } catch (error) {
          console.error("Error fetching notifications:", error);
        }
      };

      fetchData();

      // Set up polling (every 30 seconds)
      const interval = setInterval(fetchData, 30000);
      return () => clearInterval(interval);
    }
  }, [dToken]);

  const markAsRead = async (notificationId) => {
    try {
      await axios.put(
        `${backendUrl}/api/notifications/${notificationId}/read`,
        {},
        { headers: { Authorization: `Bearer ${dToken}` } }
      );
      setNotifications(
        notifications.map((n) =>
          n.id === notificationId ? { ...n, isRead: true } : n
        )
      );
      setCount((prev) => (prev > 0 ? prev - 1 : 0));
    } catch (error) {
      console.error("Error marking notification as read:", error);
    }
  };

  const markAllAsRead = async () => {
    try {
      // Mark all as read on the server
      await axios.put(
        `${backendUrl}/api/notifications/mark-all-read`,
        {},
        { headers: { Authorization: `Bearer ${dToken}` } }
      );

      // Update local state
      setNotifications(notifications.map((n) => ({ ...n, isRead: true })));
      setCount(0);

      // Hide the dropdown immediately
      setShowDropdown(false);
    } catch (error) {
      console.error("Error marking all as read:", error);
    }
  };

  return (
    <div className="relative">
      <button
        onClick={() => setShowDropdown(!showDropdown)}
        className="p-1 rounded-full hover:bg-gray-100 relative"
      >
        <Bell className="w-6 h-6 text-gray-600" />
        {count > 0 && (
          <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs px-1.5 py-0.5 rounded-full">
            {count}
          </span>
        )}
      </button>

      {showDropdown && (
        <div className="absolute right-0 mt-2 w-72 bg-white rounded-md shadow-lg z-50 border">
          <div className="p-2 border-b">
            <p className="font-medium">Notifications</p>
          </div>
          <div className="max-h-60 overflow-y-auto">
            {notifications.length > 0 ? (
              notifications.map((notification) => (
                <div
                  key={notification.id}
                  className={`p-3 border-b hover:bg-gray-50 cursor-pointer ${
                    !notification.isRead ? "bg-blue-50" : ""
                  }`}
                  onClick={() => {
                    if (!notification.isRead) {
                      markAsRead(notification.id);
                    }
                    // Handle notification click (e.g., navigate to appointment)
                  }}
                >
                  <p className="text-sm font-medium">{notification.title}</p>
                  <p className="text-xs text-gray-500">
                    {notification.message}
                  </p>
                  <p className="text-xs text-gray-400 mt-1">
                    {new Date(notification.createdAt).toLocaleString()}
                  </p>
                </div>
              ))
            ) : (
              <div className="p-4 text-center text-sm text-gray-500">
                No new notifications
              </div>
            )}
          </div>
          {notifications.length > 0 && (
            <div className="p-2 border-t text-center">
              <button
                onClick={markAllAsRead}
                className="text-xs text-blue-500 hover:underline"
              >
                Mark all as read
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default NotificationBadge;
