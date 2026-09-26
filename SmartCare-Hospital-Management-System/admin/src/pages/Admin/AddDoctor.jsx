import React from "react";
import { useAddDoctor } from "../../hooks/doctor/addDoctor/useAddDoctor";
import AddDoctorForm from "../../components/doctor/addDoctor/AddDoctorForm";

const AddDoctor = () => {
  const addDoctorProps = useAddDoctor();

  return <AddDoctorForm {...addDoctorProps} />;
};

export default AddDoctor;
