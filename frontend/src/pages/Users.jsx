import { useState } from 'react';
import Layout from '../components/Layout';

const Users = () => {
  const [users, setUsers] = useState([
    { id: 1, username: 'admin', fullName: 'System Administrator', email: 'admin@example.com', role: 'ADMIN', isActive: true },
    { id: 2, username: 'user1', fullName: 'John Doe', email: 'john@example.com', role: 'USER', isActive: true },
    { id: 3, username: 'user2', fullName: 'Jane Smith', email: 'jane@example.com', role: 'USER', isActive: false },
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
      <div className="animate-fade-in">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-800">User Management</h1>
          <button className="px-6 py-2 bg-green-500 hover:bg-green-600 text-white rounded-lg font-medium transition">
            + Add New User
          </button>
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
              {filteredUsers.map(user => (
                <tr key={user.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 text-sm text-gray-900">{user.username}</td>
                  <td className="px-6 py-4 text-sm text-gray-900">{user.fullName}</td>
                  <td className="px-6 py-4 text-sm text-gray-600">{user.email}</td>
                  <td className="px-6 py-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      user.role === 'ADMIN' ? 'bg-orange-100 text-orange-800' : 'bg-blue-100 text-blue-800'
                    }`}>
                      {user.role}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      user.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    }`}>
                      {user.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex space-x-2">
                      <button className="text-xl hover:scale-110 transition" title="Edit">✏️</button>
                      <button
                        onClick={() => toggleUserStatus(user.id)}
                        className="text-xl hover:scale-110 transition"
                        title={user.isActive ? 'Deactivate' : 'Activate'}
                      >
                        {user.isActive ? '🔒' : '🔓'}
                      </button>
                      <button className="text-xl hover:scale-110 transition" title="Delete">🗑️</button>
                    </div>
                  </td>
                </tr>
              ))}
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
          <div><strong className="text-gray-800">Active:</strong> <span className="text-gray-600">{users.filter(u => u.isActive).length}</span></div>
          <div><strong className="text-gray-800">Inactive:</strong> <span className="text-gray-600">{users.filter(u => !u.isActive).length}</span></div>
        </div>
      </div>
    </Layout>
  );
};

export default Users;
