import React from "react";
import { assets } from "../../assets/assets";

const Header = () => {
  return (
    <section className="relative bg-primary text-white">
      <div className="absolute inset-0">
        <img
          src={assets.hero_bg}
          alt="Doctors discussing"
          className="w-full h-full object-cover opacity-40"
        />
        <div className="absolute inset-0 bg-gradient-to-r from-[#5f6fff] via-[#7f8fff] to-[#5f6fff] opacity-90 animate-gradient"></div>
      </div>

      <div className="relative z-10 py-24 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
          <div>
            <div className="flex items-center gap-2 mb-4">
              <img
                src={assets.logo}
                alt="Jenushan Care"
                className="h-20 bg-transparent"
              />
              <span className="text-xl font-bold">JenuCare</span>
            </div>
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold leading-tight">
              Personalized Healthcare{" "}
              <span className="text-blue-300">At Your Fingertips</span>
            </h1>
            <p className="mt-6 text-lg text-blue-100">
              Connect with board-certified doctors in minutes. Our platform
              makes it easy to find and book appointments with specialists who
              truly care.
            </p>
            <div className="mt-8 flex flex-col sm:flex-row gap-4">
              <button
                onClick={() =>
                  document
                    .getElementById("specialization")
                    .scrollIntoView({ behavior: "smooth" })
                }
                className="px-8 py-4 bg-white text-blue-800 rounded-xl font-medium hover:shadow-lg transition-all duration-300 hover:scale-105"
              >
                Find a Doctor
              </button>
            </div>
          </div>
          <div className="relative">
            <img
              src={assets.doctor_hero}
              alt="Doctor consultation"
              className="rounded-2xl shadow-2xl w-full max-w-lg mx-auto border-4 border-white"
            />
            <div className="absolute -bottom-6 -left-6 bg-white p-4 rounded-xl shadow-lg">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-blue-100 rounded-lg">
                  <svg
                    className="w-6 h-6 text-blue-600"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                    />
                  </svg>
                </div>
                <div>
                  <p className="text-xs text-gray-500">Average Wait Time</p>
                  <p className="font-bold text-gray-800">Under 15 min</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Header;
