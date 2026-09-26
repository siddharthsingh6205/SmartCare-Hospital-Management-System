import  { useContext, useEffect, useState } from "react";

import { useNavigate, useParams } from "react-router-dom";
import { AppContext } from "../../context/AppContext";

const Doctors = () => {
  const { specialization } = useParams();
  const [filterDoc, setFilterDoc] = useState([]);
  const [showFilter, setShowFilter] = useState(false);
  const navigate = useNavigate();
  const { doctors } = useContext(AppContext);

  // Filter function to apply specialization
  const applyFilter = () => {
    if (specialization) {
      return setFilterDoc(
        doctors.filter(
          (doc) =>
            doc.specialization.toLowerCase() == specialization.toLowerCase()
        ) 
      );
    } else {
      setFilterDoc(doctors);
    }
  };

  // Fetch doctors data when component loads
  useEffect(() => {
    if (doctors && doctors.length > 0) {
      applyFilter();
    }
  }, [doctors, specialization]);

  return (
    <div>
      <p className="text-gray-600">Browse through the doctors specialist.</p>
      <div className="flex flex-col sm:flex-row items-start gap-5 mt-5">
        <button
          onClick={() => setShowFilter(!showFilter)}
          className={`py-1 px-3 border rounded text-sm transition-all sm:hidden ${
            showFilter ? "bg-[#5f6fff] text-white" : ""
          }`}
        >
          Filters
        </button>
        <div
          className={`flex-col gap-4 text-sm text-gray-600 ${
            showFilter ? "flex" : "hidden sm:flex"
          }`}
        >
          {[
            "General physician",
            "Gynecologist",
            "Dermatologist",
            "Pediatricians",
            "Neurologist",
            "Gastroenterologist",
          ].map((specializationOption) => (
            <p
              key={specializationOption}
              onClick={() =>
                specialization === specializationOption
                  ? navigate("/doctors")
                  : navigate(`/doctors/${specializationOption}`)
              }
              className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${
                specialization === specializationOption
                  ? "bg-[#E2E5FF] text-black "
                  : ""
              }`}
            >
              {specializationOption}
            </p>
          ))}
        </div>
        <div className="w-full grid grid-cols-[repeat(auto-fill,minmax(200px,1fr))] gap-4 gap-y-6">
          {filterDoc.map((item, index) => (
            <div
              onClick={() => {
                navigate(`/appointment/${item.id}`);
                window.scrollTo(0, 0);
              }}
              className="border border-indigo-200 rounded-xl overflow-hidden cursor-pointer hover:translate-y-[-10px] transition-all duration-500"
              key={index}
            >
              <img
                className="bg-indigo-50"
                src={`data:image/jpeg;base64,${item.user.image}`}
                alt=""
              />
              <div className="p-4">
                <p className="text-neutral-800 text-lg font-medium">
                  {item.user.name}
                </p>
                <p className="text-zinc-600 text-sm">{item.specialization}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Doctors;
