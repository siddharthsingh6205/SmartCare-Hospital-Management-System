const SuccessMessage = ({ navigate }) => {
  return (
    <div className="p-8 bg-white rounded-lg shadow-lg w-full max-w-md text-center">
      <h2 className="text-2xl font-semibold mb-6 text-gray-800">
        Password reset successfully!
      </h2>
      <button
        onClick={() => navigate("/login")}
        className="w-full p-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
      >
        Go to Login
      </button>
    </div>
  );
};

export default SuccessMessage;
