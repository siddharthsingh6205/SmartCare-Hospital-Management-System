import { Oval } from "react-loader-spinner";

const ForgotPasswordForm = ({ email, setEmail, loading, handleSendOtp }) => {
  return (
    <div className="p-8 bg-white rounded-lg shadow-lg w-full max-w-md">
      <h2 className="text-4xl font-bold mb-8 text-gray-800">
        Forgot Password?
      </h2>
      <p className="mb-6 text-gray-600">Please enter your email below</p>
      <input
        type="email"
        placeholder="Enter your email"
        className="w-full p-3 border border-gray-300 rounded-md mb-6 focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <button
        onClick={handleSendOtp}
        className={`bg-blue-600 w-full text-white p-3 rounded-md flex items-center justify-center ${
          loading ? "opacity-50 cursor-not-allowed" : "hover:bg-blue-700"
        }`}
        disabled={loading}
      >
        {loading ? <Oval height={24} width={24} color="white" /> : "Send OTP"}
      </button>
    </div>
  );
};

export default ForgotPasswordForm;
