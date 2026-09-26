import  { useContext, useEffect, useState } from "react";
import { AppContext } from "../../context/AppContext";

const MyProfile = () => {
  const {
    userData,
    setUserData,
    loading,
    token,
    loadUserProfileData,
    updateUserProfileData,
  } = useContext(AppContext);
  const [isEdit, setIsEdit] = useState(false);
  const [localLoading, setLocalLoading] = useState(true);

  useEffect(() => {
    const loadData = async () => {
      if (token) {
        try {
          setLocalLoading(true);
          await loadUserProfileData();
        } catch (error) {
          console.error("Failed to load profile:", error);
        } finally {
          setLocalLoading(false);
        }
      }
    };
    loadData();
  }, [token]);

  const handleSave = async () => {
    try {
      const updatedData = {
        name: userData.user.name,
        contactNumber: userData.contactNumber,
        address: userData.address,
        gender: userData.gender,
        dob: userData.dob,
      };

      await updateUserProfileData(updatedData);
      setIsEdit(false);
    } catch (error) {
      console.error("Failed to update profile:", error);
    
    }
  };

  // Show loading state
  if (localLoading || !userData) {
    return <div className="p-4">Loading profile data...</div>;
  }

  return (
    userData && (
      <div className="max-w-lg flex flex-col gap-2 text-sm">
        <img
          className="w-36 rounded"
          src={`data:image/jpeg;base64,${userData.user.image}`}
          alt=""
        />

        {isEdit ? (
          <input
            className="bg-gray-50 text-3xl font-medium max-w-60 mt-4"
            type="text"
            onChange={(e) =>
              setUserData((prev) => ({
                ...prev,
                user: {
                  ...prev.user,
                  name: e.target.value,
                },
              }))
            }
            value={userData.user.name}
          />
        ) : (
          <p className="font-medium text-3xl text-neutral-800 mt-4">
            {userData.user.name}
          </p>
        )}

        <hr className="bg-zinc-400 h-[1px] border-none" />
        <div>
          <p className="text-neutral-500 underline mt-3">CONTACT INFORMATION</p>
          <div className="grid grid-cols-[1fr_3fr] gap-y-2.5 mt-3 text-neutral-700">
            <p className="font-medium">Email id:</p>
            <p className="text-blue-500">{userData.user.email}</p>
            <p className="font-medium">Phone:</p>
            {isEdit ? (
              <input
                className="bg-gray-100 max-w-52"
                type="text"
                onChange={(e) =>
                  setUserData((prev) => ({
                    ...prev,
                    contactNumber: e.target.value,
                  }))
                }
                value={userData.contactNumber}
              />
            ) : (
              <p className="text-blue-400">{userData.contactNumber}</p>
            )}
            <p className="font-medium">Address:</p>
            {isEdit ? (
              <p>
                <input
                  className="bg-gray-50"
                  type="text"
                  onChange={(e) =>
                    setUserData((prev) => ({
                      ...prev,
                      address: e.target.value,
                    }))
                  }
                  value={userData.address}
                />
              </p>
            ) : (
              <p className="text-gray-500">{userData.address}</p>
            )}
          </div>
        </div>
        <div>
          <p className="text-neutral-500 underline mt-3">BASIC INFORMATION</p>
          <div className="grid grid-cols-[1fr_3fr] gap-y-2.5 mt-3 text-neutral-700">
            <p className="font-medium">Gender:</p>
            {isEdit ? (
              <select
                className="max-w-20 bg-gray-100"
                onChange={(e) =>
                  setUserData((prev) => ({ ...prev, gender: e.target.value }))
                }
                value={userData.gender}
              >
                <option value="Male">Male</option>
                <option value="Female">Female</option>
              </select>
            ) : (
              <p className="text-gray-400">{userData.gender}</p>
            )}
            <p className="font-medium">Birthday:</p>
            {isEdit ? (
              <input
                className="max-w-28 bg-gray-100"
                type="date"
                onChange={(e) =>
                  setUserData((prev) => ({ ...prev, dob: e.target.value }))
                }
                value={userData.dob}
              />
            ) : (
              <p className="text-gray-400">{userData.dob}</p>
            )}
          </div>
        </div>
        <div className="mt-10">
          {isEdit ? (
            <button
              onClick={handleSave}
              disabled={loading}
              className={`border border-primary px-8 py-2 rounded-full hover:bg-[#5f6fff] hover:text-white transition-all ${
                loading ? "opacity-50 cursor-not-allowed" : ""
              }`}
            >
              {loading ? "Saving..." : "Save information"}
            </button>
          ) : (
            <button
              onClick={() => setIsEdit(true)}
              className="border border-primary px-8 py-2 rounded-full hover:bg-[#5f6fff] hover:text-white transition-all"
            >
              Edit
            </button>
          )}
        </div>
      </div>
    )
  );
};

export default MyProfile;
