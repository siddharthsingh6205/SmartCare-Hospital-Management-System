import { useContext, useState } from "react";
import { AppContext } from "../../context/AppContext";

export const useRegister = () => {
  const { loading, registerPatient } = useContext(AppContext);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    age: "",
    gender: "",
    contactNumber: "",
    address: "",
    medicalHistory: "",
    dob: "",
  });
  const [image, setImage] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setImage(e.target.files[0]);
  };

  const onSubmitHandler = async (e) => {
    e.preventDefault();

    const patientObj = {
      age: formData.age,
      gender: formData.gender,
      contactNumber: formData.contactNumber,
      address: formData.address,
      medicalHistory: formData.medicalHistory,
      dob: String(formData.dob),
      user: {
        name: formData.name,
        email: formData.email,
        password: formData.password,
      },
    };

    console.log(patientObj);

    const formDataToSend = new FormData();
    formDataToSend.append("patient", JSON.stringify(patientObj));
    if (image) {
      formDataToSend.append("image", image);
    }

    await registerPatient(formDataToSend, image);
  };

  return {
    formData,
    handleChange,
    handleFileChange,
    onSubmitHandler,
    image,
    loading,
  };
};
