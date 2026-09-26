import { useContext, useEffect, useState } from "react";
import { AppContext } from "../../context/AppContext";

export const useLogin = () => {
  const { login } = useContext(AppContext);
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [remember, setRemember] = useState(false);

  useEffect(() => {
    const rememberedEmail = localStorage.getItem("rememberedEmail");
    const rememberedPassword = localStorage.getItem("rememberedPassword");

    if (rememberedEmail && rememberedPassword) {
      setFormData({
        email: rememberedEmail,
        password: rememberedPassword,
      });
      setRemember(true);
    }
  }, []);

  function handleChange(e) {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  }

  const onSubmitHandler = async (e) => {
    e.preventDefault();
    await login(formData, remember);
  };

  return {
    formData,
    handleChange,
    onSubmitHandler,
    setRemember,
    remember,
  };
};
