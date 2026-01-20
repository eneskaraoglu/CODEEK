# JWT Authentication Test Guide

## 🎉 JWT Security Implemented Successfully!

Your Screen Engine backend now has complete JWT authentication and role-based authorization!

## 📋 What's Been Implemented

### Security Features
✅ **JWT Token Generation & Validation**
✅ **BCrypt Password Encryption**
✅ **Role-Based Access Control (RBAC)**
✅ **User Authentication & Registration**
✅ **Method-Level Security Annotations**
✅ **Stateless Session Management**

### Database Tables Created
- `t_user` - User accounts with encrypted passwords
- `t_role` - Role definitions (SUPER_ADMIN, ADMIN, MANAGER, USER, VIEWER)
- `t_user_role` - User-Role mapping
- `t_permission` - Fine-grained permissions
- `t_role_permission` - Role-Permission mapping
- `t_refresh_token` - Refresh token storage

### Default Users
| Username | Password | Role | Description |
|----------|----------|------|-------------|
| admin | admin123 | ROLE_ADMIN | Administrator |
| user | admin123 | ROLE_USER | Regular user |
| manager | admin123 | ROLE_MANAGER | Manager |

## 🔐 Testing Authentication

### 1. Login (Get JWT Token)

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600000,
    "user": {
      "userId": 1,
      "username": "admin",
      "email": "admin@screenengine.com",
      "fullName": "System Administrator",
      "fabrikaKod": 101,
      "roles": ["ROLE_ADMIN"]
    }
  },
  "message": "Login successful"
}
```

**Save the token for subsequent requests!**

### 2. Register New User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123",
    "email": "newuser@example.com",
    "fullName": "New User",
    "fabrikaKod": 101
  }'
```

### 3. Access Protected Endpoint (With Token)

```bash
# Replace YOUR_JWT_TOKEN with the actual token from login response
TOKEN="YOUR_JWT_TOKEN"

curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "username": "admin",
    "email": "admin@screenengine.com",
    "fullName": "System Administrator",
    "fabrikaKod": 101,
    "roles": [{"authority": "ROLE_ADMIN"}]
  }
}
```

### 4. Test Role-Based Authorization

#### Admin-Only Endpoint
```bash
curl -X GET http://localhost:8080/api/v1/users/admin-only \
  -H "Authorization: Bearer $TOKEN"
```

#### Manager or Higher
```bash
curl -X GET http://localhost:8080/api/v1/users/manager-or-higher \
  -H "Authorization: Bearer $TOKEN"
```

#### Without Token (Should Fail)
```bash
curl -X GET http://localhost:8080/api/v1/users/me
```

**Expected Response:** 403 Forbidden

## 🛡️ Security Configuration

### Public Endpoints (No Authentication Required)
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/register`
- `GET /api/v1/health`
- `/swagger-ui/**`
- `/actuator/health`

### Protected Endpoints (Requires JWT)
- All `/api/v1/users/**` endpoints
- All other `/api/v1/**` endpoints

## 📝 Using Authorization Annotations

### Examples from UserController

```java
// Any authenticated user
@PreAuthorize("isAuthenticated()")
public ApiResponse<String> listUsers() { ... }

// Admin only
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public ApiResponse<String> adminOnlyEndpoint() { ... }

// Manager or higher
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'SUPER_ADMIN')")
public ApiResponse<String> managerOrHigherEndpoint() { ... }

// Specific permission
@PreAuthorize("hasAuthority('USER_CREATE')")
public ApiResponse<String> createUser() { ... }
```

## 🔧 Troubleshooting

### Issue: "Invalid username or password"
**Cause**: Password hash mismatch
**Solution**: The default password is "admin123" (BCrypt hashed in database)

### Issue: 403 Forbidden
**Cause**: Missing or invalid JWT token
**Solution**:
1. Login to get a valid token
2. Include token in Authorization header: `Bearer <token>`

### Issue: Token expired
**Cause**: JWT token has expired (default: 1 hour)
**Solution**: Login again to get a new token

### Issue: Access denied for role
**Cause**: User doesn't have required role
**Solution**: Assign appropriate role to user in database

## 🎯 Complete cURL Example Workflow

```bash
# 1. Login as admin
RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

# 2. Extract token (requires jq)
TOKEN=$(echo $RESPONSE | jq -r '.data.token')

echo "Token: $TOKEN"

# 3. Get current user info
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer $TOKEN" | jq

# 4. Access admin-only endpoint
curl -X GET http://localhost:8080/api/v1/users/admin-only \
  -H "Authorization: Bearer $TOKEN" | jq

# 5. Try to access without token (should fail)
curl -X GET http://localhost:8080/api/v1/users/admin-only
```

## 📊 Database Queries

### View All Users
```sql
SELECT user_id, username, email, full_name, active
FROM t_user;
```

### View User Roles
```sql
SELECT u.username, r.role_name, r.role_code
FROM t_user u
JOIN t_user_role ur ON u.user_id = ur.user_id
JOIN t_role r ON ur.role_id = r.role_id
WHERE u.username = 'admin';
```

### View Role Permissions
```sql
SELECT r.role_name, p.permission_name, p.permission_code
FROM t_role r
JOIN t_role_permission rp ON r.role_id = rp.role_id
JOIN t_permission p ON rp.permission_id = p.permission_id
WHERE r.role_code = 'ROLE_ADMIN';
```

## 🚀 Next Steps

1. **Implement Refresh Tokens** - Add token refresh logic
2. **Add Password Reset** - Implement forgot password flow
3. **Email Verification** - Add email verification for new users
4. **Account Lockout** - Implement account lockout after failed attempts
5. **Session Management** - Add active session tracking
6. **Audit Logging** - Log authentication events

---

**Status**: ✅ JWT Authentication Fully Operational
**Last Updated**: January 2025
