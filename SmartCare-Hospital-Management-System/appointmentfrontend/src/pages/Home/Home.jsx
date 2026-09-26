import React, { useContext } from "react";
import Header from "../../components/Home/Header";
import SpecialityMenu from "../../components/Home/SpecialityMenu";
import TopDoctors from "../../components/Home/TopDoctors";
import Banner from "../../components/Home/Banner";
import { useNavigate, Link } from "react-router-dom";
import { AppContext } from "../../context/AppContext";
import { assets, specializationData } from "../../assets/assets";

const Home = () => {
  return (
    <div className="relative overflow-hidden">
      <Header />
      <SpecialityMenu />
      <TopDoctors />
      <Banner />
    </div>
  );
};

export default Home;
