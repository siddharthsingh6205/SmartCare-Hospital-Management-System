import React, { useContext } from "react";

import RememberMe from "./RememberMe";
import { Oval } from "react-loader-spinner";
import { useLogin } from "../../hooks/login/useLogin";
import { AppContext } from "../../context/AppContext";

const LoginForm = () => {
  const { formData, remember, handleChange, setRemember, onSubmitHandler } =
    useLogin();
  const { loading } = useContext(AppContext);

  return (
    <form onSubmit={onSubmitHandler} className="w-full space-y-4">
      <div className="w-full">
        <label htmlFor="email" className="block text-gray-700">
          Email
        </label>
        <input
          id="email"
          onChange={handleChange}
          value={formData.email}
          className="border border-gray-300 rounded w-full p-2 mt-1 focus:outline-none focus:ring-2 focus:ring-blue-500"
          type="email"
          name="email"
          autoComplete="current-email"
          required
        />
      </div>

      <div className="w-full">
        <label htmlFor="password" className="block text-gray-700">
          Password
        </label>
        <input
          id="password"
          onChange={handleChange}
          value={formData.password}
          className="border border-gray-300 rounded w-full p-2 mt-1 focus:outline-none focus:ring-2 focus:ring-blue-500"
          type="password"
          name="password"
          required
          autoComplete="current-password"
        />
      </div>

      <div className="flex justify-between items-center w-full">
        <RememberMe remember={remember} setRemember={setRemember} />

        <a href="/forgotPassword" className="text-blue-600 hover:underline">
          Forgot password?
        </a>
      </div>

      <button
        type="submit"
        className="bg-blue-600 text-white w-full py-2 rounded-md text-base hover:bg-blue-700 transition-colors flex justify-center items-center"
        disabled={loading}
      >
        {loading ? (
          <Oval
            height={20}
            width={20}
            color="white"
            visible={true}
            ariaLabel="oval-loading"
          />
        ) : (
          "Sign In"
        )}
      </button>
    </form>
  );
};

export default LoginForm;
