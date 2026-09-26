import {  useState } from "react";
import { assets } from "../../assets/assets";

const About = () => {
  const [activeCard, setActiveCard] = useState(0);

  const features = [
    {
      title: "EFFICIENCY",
      description:
        "Streamlined appointment scheduling that fits into your busy lifestyle.",
      icon: "⏱️",
    },
    {
      title: "CONVENIENCE",
      description:
        "Access to a network of trusted healthcare professionals in your area.",
      icon: "🏥",
    },
    {
      title: "PERSONALIZATION",
      description:
        "Tailored recommendations and reminders to help you stay on top of your health.",
      icon: "🎯",
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-b from-white to-[#5f6fff]/10">
      <div className="container mx-auto px-4 py-16">
        {/* Animated Header */}
        <div className="text-center mb-16">
          <h1 className="text-4xl md:text-5xl font-bold mb-4">
            <span className="bg-clip-text text-transparent bg-gradient-to-r from-[#5f6fff] to-[#a855f7]">
              ABOUT US
            </span>
          </h1>
          <p className="text-gray-600 max-w-2xl mx-auto">
            Your trusted partner in healthcare management
          </p>
        </div>

        {/* Main Content */}
        <div className="flex flex-col lg:flex-row gap-12 items-center mb-20">
          <div className="w-full lg:w-1/2">
            <div className="rounded-2xl overflow-hidden shadow-xl hover:shadow-2xl transition-shadow duration-300">
              <img
                className="w-full h-auto object-cover"
                src={assets.about_image}
                alt="About JenuCart"
              />
            </div>
          </div>

          <div className="w-full lg:w-1/2 space-y-6">
            <div className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow duration-300">
              <p className="text-gray-600 mb-4">
                Welcome to{" "}
                <span className="text-[#5f6fff] font-semibold">JenuCare</span>,
                your trusted partner in managing your healthcare needs
                conveniently and efficiently. At JenuCart, we understand the
                challenges individuals face when it comes to scheduling doctor
                appointments and managing their health records.
              </p>

              <p className="text-gray-600 mb-4">
                JenuCart is committed to excellence in healthcare technology.
                We continuously strive to enhance our platform, integrating the
                latest advancements to improve user experience and deliver
                superior service. Whether you're booking your first appointment
                or managing ongoing care, JenuCart is here to support you
                every step of the way.
              </p>
            </div>

            <div className="bg-gradient-to-r from-[#5f6fff] to-[#a855f7] p-8 rounded-2xl text-white shadow-lg hover:shadow-xl transition-shadow duration-300">
              <h3 className="text-xl font-bold mb-3">Our Vision</h3>
              <p className="opacity-90">
                Our vision at JenuCart is to create a seamless healthcare
                experience for every user. We aim to bridge the gap between
                patients and healthcare providers, making it easier for you to
                access the care you need, when you need it.
              </p>
            </div>
          </div>
        </div>

        {/* Features Section */}
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold mb-4">
            <span className="bg-clip-text text-transparent bg-gradient-to-r from-[#5f6fff] to-[#a855f7]">
              WHY CHOOSE US
            </span>
          </h2>
          <p className="text-gray-600 max-w-2xl mx-auto">
            Discover what makes JenuCart the right choice for your healthcare
            needs
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-20">
          {features.map((feature, index) => (
            <div
              key={index}
              className={`p-8 rounded-2xl cursor-pointer transition-all duration-300 ${
                activeCard === index
                  ? "bg-[#5f6fff] text-white shadow-xl transform -translate-y-2"
                  : "bg-white text-gray-600 shadow-lg hover:shadow-md"
              }`}
              onClick={() => setActiveCard(index)}
              onMouseEnter={() => setActiveCard(index)}
            >
              <div className="text-4xl mb-4">{feature.icon}</div>
              <h3 className="text-xl font-bold mb-3">{feature.title}</h3>
              <p>{feature.description}</p>
            </div>
          ))}
        </div>

        {/* Stats Section */}
        <div className="bg-gradient-to-r from-[#5f6fff] to-[#a855f7] rounded-2xl p-8 text-white shadow-xl">
          <div className="text-center">
            <div className="text-4xl font-bold mb-2">24/7</div>
            <p className="opacity-90">Support Available</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default About;
