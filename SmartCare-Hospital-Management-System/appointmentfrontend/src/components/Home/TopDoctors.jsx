import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AppContext } from "../../context/AppContext";
import { assets } from "../../assets/assets";

const TopDoctors = () => {
  const navigate = useNavigate();
  const { doctors, loading } = useContext(AppContext);

  if (loading) {
    return (
      <div className="flex flex-col items-center gap-4 my-16 text-gray-900 md:mx-10">
        <p>Loading doctors...</p>
      </div>
    );
  }

  if (!doctors || doctors.length === 0) {
    return (
      <div className="flex flex-col items-center gap-4 my-16 text-gray-900 md:mx-10">
        <h1 className="text-3xl font-medium">Our Doctors</h1>
        <p className="sm:w-1/3 text-center text-sm">
          Currently there are no doctors available. Please check back later.
        </p>
      </div>
    );
  }

  return (
    <section className="relative py-16 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
      <div className="absolute inset-0 -z-10 opacity-5">
        <img
          src={assets.hospital_bg}
          alt="Modern hospital"
          className="w-full h-full object-cover"
        />
      </div>

      <div className="relative text-center max-w-3xl mx-auto mb-12">
        <h2 className="text-3xl md:text-4xl font-bold text-gray-900">
          Our <span className="text-blue-600">Top Doctors</span>
        </h2>
        <p className="mt-4 text-gray-600">
          Highly rated and trusted by thousands of patients
        </p>
      </div>

      {!doctors || doctors.length === 0 ? (
        <div className="relative text-center py-12 bg-white bg-opacity-80 rounded-xl backdrop-blur-sm">
          <svg
            className="mx-auto h-12 w-12 text-gray-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          <h3 className="mt-2 text-lg font-medium text-gray-900">
            No doctors available
          </h3>
          <p className="mt-1 text-gray-500">
            Please check back later for available doctors.
          </p>
        </div>
      ) : (
        <>
          <div className="relative grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {doctors.slice(0, 8).map((doctor, index) => (
              <div
                key={index}
                onClick={() => {
                  navigate(`/appointment/${doctor.id}`);
                  window.scrollTo(0, 0);
                }}
                className="bg-white rounded-xl overflow-hidden shadow-sm hover:shadow-md border border-gray-100 cursor-pointer transition-all duration-300 transform hover:-translate-y-2"
              >
                <div className="relative h-48">
                  <img
                    src={`data:image/jpeg;base64,${doctor.user.image}`}
                    alt={doctor.user.name}
                    className="w-full h-full object-cover"
                  />
                  <div className="absolute top-4 right-4 bg-white p-2 rounded-full shadow">
                    <svg
                      className="w-5 h-5 text-yellow-400"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                    </svg>
                  </div>
                </div>
                <div className="p-6">
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="text-lg font-bold text-gray-900">
                        {doctor.user.name}
                      </h3>
                      <p className="text-[#5f6fff]">{doctor.specialization}</p>
                    </div>
                  </div>

                  <button className="mt-4 w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary hover:bg-primary focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    Book Now
                  </button>
                </div>
              </div>
            ))}
          </div>
          <div className="relative mt-12 text-center">
            <button
              onClick={() => {
                navigate("/doctors");
                scrollTo(0, 0);
              }}
              className="px-8 py-3 bg-white text-blue-600 border border-blue-600 rounded-xl font-medium hover:bg-blue-50 hover:shadow-md transition-all"
            >
              View All Doctors
            </button>
          </div>
        </>
      )}
    </section>
  );
};

export default TopDoctors;
