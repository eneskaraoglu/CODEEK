import { useState } from 'react';
import './App.css';
import { authAPI } from './services/api';

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    fullName: ''
  });
  const [message, setMessage] = useState('');
  const [token, setToken] = useState(localStorage.getItem('token'));

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');

    try {
      if (isLogin) {
        const response = await authAPI.login({
          username: formData.username,
          password: formData.password
        });
        localStorage.setItem('token', response.data.token);
        setToken(response.data.token);
        setMessage('Login successful!');
      } else {
        const response = await authAPI.register(formData);
        setMessage('Registration successful! Please login.');
        setIsLogin(true);
      }
      setFormData({ username: '', email: '', password: '', fullName: '' });
    } catch (error) {
      const errorMsg = error.response?.data?.message || error.response?.data?.error || 'An error occurred';
      setMessage(errorMsg);
    }
  };

  const handleLogout = () => {
    authAPI.logout();
    setToken(null);
    setMessage('Logged out successfully');
  };

  if (token) {
    return (
      <div className="App">
        <div className="container">
          <h1>Welcome!</h1>
          <p>You are logged in</p>
          <p className="token-display">Token: {token.substring(0, 20)}...</p>
          <button onClick={handleLogout} className="logout-btn">
            Logout
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="App">
      <div className="container">
        <h1>{isLogin ? 'Login' : 'Register'}</h1>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              name="username"
              placeholder="Username"
              value={formData.username}
              onChange={handleChange}
              required
              minLength={3}
              maxLength={50}
            />
          </div>

          {!isLogin && (
            <>
              <div className="form-group">
                <input
                  type="text"
                  name="fullName"
                  placeholder="Full Name"
                  value={formData.fullName}
                  onChange={handleChange}
                  required
                  minLength={1}
                  maxLength={200}
                />
              </div>
              <div className="form-group">
                <input
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                />
              </div>
            </>
          )}

          <div className="form-group">
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              required
              minLength={6}
              maxLength={100}
            />
          </div>

          <button type="submit" className="submit-btn">
            {isLogin ? 'Login' : 'Register'}
          </button>
        </form>

        {message && <p className={`message ${message.includes('successful') ? 'success' : 'error'}`}>{message}</p>}

        <p className="toggle-text">
          {isLogin ? "Don't have an account? " : 'Already have an account? '}
          <span onClick={() => setIsLogin(!isLogin)} className="toggle-link">
            {isLogin ? 'Register' : 'Login'}
          </span>
        </p>
      </div>
    </div>
  );
}

export default App;
