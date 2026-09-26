import React, { useContext, useEffect, useState } from "react";
import { DoctorContext } from "../../context/DoctorContext";
import { AppContext } from "../../context/AppContext";

const DoctorProfile = () => {
  const { dToken, profileData, setProfileData, getProfileData, updateProfile } =
    useContext(DoctorContext);
  const { currency } = useContext(AppContext);
  const [isEdit, setIsEdit] = useState(false);

  useEffect(() => {
    if (dToken) {
      getProfileData();
    }
  }, [dToken]);

  if (!profileData) return <div>Loading...</div>;

  return (
    <div className="flex flex-col gap-4 m-5">
      <div>
        <img
          className="bg-primary/80 w-full sm:max-w-64 rounded-lg"
          src={`data:image/jpeg;base64,${profileData.user?.image}`}
          alt="Doctor"
        />
      </div>

      <div className="flex-1 border border-stone-100 rounded-lg p-8 py-7 bg-white">
        {/* ----- Doc Info : name, degree, specialization ----- */}
        <p className="flex items-center gap-2 text-3xl font-medium text-gray-700">
          {profileData.user?.name}
        </p>
        <div className="flex items-center gap-2 mt-1 text-gray-600">
          <p>
            {profileData.degree} - {profileData.specialization}
          </p>
          <button className="py-0.5 px-2 border text-xs rounded-full">
            {profileData.experience}
          </button>
        </div>

        {/* ----- Doc About ----- */}
        <div>
          <p className="flex items-center gap-1 text-sm font-medium text-[#262626] mt-3">
            About :
          </p>
          {isEdit ? (
            <textarea
              onChange={(e) =>
                setProfileData((prev) => ({
                  ...prev,
                  aboutDoctor: e.target.value,
                }))
              }
              className="w-full outline-primary p-2 border rounded mt-1"
              rows={8}
              value={profileData.aboutDoctor || ""}
            />
          ) : (
            <p className="text-sm text-gray-600 max-w-[700px] mt-1">
              {profileData.aboutDoctor}
            </p>
          )}
        </div>

        {/* ----- Fees ----- */}
        <p className="text-gray-600 font-medium mt-4">
          Appointment fee:{" "}
          <span className="text-gray-800">
            {currency}{" "}
            {isEdit ? (
              <input
                type="number"
                onChange={(e) =>
                  setProfileData((prev) => ({
                    ...prev,
                    fees: e.target.value,
                  }))
                }
                value={profileData.fees}
                className="border rounded p-1 w-20"
              />
            ) : (
              profileData.fees
            )}
          </span>
        </p>

        {/* ----- Address ----- */}
        <div className="flex gap-2 py-2">
          <p>Address:</p>
          <div className="text-sm">
            {isEdit ? (
              <>
                <input
                  type="text"
                  onChange={(e) =>
                    setProfileData((prev) => ({
                      ...prev,
                      address: {
                        ...prev.address,
                        line1: e.target.value,
                      },
                    }))
                  }
                  value={profileData.address?.line1 || ""}
                  className="border rounded p-1 w-full mb-1"
                />
                <br />
                <input
                  type="text"
                  onChange={(e) =>
                    setProfileData((prev) => ({
                      ...prev,
                      address: {
                        ...prev.address,
                        line2: e.target.value,
                      },
                    }))
                  }
                  value={profileData.address?.line2 || ""}
                  className="border rounded p-1 w-full"
                />
              </>
            ) : (
              <>
                {profileData.address?.line1}
                <br />
                {profileData.address?.line2}
              </>
            )}
          </div>
        </div>



        {/* ----- Action Buttons ----- */}
        {isEdit ? (
          <div className="flex gap-2 mt-5">
            <button
              onClick={updateProfile}
              className="px-4 py-1 bg-primary text-white text-sm rounded-full hover:bg-primary-dark transition-all"
            >
              Save
            </button>
            <button
              onClick={() => setIsEdit(false)}
              className="px-4 py-1 border border-gray-300 text-sm rounded-full hover:bg-gray-50 transition-all"
            >
              Cancel
            </button>
          </div>
        ) : (
          <button
            onClick={() => setIsEdit(true)}
            className="px-4 py-1 border border-primary text-sm rounded-full mt-5 hover:bg-primary hover:text-white transition-all"
          >
            Edit Profile
          </button>
        )}
      </div>
    </div>
  );
};

export default DoctorProfile;
