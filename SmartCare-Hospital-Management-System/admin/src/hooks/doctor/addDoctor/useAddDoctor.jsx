import { useState } from "react";
import { AdminContext } from "../../../context/AdminContext";
import { useContext } from "react";

export const useAddDoctor = () => {
  const [docImg, setDocImg] = useState(null);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [experience, setExperience] = useState("1 Year");
  const [fees, setFees] = useState("");
  const [about, setAbout] = useState("");
  const [speciality, setSpeciality] = useState("General physician");
  const [degree, setDegree] = useState("");
  const [address1, setAddress1] = useState("");
  const [address2, setAddress2] = useState("");
  const [contactNumber, setContactNumber] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { registerDoctor } = useContext(AdminContext);

  const onSubmitHandler = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);

    const result = await registerDoctor(
      {
        name,
        email,
        password,
        experience,
        fees,
        about,
        speciality,
        degree,
        address1,
        address2,
        contactNumber,
      },
      docImg
    );

    if (result.success) {
      resetForm();
    }

    setIsSubmitting(false);
  };

  const resetForm = () => {
    setDocImg(null);
    setName("");
    setEmail("");
    setPassword("");
    setExperience("1 Year");
    setFees("");
    setAbout("");
    setSpeciality("General physician");
    setDegree("");
    setAddress1("");
    setAddress2("");
    setContactNumber("");
  };

  return {
    docImg,
    setDocImg,
    name,
    setName,
    email,
    setEmail,
    password,
    setPassword,
    experience,
    setExperience,
    fees,
    setFees,
    about,
    setAbout,
    speciality,
    setSpeciality,
    degree,
    setDegree,
    address1,
    setAddress1,
    address2,
    setAddress2,
    contactNumber,
    setContactNumber,
    isSubmitting,
    onSubmitHandler,
  };
};
