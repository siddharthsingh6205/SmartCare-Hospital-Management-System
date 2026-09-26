import React from "react";
import { assets } from "../../assets/assets";
import { useNavigate } from "react-router-dom";

const Banner = () => {
  const navigate = useNavigate();

  return (
    <section className="relative py-16 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto my-12">
      <div className="absolute inset-0 overflow-hidden rounded-3xl">
        <img
          src={assets.stethoscope_bg}
          alt="Stethoscope on blue background"
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-primary opacity-70"></div>
      </div>

      <div className="relative z-10">
        <div className="grid grid-cols-1 lg:grid-cols-2">
          <div className="p-12 text-white">
            <h2 className="text-3xl font-bold">Your Health Can't Wait</h2>
            <p className="mt-4 text-blue-100">
              Join thousands of patients who trust Jenushan Care for their
              medical needs. Schedule your appointment today and experience
              healthcare reimagined.
            </p>
            <div className="mt-8 flex flex-col sm:flex-row gap-4">
              <button
                onClick={() => {
                  navigate("/register");
                  scrollTo(0, 0);
                }}
                className="px-8 py-4 bg-white text-[#5f6fff]  rounded-xl font-medium hover:shadow-lg transition-all duration-300 hover:scale-105"
              >
                Create Account
              </button>
              <button
                onClick={() => {
                  navigate("/contact");
                  scrollTo(0, 0);
                }}
                className="px-8 py-4 border-2 border-white text-white rounded-xl font-medium hover:bg-white hover:bg-opacity-5 hover:text-[#5f6fff] transition-all"
              >
                Contact Us
              </button>
            </div>
          </div>
          <div className="hidden lg:block relative">
            <img
              src={assets.doctor_cta}
              alt="Doctor with patient"
              className="absolute bottom-0 right-0 h-full w-auto object-contain"
            />
          </div>
        </div>
      </div>
    </section>
  );
};

export default Banner;
