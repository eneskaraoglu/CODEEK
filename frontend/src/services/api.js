import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle 401 responses (unauthorized) - redirect to login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      // Clear auth data
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      // Redirect to login
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API calls
export const authAPI = {
  register: (userData) => api.post('/v1/auth/register', userData),
  login: (credentials) => api.post('/v1/auth/login', credentials),
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }
};

// User API calls
export const userAPI = {
  getProfile: () => api.get('/v1/users/me'),
  updateProfile: (data) => api.put('/users/profile', data),

  // Admin user management
  getAllUsers: () => api.get('/v1/users'),
  getUserById: (userId) => api.get(`/v1/users/${userId}`),
  updateUser: (userId, data) => api.put(`/v1/users/${userId}`, data),
  deleteUser: (userId) => api.delete(`/v1/users/${userId}`),
  toggleUserStatus: (userId) => api.patch(`/v1/users/${userId}/toggle-status`),
};

export default api;
