import { useNavigate, useLocation } from 'react-router-dom';
import { authAPI } from '../services/api';
import './Layout.css';

const Layout = ({ children }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  const handleLogout = () => {
    authAPI.logout();
    navigate('/login');
  };

  const isActive = (path) => location.pathname === path;

  return (
    <div className="layout">
      <nav className="navbar">
        <div className="navbar-brand">
          <h2>CODEEK App</h2>
        </div>
        <div className="navbar-menu">
          <button
            className={`nav-link ${isActive('/dashboard') ? 'active' : ''}`}
            onClick={() => navigate('/dashboard')}
          >
            Dashboard
          </button>
          <button
            className={`nav-link ${isActive('/profile') ? 'active' : ''}`}
            onClick={() => navigate('/profile')}
          >
            Profile
          </button>
          {user.role === 'ADMIN' && (
            <button
              className={`nav-link ${isActive('/users') ? 'active' : ''}`}
              onClick={() => navigate('/users')}
            >
              Users
            </button>
          )}
        </div>
        <div className="navbar-user">
          <span className="user-name">{user.fullName || user.username}</span>
          <span className="user-role">{user.role}</span>
          <button onClick={handleLogout} className="logout-button">
            Logout
          </button>
        </div>
      </nav>
      <main className="main-content">{children}</main>
    </div>
  );
};

export default Layout;
