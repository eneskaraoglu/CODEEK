import { useState } from 'react';
import Layout from '../components/Layout';

const Profile = () => {
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user') || '{}'));
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    fullName: user.fullName || '',
    email: user.email || '',
    phone: user.phone || ''
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const updatedUser = { ...user, ...formData };
    setUser(updatedUser);
    localStorage.setItem('user', JSON.stringify(updatedUser));
    setIsEditing(false);
  };

  const handleCancel = () => {
    setFormData({
      fullName: user.fullName || '',
      email: user.email || '',
      phone: user.phone || ''
    });
    setIsEditing(false);
  };

  return (
    <Layout>
      <div className="animate-fade-in">
        <h1 className="text-3xl font-bold text-gray-800 mb-8">My Profile</h1>

        <div className="bg-white rounded-xl shadow-md overflow-hidden">
          <div className="bg-gradient-to-r from-purple-600 to-indigo-600 p-8 flex items-center space-x-6">
            <div className="w-20 h-20 bg-white rounded-full flex items-center justify-center text-purple-600 text-4xl font-bold">
              {(user.fullName || user.username || 'U').charAt(0).toUpperCase()}
            </div>
            <div className="text-white">
              <h2 className="text-2xl font-bold">{user.fullName || user.username}</h2>
              <p className="text-purple-100 mt-1">{user.role}</p>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="p-8">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Username</label>
                <input
                  type="text"
                  value={user.username}
                  disabled
                  className="w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-lg cursor-not-allowed"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Role</label>
                <input
                  type="text"
                  value={user.role}
                  disabled
                  className="w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-lg cursor-not-allowed"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Full Name</label>
                <input
                  type="text"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleChange}
                  disabled={!isEditing}
                  className={`w-full px-4 py-2 border border-gray-300 rounded-lg ${
                    !isEditing ? 'bg-gray-100 cursor-not-allowed' : 'focus:ring-2 focus:ring-primary-500'
                  }`}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Email</label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  disabled={!isEditing}
                  className={`w-full px-4 py-2 border border-gray-300 rounded-lg ${
                    !isEditing ? 'bg-gray-100 cursor-not-allowed' : 'focus:ring-2 focus:ring-primary-500'
                  }`}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Phone</label>
                <input
                  type="tel"
                  name="phone"
                  value={formData.phone}
                  onChange={handleChange}
                  disabled={!isEditing}
                  placeholder="Not set"
                  className={`w-full px-4 py-2 border border-gray-300 rounded-lg ${
                    !isEditing ? 'bg-gray-100 cursor-not-allowed' : 'focus:ring-2 focus:ring-primary-500'
                  }`}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Created At</label>
                <input
                  type="text"
                  value={new Date(user.createdAt || Date.now()).toLocaleDateString()}
                  disabled
                  className="w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-lg cursor-not-allowed"
                />
              </div>
            </div>

            <div className="flex space-x-4">
              {!isEditing ? (
                <button
                  type="button"
                  onClick={() => setIsEditing(true)}
                  className="px-6 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg font-medium transition"
                >
                  Edit Profile
                </button>
              ) : (
                <>
                  <button
                    type="submit"
                    className="px-6 py-2 bg-green-500 hover:bg-green-600 text-white rounded-lg font-medium transition"
                  >
                    Save Changes
                  </button>
                  <button
                    type="button"
                    onClick={handleCancel}
                    className="px-6 py-2 bg-gray-500 hover:bg-gray-600 text-white rounded-lg font-medium transition"
                  >
                    Cancel
                  </button>
                </>
              )}
            </div>
          </form>
        </div>

        <div className="bg-white rounded-xl shadow-md p-6 mt-6">
          <h2 className="text-xl font-bold text-gray-800 mb-4">Security</h2>
          <button className="px-6 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg font-medium transition">
            Change Password
          </button>
        </div>
      </div>
    </Layout>
  );
};

export default Profile;
