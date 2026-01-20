import { useEffect, useState } from 'react';
import Layout from '../components/Layout';
import './Dashboard.css';

const Dashboard = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const [stats, setStats] = useState({
    totalUsers: 0,
    activeProjects: 0,
    completedTasks: 0,
    pendingTasks: 0
  });

  useEffect(() => {
    // Simulate loading stats
    setStats({
      totalUsers: 150,
      activeProjects: 12,
      completedTasks: 45,
      pendingTasks: 18
    });
  }, []);

  return (
    <Layout>
      <div className="dashboard">
        <h1>Welcome back, {user.fullName || user.username}!</h1>
        <p className="subtitle">Here's what's happening with your account today.</p>

        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon users">👥</div>
            <div className="stat-info">
              <h3>{stats.totalUsers}</h3>
              <p>Total Users</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon projects">📊</div>
            <div className="stat-info">
              <h3>{stats.activeProjects}</h3>
              <p>Active Projects</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon completed">✅</div>
            <div className="stat-info">
              <h3>{stats.completedTasks}</h3>
              <p>Completed Tasks</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon pending">⏳</div>
            <div className="stat-info">
              <h3>{stats.pendingTasks}</h3>
              <p>Pending Tasks</p>
            </div>
          </div>
        </div>

        <div className="recent-activity">
          <h2>Recent Activity</h2>
          <div className="activity-list">
            <div className="activity-item">
              <span className="activity-icon">🔔</span>
              <div className="activity-details">
                <p className="activity-title">New user registered</p>
                <p className="activity-time">2 hours ago</p>
              </div>
            </div>
            <div className="activity-item">
              <span className="activity-icon">✏️</span>
              <div className="activity-details">
                <p className="activity-title">Project updated</p>
                <p className="activity-time">5 hours ago</p>
              </div>
            </div>
            <div className="activity-item">
              <span className="activity-icon">🎉</span>
              <div className="activity-details">
                <p className="activity-title">Task completed</p>
                <p className="activity-time">1 day ago</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Dashboard;
