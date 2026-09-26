import { Oval } from "react-loader-spinner";

const PasswordResetForm = ({
  newPassword,
  setNewPassword,
  confirmPassword,
  setConfirmPassword,
  loading,
  handleResetPassword,
}) => {
  return (
    <div className="p-8 bg-white rounded-lg shadow-lg w-full max-w-md">
      <h2 className="text-2xl font-bold mb-6 text-gray-800">Reset Password</h2>
      <input
        type="password"
        placeholder="New password"
        className="w-full p-3 border border-gray-300 rounded-md mb-4 focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800"
        value={newPassword}
        onChange={(e) => setNewPassword(e.target.value)}
      />
      <input
        type="password"
        placeholder="Confirm password"
        className="w-full p-3 border border-gray-300 rounded-md mb-6 focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-800"
        value={confirmPassword}
        onChange={(e) => setConfirmPassword(e.target.value)}
      />
      <button
        onClick={handleResetPassword}
        className={`w-full bg-blue-600 text-white py-3 rounded-md flex items-center justify-center ${
          loading ? "opacity-50 cursor-not-allowed" : "hover:bg-blue-700"
        }`}
        disabled={loading}
      >
        {loading ? (
          <Oval height={20} width={20} color="white" />
        ) : (
          "Reset Password"
        )}
      </button>
    </div>
  );
};

export default PasswordResetForm;
