# Full-Stack Application Guide

## Application Overview

Your CODEEK application is now a complete full-stack web application with:
- **Backend:** Spring Boot REST API with JWT authentication
- **Frontend:** React SPA with routing and role-based access control
- **Database:** PostgreSQL with Flyway migrations

## Running Servers

### Backend
- **URL:** http://localhost:8080
- **Command:** `cd backend && ./mvnw spring-boot:run`

### Frontend
- **URL:** http://localhost:3000
- **Command:** `cd frontend && npm run dev`

## Application Features

### Authentication & Authorization
- JWT-based authentication
- Role-based access control (USER, ADMIN)
- Secure password hashing with BCrypt
- Token stored in localStorage
- Auto-redirect to login when not authenticated

### Pages & Routes

#### `/login` - Login/Register Page
- Toggle between login and registration
- Form validation (client & server)
- Beautiful gradient background
- Error message display

#### `/dashboard` - Dashboard (Protected)
- Welcome message with user's name
- Statistics cards (users, projects, tasks)
- Recent activity feed
- Accessible to all authenticated users

#### `/profile` - User Profile (Protected)
- View and edit profile information
- Avatar with user's initial
- Role badge display
- Change password button
- Accessible to all authenticated users

#### `/users` - User Management (Admin Only)
- View all users in a table
- Search by username, name, or email
- Filter by role (ALL, ADMIN, USER)
- User status toggle (Active/Inactive)
- User statistics summary
- **Only accessible to ADMIN role**

### Navigation
- Top navigation bar with:
  - Brand logo
  - Navigation links (Dashboard, Profile, Users)
  - User info display (name + role badge)
  - Logout button
- Active link highlighting
- Responsive design

## User Roles

### USER Role
- Access to Dashboard
- Access to Profile
- Cannot access Users management

### ADMIN Role
- Access to Dashboard
- Access to Profile
- Access to Users management
- Full admin capabilities

## API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login and get JWT token

### Response Format
All API responses follow this structure:
```json
{
  "success": true,
  "data": {
    "token": "jwt-token-here",
    "user": {
      "userId": 1,
      "username": "admin",
      "email": "admin@example.com",
      "fullName": "Administrator",
      "role": "ADMIN",
      "roles": ["ROLE_ADMIN"]
    }
  },
  "message": "Login successful"
}
```

## File Structure

### Frontend (`frontend/`)
```
src/
├── components/
│   ├── Layout.jsx          # Main layout with navigation
│   ├── Layout.css
│   └── ProtectedRoute.jsx  # Route protection wrapper
├── pages/
│   ├── Login.jsx          # Login/Register page
│   ├── Auth.css
│   ├── Dashboard.jsx      # Main dashboard
│   ├── Dashboard.css
│   ├── Profile.jsx        # User profile
│   ├── Profile.css
│   ├── Users.jsx          # Admin user management
│   └── Users.css
├── services/
│   └── api.js            # API client with axios
├── App.jsx               # Main app with routing
└── App.css
```

### Backend (`backend/`)
```
src/main/java/com/screenengine/
├── controller/
│   └── AuthController.java          # Auth endpoints
├── service/
│   └── AuthService.java             # Auth business logic
├── dto/
│   ├── AuthResponse.java            # Login/register response
│   ├── LoginRequest.java
│   └── RegisterRequest.java
├── security/
│   ├── JwtTokenProvider.java        # JWT generation/validation
│   ├── JwtAuthenticationFilter.java # JWT filter
│   └── UserPrincipal.java           # User details
├── config/
│   └── SecurityConfig.java          # Spring Security config
└── exception/
    └── GlobalExceptionHandler.java  # Error handling
```

## Testing the Application

1. **Start both servers** (backend and frontend)

2. **Register a new user:**
   - Go to http://localhost:3000
   - Click "Register"
   - Fill in: Username, Full Name, Email, Password
   - Submit

3. **Login:**
   - Enter your username and password
   - You'll be redirected to the dashboard

4. **Explore the application:**
   - View the dashboard
   - Edit your profile
   - If you're an admin, manage users

## Default User (If seeded)
- **Username:** admin
- **Password:** admin123
- **Role:** ADMIN

## Key Features Implemented

### Frontend
✅ React Router for navigation
✅ Protected routes with authentication check
✅ Role-based route protection
✅ Axios interceptor for JWT token injection
✅ Beautiful UI with CSS animations
✅ Responsive design
✅ Form validation
✅ Error handling and display
✅ Loading states

### Backend
✅ JWT authentication
✅ Spring Security configuration
✅ Role-based access control
✅ Password encryption (BCrypt)
✅ Input validation
✅ Global exception handling
✅ PostgreSQL database
✅ Flyway migrations
✅ User info returned with token

## Next Steps (Optional Enhancements)

1. **Add more features:**
   - Password reset functionality
   - Email verification
   - Profile image upload
   - Real-time notifications

2. **Improve UX:**
   - Toast notifications
   - Loading spinners
   - Better error messages
   - Dark mode toggle

3. **Add more admin features:**
   - Create/edit/delete users
   - Assign roles
   - View user activity logs
   - System settings

4. **Security enhancements:**
   - Refresh tokens
   - Token blacklist
   - Rate limiting
   - CORS configuration

## Troubleshooting

### Backend won't start
- Check if PostgreSQL is running
- Verify database credentials in `application.properties`
- Ensure port 8080 is available

### Frontend won't start
- Run `npm install` in frontend directory
- Ensure port 3000 is available
- Check for Node.js version compatibility

### Login fails
- Check backend logs
- Verify user exists in database
- Check JWT secret is configured

### ADMIN pages not accessible
- Verify user has ADMIN role in database
- Check role is being returned in login response
- Verify ProtectedRoute is checking role correctly

## Support

For issues or questions, refer to the project documentation or backend logs for debugging.

---

**Built with:**
- Spring Boot 3.2.2
- React 18
- Vite
- React Router 6
- Axios
- PostgreSQL 16
- JWT
