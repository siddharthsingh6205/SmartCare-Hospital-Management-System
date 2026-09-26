import { useState } from "react";
import { AdminContext } from "../../../context/AdminContext";
import { useContext } from "react";

export const useAddAdmin = () => {
  const [adminImg, setAdminImg] = useState(null);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { registerAdmin } = useContext(AdminContext);

  const onSubmitHandler = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);

    const result = await registerAdmin({ name, email, password }, adminImg);

    if (result.success) {
      resetForm();
    }

    setIsSubmitting(false);
  };

  const resetForm = () => {
    setAdminImg(null);
    setName("");
    setEmail("");
    setPassword("");
  };

  return {
    adminImg,
    setAdminImg,
    name,
    setName,
    email,
    setEmail,
    password,
    setPassword,
    isSubmitting,
    onSubmitHandler,
  };
};
