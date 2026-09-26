import { assets } from "../../../assets/assets";

const AddAdminForm = ({
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
}) => {
  return (
    <form onSubmit={onSubmitHandler} className="m-5 w-full">
      <p className="mb-3 text-lg font-medium">Add Admin</p>

      <div className="bg-white px-8 py-8 border rounded w-full max-w-4xl max-h-[80vh] overflow-y-scroll">
        <div className="flex items-center gap-4 mb-8 text-gray-500">
          <label htmlFor="admin-img">
            <img
              className="w-16 bg-gray-100 rounded-full cursor-pointer"
              src={
                adminImg ? URL.createObjectURL(adminImg) : assets.upload_area
              }
              alt="Admin preview"
            />
          </label>
          <input
            onChange={(e) => setAdminImg(e.target.files[0])}
            type="file"
            id="admin-img"
            hidden
            accept="image/*"
          />
          <p>
            Upload Admin <br /> picture
          </p>
        </div>

        <div className="flex flex-col lg:flex-row items-start gap-10 text-gray-600">
          <div className="w-full lg:flex-1 flex flex-col gap-4">
            <div className="flex-1 flex flex-col gap-1">
              <p>Admin name</p>
              <input
                onChange={(e) => setName(e.target.value)}
                value={name}
                className="border rounded px-3 py-2"
                type="text"
                placeholder="Name"
                required
              />
            </div>

            <div className="flex-1 flex flex-col gap-1">
              <p>Admin Email</p>
              <input
                onChange={(e) => setEmail(e.target.value)}
                value={email}
                className="border rounded px-3 py-2"
                type="email"
                placeholder="Email"
                required
              />
            </div>

            <div className="flex-1 flex flex-col gap-1">
              <p>Set Password</p>
              <input
                onChange={(e) => setPassword(e.target.value)}
                value={password}
                className="border rounded px-3 py-2"
                type="password"
                placeholder="Password"
                required
                minLength="6"
              />
            </div>
          </div>
        </div>

        <button
          type="submit"
          className="bg-primary px-10 py-3 mt-4 text-white rounded-full disabled:opacity-50"
          disabled={isSubmitting}
        >
          {isSubmitting ? "Adding..." : "Add Admin"}
        </button>
      </div>
    </form>
  );
};

export default AddAdminForm;
