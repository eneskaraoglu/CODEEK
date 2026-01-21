import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { userAPI } from '../services/api';

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterRole, setFilterRole] = useState('ALL');

  // Fetch users on component mount
  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const response = await userAPI.getAllUsers();
      if (response.data.success) {
        setUsers(response.data.data);
      } else {
        setError(response.data.message || 'Failed to fetch users');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch users');
      console.error('Error fetching users:', err);
    } finally {
      setLoading(false);
    }
  };

  const filteredUsers = users.filter(user => {
    const matchesSearch = user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         user.email.toLowerCase().includes(searchTerm.toLowerCase());

    // Extract primary role from roles array
    const primaryRole = user.roles && user.roles.length > 0
      ? user.roles[0].replace('ROLE_', '')
      : 'USER';

    const matchesRole = filterRole === 'ALL' || primaryRole === filterRole;
    return matchesSearch && matchesRole;
  });

  const toggleUserStatus = async (userId) => {
    try {
      const response = await userAPI.toggleUserStatus(userId);
      if (response.data.success) {
        // Update local state
        setUsers(users.map(user =>
          user.userId === userId ? response.data.data : user
        ));
      } else {
        alert(response.data.message || 'Failed to toggle user status');
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to toggle user status');
      console.error('Error toggling user status:', err);
    }
  };

  const handleDeleteUser = async (userId) => {
    if (!confirm('Are you sure you want to delete this user?')) return;

    try {
      const response = await userAPI.deleteUser(userId);
      if (response.data.success) {
        // Refresh user list
        fetchUsers();
      } else {
        alert(response.data.message || 'Failed to delete user');
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to delete user');
      console.error('Error deleting user:', err);
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="flex justify-center items-center h-64">
          <div className="text-xl text-gray-600">Loading users...</div>
        </div>
      </Layout>
    );
  }

  if (error) {
    return (
      <Layout>
        <div className="bg-red-50 border border-red-200 rounded-lg p-4 text-red-700">
          Error: {error}
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="animate-fade-in">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-800">User Management</h1>
        </div>

        <div className="flex gap-4 mb-6">
          <input
            type="text"
            placeholder="Search users..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none"
          />
          <select
            value={filterRole}
            onChange={(e) => setFilterRole(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none"
          >
            <option value="ALL">All Roles</option>
            <option value="ADMIN">Admin</option>
            <option value="USER">User</option>
          </select>
        </div>

        <div className="bg-white rounded-xl shadow-md overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Username</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Full Name</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Email</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Role</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Status</th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {filteredUsers.map(user => {
                const primaryRole = user.roles && user.roles.length > 0
                  ? user.roles[0].replace('ROLE_', '')
                  : 'USER';
                const isActive = user.status === 'ACTIVE';

                return (
                  <tr key={user.userId} className="hover:bg-gray-50">
                    <td className="px-6 py-4 text-sm text-gray-900">{user.username}</td>
                    <td className="px-6 py-4 text-sm text-gray-900">{user.fullName}</td>
                    <td className="px-6 py-4 text-sm text-gray-600">{user.email}</td>
                    <td className="px-6 py-4">
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                        primaryRole === 'ADMIN' ? 'bg-orange-100 text-orange-800' : 'bg-blue-100 text-blue-800'
                      }`}>
                        {primaryRole}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                        isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                      }`}>
                        {isActive ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex space-x-2">
                        <button
                          onClick={() => toggleUserStatus(user.userId)}
                          className="text-xl hover:scale-110 transition"
                          title={isActive ? 'Deactivate' : 'Activate'}
                        >
                          {isActive ? '🔒' : '🔓'}
                        </button>
                        <button
                          onClick={() => handleDeleteUser(user.userId)}
                          className="text-xl hover:scale-110 transition"
                          title="Delete"
                        >
                          🗑️
                        </button>
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          {filteredUsers.length === 0 && (
            <div className="py-12 text-center text-gray-500">
              No users found matching your criteria
            </div>
          )}
        </div>

        <div className="mt-6 bg-white rounded-xl shadow-md p-6 flex gap-8">
          <div><strong className="text-gray-800">Total Users:</strong> <span className="text-gray-600">{users.length}</span></div>
          <div><strong className="text-gray-800">Active:</strong> <span className="text-gray-600">{users.filter(u => u.status === 'ACTIVE').length}</span></div>
          <div><strong className="text-gray-800">Inactive:</strong> <span className="text-gray-600">{users.filter(u => u.status === 'INACTIVE').length}</span></div>
        </div>
      </div>
    </Layout>
  );
};

export default Users;
