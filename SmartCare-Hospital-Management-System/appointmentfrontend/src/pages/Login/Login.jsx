import React from "react";
import AuthFooter from "../../components/Login/AuthFooter";
import LoginForm from "../../components/Login/LoginForm";

const Login = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="flex flex-col gap-4 m-auto items-start p-8 w-full max-w-md border rounded-xl text-gray-600 text-sm shadow-lg bg-white">
        <h2 className="text-2xl font-semibold m-auto text-gray-800">
          Patient Login
        </h2>

        <LoginForm />

        <AuthFooter
          text="Don't have an account?"
          linkText="Register here"
          linkPath="/register"
        />
      </div>
    </div>
  );
};

export default Login;
