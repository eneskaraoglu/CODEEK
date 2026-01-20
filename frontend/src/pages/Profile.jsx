import { useState } from 'react';
import Layout from '../components/Layout';
import './Profile.css';

const Profile = () => {
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user') || '{}'));
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    fullName: user.fullName || '',
    email: user.email || '',
    phone: user.phone || ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Update user in localStorage (in real app, would call API)
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
      <div className="profile">
        <h1>My Profile</h1>

        <div className="profile-card">
          <div className="profile-header">
            <div className="profile-avatar">
              {(user.fullName || user.username || 'U').charAt(0).toUpperCase()}
            </div>
            <div className="profile-header-info">
              <h2>{user.fullName || user.username}</h2>
              <p className="profile-role">{user.role}</p>
            </div>
          </div>

          <div className="profile-body">
            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Username</label>
                  <input
                    type="text"
                    value={user.username}
                    disabled
                    className="disabled-input"
                  />
                </div>
                <div className="form-group">
                  <label>Role</label>
                  <input
                    type="text"
                    value={user.role}
                    disabled
                    className="disabled-input"
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Full Name</label>
                  <input
                    type="text"
                    name="fullName"
                    value={formData.fullName}
                    onChange={handleChange}
                    disabled={!isEditing}
                    className={!isEditing ? 'disabled-input' : ''}
                  />
                </div>
                <div className="form-group">
                  <label>Email</label>
                  <input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    disabled={!isEditing}
                    className={!isEditing ? 'disabled-input' : ''}
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Phone</label>
                  <input
                    type="tel"
                    name="phone"
                    value={formData.phone}
                    onChange={handleChange}
                    disabled={!isEditing}
                    className={!isEditing ? 'disabled-input' : ''}
                    placeholder="Not set"
                  />
                </div>
                <div className="form-group">
                  <label>Created At</label>
                  <input
                    type="text"
                    value={new Date(user.createdAt || Date.now()).toLocaleDateString()}
                    disabled
                    className="disabled-input"
                  />
                </div>
              </div>

              <div className="form-actions">
                {!isEditing ? (
                  <button
                    type="button"
                    onClick={() => setIsEditing(true)}
                    className="edit-btn"
                  >
                    Edit Profile
                  </button>
                ) : (
                  <>
                    <button type="submit" className="save-btn">
                      Save Changes
                    </button>
                    <button
                      type="button"
                      onClick={handleCancel}
                      className="cancel-btn"
                    >
                      Cancel
                    </button>
                  </>
                )}
              </div>
            </form>
          </div>
        </div>

        <div className="security-section">
          <h2>Security</h2>
          <button className="change-password-btn">Change Password</button>
        </div>
      </div>
    </Layout>
  );
};

export default Profile;
