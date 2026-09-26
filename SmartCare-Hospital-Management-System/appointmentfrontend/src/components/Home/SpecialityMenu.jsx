import React from "react";
import { specializationData } from "../../assets/assets";
import { Link } from "react-router-dom";
import { assets } from "../../assets/assets";

const specializationMenu = () => {
  return (
  <section
        id="specialization"
        className="relative py-16 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto"
      >
        {/* Subtle pattern background */}
        <div className="absolute inset-0 -z-10 opacity-10">
          <img
            src={assets.pattern_bg}
            alt="Geometric pattern"
            className="w-full h-full object-cover"
          />
        </div>

        <div className="relative text-center max-w-3xl mx-auto">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900">
            Find by <span className="text-blue-600">Specialization</span>
          </h2>
          <p className="mt-4 text-gray-600">
            Browse our network of specialists across various medical fields to
            find the perfect care for your needs.
          </p>
        </div>

        <div className="relative mt-12 grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-6">
          {specializationData.map((item, index) => (
            <Link
              to={`/doctors/${item.specialization}`}
              key={index}
              className="group bg-white p-6 rounded-xl shadow-sm hover:shadow-md border border-gray-100 hover:border-blue-200 transition-all duration-300 transform hover:-translate-y-2"
            >
              <div className="w-16 h-16 mx-auto mb-4 bg-blue-50 rounded-xl flex items-center justify-center group-hover:bg-blue-100 transition-colors">
                <img
                  className="w-10 h-10 object-contain"
                  src={item.image}
                  alt={item.specialization}
                />
              </div>
              <p className="text-center font-medium text-gray-800 group-hover:text-blue-600 transition-colors">
                {item.specialization}
              </p>
            </Link>
          ))}
        </div>
      </section>
  );
};

export default specializationMenu;
