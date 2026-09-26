import React from "react";
import { Oval } from "react-loader-spinner";
import { useRegister } from "../../hooks/Registration/useRegister";
import AuthFooter from "../Login/AuthFooter";

const RegisterForm = () => {
  const {
    formData,
    handleChange,
    handleFileChange,
    onSubmitHandler,
    image,
    loading,
  } = useRegister();

  return (
    <form
      onSubmit={onSubmitHandler}
      className="bg-white p-8 rounded-lg shadow-md max-w-4xl mx-auto"
    >
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Left Column */}
        <div className="space-y-4">
          <div className="flex flex-col">
            <label className="text-gray-700 mb-1">Full Name</label>
            <input
              name="name"
              onChange={handleChange}
              value={formData.name}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="text"
              required
            />
          </div>

          <div className="flex flex-col">
            <label className="text-gray-700 mb-1">Email</label>
            <input
              name="email"
              onChange={handleChange}
              value={formData.email}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="email"
              required
            />
          </div>

          <div className="flex flex-col">
            <label className="text-gray-700 mb-1">Password</label>
            <input
              name="password"
              onChange={handleChange}
              value={formData.password}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="password"
              required
            />
          </div>

          <div className="flex flex-col">
            <label className="text-gray-700 mb-1">Age</label>
            <input
              name="age"
              onChange={handleChange}
              value={formData.age}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="number"
              min="1"
              required
            />
          </div>
        </div>

        {/* Right Column */}
        <div className="space-y-4">
          <div className="flex flex-col">
            <label className="text-gray-700 mb-1">Gender</label>
            <select
              name="gender"
              onChange={handleChange}
              value={formData.gender}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">Select Gender</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div className="flex flex-col">
            <label className="text-gray-700 mb-1">Contact Number</label>
            <input
              name="contactNumber"
              onChange={handleChange}
              value={formData.contactNumber}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="tel"
              required
            />
          </div>

          <div className="flex flex-col">
            <label className="text-gray-700 mb-1">Address</label>
            <input
              name="address"
              onChange={handleChange}
              value={formData.address}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="text"
              required
            />
          </div>

          <div className="flex flex-col">
            <label className="text-gray-700 mb-1">Date of Birth</label>
            <input
              name="dob"
              onChange={handleChange}
              value={formData.dob || ""}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              type="date"
              max={new Date().toISOString().split("T")[0]}
              required
            />
          </div>
        </div>
      </div>

      {/* Profile Image Upload */}
      <div className="mt-6">
        <label className="block text-gray-700 mb-2">Profile Image</label>
        <div className="flex items-center">
          <label className="cursor-pointer bg-gray-100 hover:bg-gray-200 px-4 py-2 rounded border border-gray-300 transition-colors">
            Choose File
            <input
              type="file"
              onChange={handleFileChange}
              className="hidden"
              accept="image/*"
            />
          </label>
          <span className="ml-3 text-gray-600">
            {image ? image.name : "No file chosen"}
          </span>
        </div>
      </div>

      {/* Register Button */}
      <div className="mt-8">
        <button
          type="submit"
          className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded transition-colors flex justify-center items-center"
          disabled={loading}
        >
          {loading ? (
            <Oval
              height={20}
              width={20}
              color="white"
              visible={true}
              ariaLabel="oval-loading"
            />
          ) : (
            "Register"
          )}
        </button>
      </div>

      <AuthFooter
        text="Do you have an account?"
        linkText="Login here"
        linkPath="/login"
      />
    </form>
  );
};

export default RegisterForm;
