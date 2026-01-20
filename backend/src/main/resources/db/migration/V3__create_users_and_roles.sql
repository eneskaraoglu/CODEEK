-- User Authentication and Authorization Tables
-- Version: 1.0.0
-- Description: Creates tables for users, roles, and permissions

-- =============================================================================
-- 1. T_ROLE (Roles)
-- =============================================================================
CREATE TABLE t_role (
    role_id             BIGSERIAL           PRIMARY KEY,
    role_name           VARCHAR(50)         NOT NULL UNIQUE,
    role_code           VARCHAR(50)         NOT NULL UNIQUE,
    description         VARCHAR(200),
    active              INTEGER             DEFAULT 1,
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for T_ROLE
CREATE INDEX idx_role_name ON t_role(role_name);
CREATE INDEX idx_role_code ON t_role(role_code);

-- Comments for T_ROLE
COMMENT ON TABLE t_role IS 'User roles for authorization';
COMMENT ON COLUMN t_role.role_id IS 'Primary key, unique role identifier';
COMMENT ON COLUMN t_role.role_name IS 'Role display name (e.g., Administrator)';
COMMENT ON COLUMN t_role.role_code IS 'Role code (e.g., ROLE_ADMIN, ROLE_USER)';
COMMENT ON COLUMN t_role.description IS 'Role description';
COMMENT ON COLUMN t_role.active IS 'Active flag: 1=Active, 0=Inactive';

-- =============================================================================
-- 2. T_USER (Users)
-- =============================================================================
CREATE TABLE t_user (
    user_id             BIGSERIAL           PRIMARY KEY,
    username            VARCHAR(50)         NOT NULL UNIQUE,
    password            VARCHAR(255)        NOT NULL,
    email               VARCHAR(100)        UNIQUE,
    full_name           VARCHAR(200)        NOT NULL,
    phone               VARCHAR(20),
    active              INTEGER             DEFAULT 1,
    locked              INTEGER             DEFAULT 0,
    password_expired    INTEGER             DEFAULT 0,
    last_login          TIMESTAMP,
    failed_attempts     INTEGER             DEFAULT 0,
    fabrika_kod         BIGINT              NOT NULL,
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(50),
    updated_by          VARCHAR(50)
);

-- Indexes for T_USER
CREATE INDEX idx_user_username ON t_user(username);
CREATE INDEX idx_user_email ON t_user(email);
CREATE INDEX idx_user_fabrika ON t_user(fabrika_kod);
CREATE INDEX idx_user_active ON t_user(active);

-- Comments for T_USER
COMMENT ON TABLE t_user IS 'System users with authentication credentials';
COMMENT ON COLUMN t_user.user_id IS 'Primary key, unique user identifier';
COMMENT ON COLUMN t_user.username IS 'Unique username for login';
COMMENT ON COLUMN t_user.password IS 'BCrypt hashed password';
COMMENT ON COLUMN t_user.email IS 'User email address';
COMMENT ON COLUMN t_user.full_name IS 'User full name';
COMMENT ON COLUMN t_user.active IS 'Active flag: 1=Active, 0=Inactive';
COMMENT ON COLUMN t_user.locked IS 'Lock flag: 1=Locked, 0=Unlocked';
COMMENT ON COLUMN t_user.password_expired IS 'Password expired flag';
COMMENT ON COLUMN t_user.last_login IS 'Last successful login timestamp';
COMMENT ON COLUMN t_user.failed_attempts IS 'Number of consecutive failed login attempts';
COMMENT ON COLUMN t_user.fabrika_kod IS 'Factory code for multi-tenant';

-- =============================================================================
-- 3. T_USER_ROLE (User-Role Mapping)
-- =============================================================================
CREATE TABLE t_user_role (
    user_role_id        BIGSERIAL           PRIMARY KEY,
    user_id             BIGINT              NOT NULL,
    role_id             BIGINT              NOT NULL,
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(50),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id)
        REFERENCES t_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id)
        REFERENCES t_role(role_id) ON DELETE CASCADE,
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);

-- Indexes for T_USER_ROLE
CREATE INDEX idx_user_role_user ON t_user_role(user_id);
CREATE INDEX idx_user_role_role ON t_user_role(role_id);

-- Comments for T_USER_ROLE
COMMENT ON TABLE t_user_role IS 'Many-to-many relationship between users and roles';
COMMENT ON COLUMN t_user_role.user_id IS 'Foreign key to t_user';
COMMENT ON COLUMN t_user_role.role_id IS 'Foreign key to t_role';

-- =============================================================================
-- 4. T_PERMISSION (Permissions - Optional for fine-grained control)
-- =============================================================================
CREATE TABLE t_permission (
    permission_id       BIGSERIAL           PRIMARY KEY,
    permission_name     VARCHAR(100)        NOT NULL UNIQUE,
    permission_code     VARCHAR(100)        NOT NULL UNIQUE,
    resource            VARCHAR(100),
    action              VARCHAR(50),
    description         VARCHAR(200),
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for T_PERMISSION
CREATE INDEX idx_permission_code ON t_permission(permission_code);
CREATE INDEX idx_permission_resource ON t_permission(resource);

-- Comments for T_PERMISSION
COMMENT ON TABLE t_permission IS 'Fine-grained permissions for resources';
COMMENT ON COLUMN t_permission.permission_code IS 'Permission code (e.g., SCREEN_CREATE, SCREEN_READ)';
COMMENT ON COLUMN t_permission.resource IS 'Resource name (e.g., SCREEN, USER)';
COMMENT ON COLUMN t_permission.action IS 'Action type (CREATE, READ, UPDATE, DELETE)';

-- =============================================================================
-- 5. T_ROLE_PERMISSION (Role-Permission Mapping)
-- =============================================================================
CREATE TABLE t_role_permission (
    role_permission_id  BIGSERIAL           PRIMARY KEY,
    role_id             BIGINT              NOT NULL,
    permission_id       BIGINT              NOT NULL,
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_role_perm_role FOREIGN KEY (role_id)
        REFERENCES t_role(role_id) ON DELETE CASCADE,
    CONSTRAINT fk_role_perm_permission FOREIGN KEY (permission_id)
        REFERENCES t_permission(permission_id) ON DELETE CASCADE,
    CONSTRAINT uk_role_permission UNIQUE (role_id, permission_id)
);

-- Indexes for T_ROLE_PERMISSION
CREATE INDEX idx_role_perm_role ON t_role_permission(role_id);
CREATE INDEX idx_role_perm_permission ON t_role_permission(permission_id);

-- =============================================================================
-- 6. T_REFRESH_TOKEN (Refresh Tokens)
-- =============================================================================
CREATE TABLE t_refresh_token (
    token_id            BIGSERIAL           PRIMARY KEY,
    user_id             BIGINT              NOT NULL,
    token               VARCHAR(500)        NOT NULL UNIQUE,
    expires_at          TIMESTAMP           NOT NULL,
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    revoked             INTEGER             DEFAULT 0,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id)
        REFERENCES t_user(user_id) ON DELETE CASCADE
);

-- Indexes for T_REFRESH_TOKEN
CREATE INDEX idx_refresh_token_user ON t_refresh_token(user_id);
CREATE INDEX idx_refresh_token_token ON t_refresh_token(token);
CREATE INDEX idx_refresh_token_expires ON t_refresh_token(expires_at);

-- Comments for T_REFRESH_TOKEN
COMMENT ON TABLE t_refresh_token IS 'Refresh tokens for JWT authentication';
COMMENT ON COLUMN t_refresh_token.token IS 'Refresh token value';
COMMENT ON COLUMN t_refresh_token.expires_at IS 'Token expiration timestamp';
COMMENT ON COLUMN t_refresh_token.revoked IS 'Revoked flag: 1=Revoked, 0=Valid';

-- =============================================================================
-- Triggers for updated_at
-- =============================================================================
CREATE TRIGGER update_t_role_updated_at
    BEFORE UPDATE ON t_role
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_t_user_updated_at
    BEFORE UPDATE ON t_user
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =============================================================================
-- Initial Data: Default Roles
-- =============================================================================
INSERT INTO t_role (role_name, role_code, description, active) VALUES
    ('Super Administrator', 'ROLE_SUPER_ADMIN', 'Full system access with all permissions', 1),
    ('Administrator', 'ROLE_ADMIN', 'Administrative access to manage users and screens', 1),
    ('Manager', 'ROLE_MANAGER', 'Manager access with read/write permissions', 1),
    ('User', 'ROLE_USER', 'Standard user access with limited permissions', 1),
    ('Viewer', 'ROLE_VIEWER', 'Read-only access to screens', 1);

-- =============================================================================
-- Initial Data: Default Permissions
-- =============================================================================
INSERT INTO t_permission (permission_name, permission_code, resource, action, description) VALUES
    -- Screen permissions
    ('Create Screen', 'SCREEN_CREATE', 'SCREEN', 'CREATE', 'Create new screens'),
    ('Read Screen', 'SCREEN_READ', 'SCREEN', 'READ', 'View screens'),
    ('Update Screen', 'SCREEN_UPDATE', 'SCREEN', 'UPDATE', 'Modify screens'),
    ('Delete Screen', 'SCREEN_DELETE', 'SCREEN', 'DELETE', 'Delete screens'),

    -- User permissions
    ('Create User', 'USER_CREATE', 'USER', 'CREATE', 'Create new users'),
    ('Read User', 'USER_READ', 'USER', 'READ', 'View users'),
    ('Update User', 'USER_UPDATE', 'USER', 'UPDATE', 'Modify users'),
    ('Delete User', 'USER_DELETE', 'USER', 'DELETE', 'Delete users'),

    -- Role permissions
    ('Manage Roles', 'ROLE_MANAGE', 'ROLE', 'MANAGE', 'Manage roles and permissions'),

    -- Data permissions
    ('Export Data', 'DATA_EXPORT', 'DATA', 'EXPORT', 'Export data to files'),
    ('Import Data', 'DATA_IMPORT', 'DATA', 'IMPORT', 'Import data from files');

-- =============================================================================
-- Initial Data: Assign Permissions to Roles
-- =============================================================================
DO $$
DECLARE
    v_super_admin_id BIGINT;
    v_admin_id BIGINT;
    v_manager_id BIGINT;
    v_user_id BIGINT;
    v_viewer_id BIGINT;
BEGIN
    -- Get role IDs
    SELECT role_id INTO v_super_admin_id FROM t_role WHERE role_code = 'ROLE_SUPER_ADMIN';
    SELECT role_id INTO v_admin_id FROM t_role WHERE role_code = 'ROLE_ADMIN';
    SELECT role_id INTO v_manager_id FROM t_role WHERE role_code = 'ROLE_MANAGER';
    SELECT role_id INTO v_user_id FROM t_role WHERE role_code = 'ROLE_USER';
    SELECT role_id INTO v_viewer_id FROM t_role WHERE role_code = 'ROLE_VIEWER';

    -- SUPER_ADMIN: All permissions
    INSERT INTO t_role_permission (role_id, permission_id)
    SELECT v_super_admin_id, permission_id FROM t_permission;

    -- ADMIN: All except super admin specific
    INSERT INTO t_role_permission (role_id, permission_id)
    SELECT v_admin_id, permission_id FROM t_permission;

    -- MANAGER: CRUD screens and data, read users
    INSERT INTO t_role_permission (role_id, permission_id)
    SELECT v_manager_id, permission_id FROM t_permission
    WHERE permission_code IN (
        'SCREEN_CREATE', 'SCREEN_READ', 'SCREEN_UPDATE', 'SCREEN_DELETE',
        'USER_READ', 'DATA_EXPORT', 'DATA_IMPORT'
    );

    -- USER: Read/Update screens, export data
    INSERT INTO t_role_permission (role_id, permission_id)
    SELECT v_user_id, permission_id FROM t_permission
    WHERE permission_code IN ('SCREEN_READ', 'SCREEN_UPDATE', 'DATA_EXPORT');

    -- VIEWER: Read only
    INSERT INTO t_role_permission (role_id, permission_id)
    SELECT v_viewer_id, permission_id FROM t_permission
    WHERE permission_code IN ('SCREEN_READ', 'USER_READ');
END $$;

-- =============================================================================
-- Initial Data: Default Admin User
-- Password: admin123 (BCrypt hashed)
-- =============================================================================
INSERT INTO t_user (username, password, email, full_name, active, fabrika_kod, created_by) VALUES
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'admin@screenengine.com', 'System Administrator', 1, 101, 'SYSTEM'),
    ('user', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'user@screenengine.com', 'Test User', 1, 101, 'SYSTEM'),
    ('manager', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'manager@screenengine.com', 'Test Manager', 1, 101, 'SYSTEM');

-- Assign roles to default users
DO $$
DECLARE
    v_admin_user_id BIGINT;
    v_user_user_id BIGINT;
    v_manager_user_id BIGINT;
    v_admin_role_id BIGINT;
    v_user_role_id BIGINT;
    v_manager_role_id BIGINT;
BEGIN
    -- Get user IDs
    SELECT user_id INTO v_admin_user_id FROM t_user WHERE username = 'admin';
    SELECT user_id INTO v_user_user_id FROM t_user WHERE username = 'user';
    SELECT user_id INTO v_manager_user_id FROM t_user WHERE username = 'manager';

    -- Get role IDs
    SELECT role_id INTO v_admin_role_id FROM t_role WHERE role_code = 'ROLE_ADMIN';
    SELECT role_id INTO v_user_role_id FROM t_role WHERE role_code = 'ROLE_USER';
    SELECT role_id INTO v_manager_role_id FROM t_role WHERE role_code = 'ROLE_MANAGER';

    -- Assign roles
    INSERT INTO t_user_role (user_id, role_id, created_by) VALUES
        (v_admin_user_id, v_admin_role_id, 'SYSTEM'),
        (v_user_user_id, v_user_role_id, 'SYSTEM'),
        (v_manager_user_id, v_manager_role_id, 'SYSTEM');
END $$;
