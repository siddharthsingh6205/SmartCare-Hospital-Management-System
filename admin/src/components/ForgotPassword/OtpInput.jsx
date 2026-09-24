import { Oval } from "react-loader-spinner";

const OtpInput = ({
  otp,
  handleChangeOtp,
  handleKeyDown,
  loading,
  handleVerifyOtp,
}) => {
  return (
    <div className="p-8 bg-white rounded-lg shadow-lg w-full max-w-md">
      <h2 className="text-3xl font-bold mb-6 text-gray-800">Enter OTP</h2>
      <p className="mb-8 text-gray-600">
        Enter the 6-digit verification code sent to your email
      </p>
      <div className="flex justify-center gap-3 mb-8">
        {otp.map((value, index) => (
          <input
            key={index}
            id={`otp-input-${index}`}
            type="text"
            maxLength="1"
            value={value}
            onChange={(e) => handleChangeOtp(e.target.value, index)}
            onKeyDown={(e) => handleKeyDown(e, index)}
            className="w-14 h-14 text-center text-2xl border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800"
          />
        ))}
      </div>
      <button
        onClick={handleVerifyOtp}
        className={`w-full bg-blue-600 text-white py-3 rounded-md flex items-center justify-center ${
          loading ? "opacity-50 cursor-not-allowed" : "hover:bg-blue-700"
        }`}
        disabled={loading}
      >
        {loading ? <Oval height={20} width={20} color="white" /> : "Verify OTP"}
      </button>
    </div>
  );
};

export default OtpInput;
