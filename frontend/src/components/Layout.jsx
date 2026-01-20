import { useNavigate, useLocation } from 'react-router-dom';
import { authAPI } from '../services/api';

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
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <h2 className="text-2xl font-bold text-primary-600">CODEEK App</h2>
            </div>

            <div className="flex space-x-2">
              <button
                onClick={() => navigate('/dashboard')}
                className={`px-4 py-2 rounded-lg font-medium transition ${
                  isActive('/dashboard')
                    ? 'bg-primary-600 text-white'
                    : 'text-gray-700 hover:bg-gray-100'
                }`}
              >
                Dashboard
              </button>
              <button
                onClick={() => navigate('/profile')}
                className={`px-4 py-2 rounded-lg font-medium transition ${
                  isActive('/profile')
                    ? 'bg-primary-600 text-white'
                    : 'text-gray-700 hover:bg-gray-100'
                }`}
              >
                Profile
              </button>
              {user.role === 'ADMIN' && (
                <button
                  onClick={() => navigate('/users')}
                  className={`px-4 py-2 rounded-lg font-medium transition ${
                    isActive('/users')
                      ? 'bg-primary-600 text-white'
                      : 'text-gray-700 hover:bg-gray-100'
                  }`}
                >
                  Users
                </button>
              )}
            </div>

            <div className="flex items-center space-x-4">
              <span className="font-medium text-gray-700">
                {user.fullName || user.username}
              </span>
              <span className="px-3 py-1 bg-gray-200 text-gray-700 rounded-full text-sm font-medium">
                {user.role}
              </span>
              <button
                onClick={handleLogout}
                className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg font-medium transition"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>
    </div>
  );
};

export default Layout;
