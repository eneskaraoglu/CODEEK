import { useEffect, useState } from 'react';
import Layout from '../components/Layout';

const Dashboard = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const [stats] = useState({
    totalUsers: 150,
    activeProjects: 12,
    completedTasks: 45,
    pendingTasks: 18
  });

  const activities = [
    { icon: '🔔', title: 'New user registered', time: '2 hours ago' },
    { icon: '✏️', title: 'Project updated', time: '5 hours ago' },
    { icon: '🎉', title: 'Task completed', time: '1 day ago' },
  ];

  return (
    <Layout>
      <div className="animate-fade-in">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          Welcome back, {user.fullName || user.username}!
        </h1>
        <p className="text-gray-600 mb-8">
          Here's what's happening with your account today.
        </p>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition flex items-center space-x-4">
            <div className="text-5xl bg-blue-100 p-4 rounded-xl">👥</div>
            <div>
              <h3 className="text-3xl font-bold text-gray-800">{stats.totalUsers}</h3>
              <p className="text-gray-600">Total Users</p>
            </div>
          </div>

          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition flex items-center space-x-4">
            <div className="text-5xl bg-purple-100 p-4 rounded-xl">📊</div>
            <div>
              <h3 className="text-3xl font-bold text-gray-800">{stats.activeProjects}</h3>
              <p className="text-gray-600">Active Projects</p>
            </div>
          </div>

          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition flex items-center space-x-4">
            <div className="text-5xl bg-green-100 p-4 rounded-xl">✅</div>
            <div>
              <h3 className="text-3xl font-bold text-gray-800">{stats.completedTasks}</h3>
              <p className="text-gray-600">Completed Tasks</p>
            </div>
          </div>

          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition flex items-center space-x-4">
            <div className="text-5xl bg-orange-100 p-4 rounded-xl">⏳</div>
            <div>
              <h3 className="text-3xl font-bold text-gray-800">{stats.pendingTasks}</h3>
              <p className="text-gray-600">Pending Tasks</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-md p-6">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Recent Activity</h2>
          <div className="space-y-3">
            {activities.map((activity, index) => (
              <div
                key={index}
                className="flex items-center space-x-4 p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition"
              >
                <span className="text-2xl">{activity.icon}</span>
                <div className="flex-1">
                  <p className="font-medium text-gray-800">{activity.title}</p>
                  <p className="text-sm text-gray-500">{activity.time}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Dashboard;
