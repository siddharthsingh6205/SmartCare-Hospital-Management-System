import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AdminContext } from "../../context/AdminContext";
import { Card, Button, Input, Space, Spin } from "antd";
import { SearchOutlined, UserAddOutlined } from "@ant-design/icons";

const DoctorList = () => {
  const navigate = useNavigate();
  const { doctors, getAllDoctors, aToken } = useContext(AdminContext);
  const [searchText, setSearchText] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (aToken) {
      fetchDoctors();
    }
  }, [aToken]);

  const fetchDoctors = async () => {
    try {
      setLoading(true);
      await getAllDoctors();
    } catch (error) {
      console.error("Failed to fetch doctors:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredDoctors = doctors.filter(
    (doctor) =>
      doctor.user?.name?.toLowerCase().includes(searchText.toLowerCase()) ||
      doctor.specialization?.toLowerCase().includes(searchText.toLowerCase())
  );

  return (
    <div className="p-6">
      <div className="bg-white rounded-lg shadow-lg p-6">
        <Space direction="vertical" className="w-full">
          <Input
            placeholder="Search doctors by name or specialization"
            prefix={<SearchOutlined />}
            onChange={(e) => setSearchText(e.target.value)}
            allowClear
            className="w-full sm:w-96 mb-4"
          />

          <Spin spinning={loading}>
            <div className="grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
              {filteredDoctors.map((doctor) => (
                <div
                  key={doctor.id}
                  className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition duration-300 cursor-pointer"
                  onClick={() =>
                    navigate(`/doctor-list/${doctor.id}/availability`)
                  }
                >
                  <div className="h-48 w-full bg-gray-100 flex items-center justify-center overflow-hidden mb-4">
                    <img
                      alt={doctor.user.name}
                      src={`data:image/jpeg;base64,${doctor.user.image}`}
                      className="h-full w-full object-cover"
                    />
                  </div>
                  <h3 className="text-lg font-medium mb-2">
                    {doctor.user.name}
                  </h3>
                  <p className="text-gray-600 mb-1">{doctor.specialization}</p>
                  <p className="text-gray-500 mb-1">
                    Contact: {doctor.contactNumber}
                  </p>
                </div>
              ))}
            </div>
          </Spin>
        </Space>
      </div>
    </div>
  );
};

export default DoctorList;
