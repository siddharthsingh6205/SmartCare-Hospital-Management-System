const RememberMe = ({ remember, setRemember }) => {
  return (
    <div className="flex items-center">
      <input
        type="checkbox"
        id="remember"
        className="mr-2"
        checked={remember}
        onChange={(e) => setRemember(e.target.checked)}
      />
      <label htmlFor="remember" className="text-gray-600">
        Remember me
      </label>
    </div>
  );
};

export default RememberMe;
