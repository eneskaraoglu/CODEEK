import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import './Users.css';

const Users = () => {
  const [users, setUsers] = useState([
    {
      id: 1,
      username: 'admin',
      fullName: 'System Administrator',
      email: 'admin@example.com',
      role: 'ADMIN',
      createdAt: '2024-01-15',
      isActive: true
    },
    {
      id: 2,
      username: 'user1',
      fullName: 'John Doe',
      email: 'john@example.com',
      role: 'USER',
      createdAt: '2024-02-01',
      isActive: true
    },
    {
      id: 3,
      username: 'user2',
      fullName: 'Jane Smith',
      email: 'jane@example.com',
      role: 'USER',
      createdAt: '2024-02-10',
      isActive: false
    }
  ]);

  const [searchTerm, setSearchTerm] = useState('');
  const [filterRole, setFilterRole] = useState('ALL');

  const filteredUsers = users.filter(user => {
    const matchesSearch = user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         user.email.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesRole = filterRole === 'ALL' || user.role === filterRole;
    return matchesSearch && matchesRole;
  });

  const toggleUserStatus = (userId) => {
    setUsers(users.map(user =>
      user.id === userId ? { ...user, isActive: !user.isActive } : user
    ));
  };

  return (
    <Layout>
      <div className="users-page">
        <div className="users-header">
          <h1>User Management</h1>
          <button className="add-user-btn">+ Add New User</button>
        </div>

        <div className="users-filters">
          <input
            type="text"
            placeholder="Search users..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
          <select
            value={filterRole}
            onChange={(e) => setFilterRole(e.target.value)}
            className="filter-select"
          >
            <option value="ALL">All Roles</option>
            <option value="ADMIN">Admin</option>
            <option value="USER">User</option>
          </select>
        </div>

        <div className="users-table-container">
          <table className="users-table">
            <thead>
              <tr>
                <th>Username</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Created</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.map(user => (
                <tr key={user.id}>
                  <td>{user.username}</td>
                  <td>{user.fullName}</td>
                  <td>{user.email}</td>
                  <td>
                    <span className={`role-badge ${user.role.toLowerCase()}`}>
                      {user.role}
                    </span>
                  </td>
                  <td>{new Date(user.createdAt).toLocaleDateString()}</td>
                  <td>
                    <span className={`status-badge ${user.isActive ? 'active' : 'inactive'}`}>
                      {user.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button className="btn-edit" title="Edit">✏️</button>
                      <button
                        className={`btn-toggle ${user.isActive ? 'deactivate' : 'activate'}`}
                        onClick={() => toggleUserStatus(user.id)}
                        title={user.isActive ? 'Deactivate' : 'Activate'}
                      >
                        {user.isActive ? '🔒' : '🔓'}
                      </button>
                      <button className="btn-delete" title="Delete">🗑️</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {filteredUsers.length === 0 && (
            <div className="no-results">
              <p>No users found matching your criteria</p>
            </div>
          )}
        </div>

        <div className="users-stats">
          <div className="stat-item">
            <strong>Total Users:</strong> {users.length}
          </div>
          <div className="stat-item">
            <strong>Active:</strong> {users.filter(u => u.isActive).length}
          </div>
          <div className="stat-item">
            <strong>Inactive:</strong> {users.filter(u => !u.isActive).length}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Users;
