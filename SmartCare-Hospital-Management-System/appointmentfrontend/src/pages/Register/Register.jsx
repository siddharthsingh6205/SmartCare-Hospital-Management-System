import RegisterForm from "../../components/Registration/RegisterForm";


const Register = () => {
  return (
    <div className="w-full max-w-2xl mx-auto p-4">
      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-2xl font-semibold text-center mb-6">
          Patient Registration
        </h2>
        <RegisterForm />
   
      </div>
    </div>
  );
};

export default Register;
