
import { assets } from "../../assets/assets";


const DoctorDetails = ({ docInfo, currencySymbol }) => {
  return (
    <div className="flex flex-col sm:flex-row gap-4">
      <div>
        <img
          className="bg-primary w-full sm:max-w-72 rounded-lg"
          src={`data:image/jpeg;base64,${docInfo.user.image}`}
          alt=""
        />
      </div>

      <div className="flex-1 border border-gray-400 rounded-lg p-8 py-7 bg-white mx-2 sm:mx-0 mt-[-80px] sm:mt-0">
        <p className="flex items-center gap-2 text-2xl font-medium text-gray-900">
          {docInfo.user.name}
          <img className="w-5" src={assets.verified_icon} alt="" />
        </p>
        <div className="flex items-center gap-2 text-sm mt-1 text-gray-600">
          <p>
            {docInfo.degree} - {docInfo.specialization}
          </p>
          <button className="py-0.5 px-2 border text-xs rounded-full">
            {docInfo.experience}
          </button>
        </div>

        <div>
          <p className="flex items-center gap-1 text-sm font-medium text-gray-900 mt-3">
            About <img className="w-3" src={assets.info_icon} alt="" />
          </p>
          <p className="text-sm text-gray-500 max-w-[700px] mt-1">
            {docInfo.aboutDoctor}
          </p>
        </div>

        <p className="text-gray-500 font-medium mt-4">
          Appointment fee:{" "}
          <span className="text-gray-600">
            {currencySymbol}
            {docInfo.fees}
          </span>
        </p>
      </div>
    </div>
  );
};

export default DoctorDetails;
