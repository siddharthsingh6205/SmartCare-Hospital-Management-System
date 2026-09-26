const AuthFooter = ({ text, linkText, linkPath }) => {
  return (
    <p className="m-auto text-gray-600">
      {text}{" "}
      <a href={linkPath} className="text-blue-600 hover:underline">
        {linkText}
      </a>
    </p>
  );
};

export default AuthFooter;
