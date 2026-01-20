# Screen Engine - Quick Start Guide

## Current Status ✓

✅ Backend project structure created
✅ PostgreSQL Docker container running
✅ Database migrations created
⏳ Ready to run the application

## What's Been Set Up

### 1. Docker PostgreSQL Database
- **Container**: `screen-engine-postgres`
- **Port**: 5432
- **Database**: `screen_engine_metadata`
- **Username**: `postgres`
- **Password**: `postgres`
- **Status**: Running and ready

### 2. Backend Project
- **Framework**: Spring Boot 3.2.2 with Java 17
- **Location**: `backend/`
- **Configuration**: `backend/src/main/resources/application.yml`
- **Migrations**: Ready in `backend/src/main/resources/db/migration/`

### 3. Database Migrations Ready
- `V1__create_metadata_tables.sql` - Creates metadata tables
- `V2__insert_sample_data.sql` - Inserts sample screen and customer data

---

## Prerequisites to Run

You need to install:

1. **Java 17 or higher**
   - Download: https://adoptium.net/
   - Verify: `java -version`

2. **Maven 3.8+**
   - Download: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

---

## Running the Application

### Step 1: Verify Docker Container

Check if PostgreSQL is running:

```bash
docker ps | grep screen-engine-postgres
```

If not running, start it:

```bash
docker-compose up -d postgres
```

### Step 2: Install Dependencies

```bash
cd backend
mvn clean install
```

### Step 3: Run the Application

```bash
mvn spring-boot:run
```

Or with a specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Step 4: Verify It's Running

Once the application starts (takes 30-60 seconds), you should see:

```
Started ScreenEngineApplication in X.XXX seconds
```

Test the endpoints:

1. **Health Check**: http://localhost:8080/api/v1/health
2. **Swagger UI**: http://localhost:8080/swagger-ui.html
3. **API Docs**: http://localhost:8080/v3/api-docs

---

## What Happens on First Run

When you run the application for the first time:

1. **Flyway Migrations** automatically execute:
   - Creates metadata tables (t_kul_ekran, t_kul_ekran_tablo, t_kul_ekran_tablo_kolon)
   - Creates sample customer table (t_customer)
   - Inserts sample screen metadata (CUSTOMER_LIST)
   - Inserts 5 sample customer records

2. **Spring Boot** initializes:
   - Database connection pool (HikariCP)
   - REST controllers
   - Security configuration (temporarily permits all)
   - OpenAPI documentation

---

## Testing the API

### Using cURL

```bash
# Health check
curl http://localhost:8080/api/v1/health

# Expected response:
{
  "success": true,
  "data": {
    "status": "UP",
    "application": "Screen Engine",
    "version": "0.1.0",
    "timestamp": "2026-01-19T..."
  },
  "message": "Application is running"
}
```

### Using Browser

- Open: http://localhost:8080/swagger-ui.html
- Explore all available endpoints
- Test API calls directly from Swagger UI

---

## Verify Database

Check that migrations ran successfully:

```bash
# Connect to PostgreSQL
docker exec -it screen-engine-postgres psql -U postgres -d screen_engine_metadata

# List tables
\dt

# Should show:
# - t_kul_ekran
# - t_kul_ekran_tablo
# - t_kul_ekran_tablo_kolon
# - t_kul_ekran_yetki
# - t_customer
# - flyway_schema_history

# Check sample data
SELECT ekran_kod, ekran_ad FROM t_kul_ekran;

# Should show:
# CUSTOMER_LIST | Customer List
# SIMPLE_TEST   | Simple Test Screen

# Exit psql
\q
```

---

## Alternative: Using IntelliJ IDEA or VS Code

### IntelliJ IDEA
1. Open `backend` folder as Maven project
2. Wait for Maven dependencies to download
3. Find `ScreenEngineApplication.java`
4. Right-click → Run 'ScreenEngineApplication'

### VS Code
1. Install "Extension Pack for Java"
2. Open `backend` folder
3. Press F5 to run
4. Or use command palette: "Java: Run Java Application"

---

## Troubleshooting

### Issue: PostgreSQL Connection Refused

**Solution**: Make sure Docker container is running

```bash
docker-compose up -d postgres
docker ps | grep postgres
```

### Issue: Maven Not Found

**Solution**: Install Maven

```bash
# Windows (using Chocolatey)
choco install maven

# Or download from https://maven.apache.org/
```

### Issue: Port 8080 Already in Use

**Solution**: Change port in `application.yml` or use environment variable

```bash
mvn spring-boot:run -Dserver.port=8081
```

Or edit `backend/src/main/resources/application.yml`:

```yaml
server:
  port: 8081
```

### Issue: Flyway Migration Fails

**Solution**: Reset database and restart

```bash
# Stop application
# Drop and recreate database
docker exec -it screen-engine-postgres psql -U postgres -c "DROP DATABASE screen_engine_metadata;"
docker exec -it screen-engine-postgres psql -U postgres -c "CREATE DATABASE screen_engine_metadata;"

# Restart application
mvn spring-boot:run
```

---

## Next Steps

Once the application is running successfully:

1. **Explore Sample Data**
   - The system includes a sample "CUSTOMER_LIST" screen
   - 5 sample customer records are pre-loaded

2. **Implement Core Features** (Next Phase):
   - Domain models (Screen, Table, Column)
   - Metadata repository layer
   - Screen metadata API endpoint
   - Dynamic SQL query builder
   - Data query API endpoint
   - CRUD operations API

3. **Frontend Integration**:
   - Set up React frontend
   - Connect to backend API
   - Render dynamic screens

---

## Useful Docker Commands

```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f postgres

# Access PostgreSQL shell
docker exec -it screen-engine-postgres psql -U postgres -d screen_engine_metadata

# Restart PostgreSQL
docker-compose restart postgres

# View container status
docker-compose ps
```

---

## Additional Services

### pgAdmin (PostgreSQL Web UI)

Start pgAdmin:

```bash
docker-compose up -d pgadmin
```

Access: http://localhost:5050

- Email: `admin@screenengine.com`
- Password: `admin`

Add server in pgAdmin:
- Host: `screen-engine-postgres`
- Port: `5432`
- Database: `screen_engine_metadata`
- Username: `postgres`
- Password: `postgres`

---

## Configuration Files

### Key Files Created

```
CODEEK/
├── docker-compose.yml              # PostgreSQL + pgAdmin setup
├── backend/
│   ├── pom.xml                     # Maven dependencies
│   ├── src/main/resources/
│   │   ├── application.yml         # Main configuration
│   │   └── db/migration/
│   │       ├── V1__create_metadata_tables.sql
│   │       └── V2__insert_sample_data.sql
│   └── src/main/java/com/screenengine/
│       └── ScreenEngineApplication.java
└── QUICKSTART.md                   # This file
```

---

## Support

- **Backend README**: `backend/README.md`
- **Project Overview**: `.claude/PROJECT.md`
- **API Contract**: `.claude/API_CONTRACT.md`
- **Database Schema**: `.claude/DATABASE_SCHEMA.md`

---

**Last Updated**: January 2025
**Version**: 0.1.0
**Status**: Initial Setup Complete
