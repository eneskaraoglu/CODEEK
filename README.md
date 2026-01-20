# Screen Engine - Universal Dynamic Screen Platform

A metadata-driven platform for creating dynamic data entry and query screens without writing code.

## 🚀 Quick Start

**PostgreSQL Database is already running!** ✅

To start the backend application:

1. **Install Prerequisites**:
   - Java 17+ (https://adoptium.net/)
   - Maven 3.8+ (https://maven.apache.org/download.cgi)

2. **Run the Backend**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

3. **Access the Application**:
   - Health Check: http://localhost:8080/api/v1/health
   - Swagger UI: http://localhost:8080/swagger-ui.html

📖 **See [QUICKSTART.md](QUICKSTART.md) for detailed instructions**

---

## 📋 Project Status

### ✅ Completed Setup

- [x] Backend Spring Boot project structure
- [x] PostgreSQL database running in Docker
- [x] Database migration scripts ready
- [x] Sample metadata and data included
- [x] Basic REST API structure
- [x] OpenAPI/Swagger documentation setup
- [x] CORS configuration
- [x] Health check endpoint

### 🚧 Next Steps

- [ ] Domain models implementation
- [ ] Metadata repository layer
- [ ] Dynamic SQL query builder
- [ ] Screen metadata API endpoints
- [ ] Data query and CRUD APIs
- [ ] JWT authentication
- [ ] Frontend React setup

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend (React)                      │
│  - Generic Screen Renderer                              │
│  - Metadata-driven UI Components                        │
└─────────────────────────────────────────────────────────┘
                           ↕ REST API
┌─────────────────────────────────────────────────────────┐
│                Backend (Spring Boot)                     │
│  - Metadata Engine Layer                                │
│  - SQL Engine Layer                                     │
│  - Database Abstraction Layer                           │
└─────────────────────────────────────────────────────────┘
                           ↕
┌─────────────────────────────────────────────────────────┐
│              PostgreSQL Database                         │
│  - Metadata Tables (Screens, Tables, Columns)           │
│  - Data Tables (Customer data, etc.)                    │
└─────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
CODEEK/
├── .claude/                        # Documentation
│   ├── PROJECT.md                  # Complete project overview
│   ├── API_CONTRACT.md             # REST API specification
│   └── DATABASE_SCHEMA.md          # Database schema details
├── backend/                        # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/screenengine/
│   │   │   │   ├── config/         # Configuration
│   │   │   │   ├── controller/     # REST controllers
│   │   │   │   ├── service/        # Business logic
│   │   │   │   ├── repository/     # Database access
│   │   │   │   ├── model/          # Domain models
│   │   │   │   ├── dto/            # Data transfer objects
│   │   │   │   ├── sql/            # SQL engine
│   │   │   │   └── security/       # Security
│   │   │   └── resources/
│   │   │       ├── application.yml # Configuration
│   │   │       └── db/migration/   # Flyway migrations
│   │   └── test/                   # Tests
│   ├── pom.xml                     # Maven dependencies
│   └── README.md                   # Backend documentation
├── frontend/                       # React frontend (TBD)
├── docker-compose.yml              # PostgreSQL + pgAdmin
├── QUICKSTART.md                   # Quick start guide
└── README.md                       # This file
```

---

## 🔑 Key Features

- **Metadata-Driven UI**: Screens generated from database metadata
- **Database Agnostic**: Oracle, PostgreSQL, MySQL, SQL Server
- **20+ Column Types**: String, Number, Date, Boolean, ComboBox, JOIN/Lookup
- **Dynamic SQL**: Automatic query building with JOINs and WHERE clauses
- **Multi-Tenant**: Factory/organization-based data isolation
- **Role-Based Access**: CRUD-level permissions
- **RESTful API**: Clean JSON API with OpenAPI documentation

---

## 🐳 Docker Services

### PostgreSQL Database
- **Container**: `screen-engine-postgres`
- **Port**: 5432
- **Database**: `screen_engine_metadata`
- **Credentials**: postgres/postgres

### pgAdmin (Optional)
- **Port**: 5050
- **URL**: http://localhost:5050
- **Credentials**: admin@screenengine.com / admin

**Commands**:
```bash
# Start PostgreSQL
docker-compose up -d postgres

# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f postgres
```

---

## 🛠️ Tech Stack

### Backend
- Spring Boot 3.2.2
- Java 17
- JOOQ (SQL query builder)
- Spring Data JDBC
- HikariCP (connection pooling)
- Flyway (migrations)
- SpringDoc OpenAPI

### Frontend (Planned)
- React 18+
- TypeScript 5+
- Ant Design Pro
- TanStack Query
- Vite

### Database
- PostgreSQL 16 (primary)
- Oracle 11g+ (supported)
- MySQL 8+ (supported)
- SQL Server 2016+ (supported)

---

## 📚 Documentation

- **[QUICKSTART.md](QUICKSTART.md)** - Get started quickly
- **[backend/README.md](backend/README.md)** - Backend details
- **[.claude/PROJECT.md](.claude/PROJECT.md)** - Complete project overview
- **[.claude/API_CONTRACT.md](.claude/API_CONTRACT.md)** - REST API spec
- **[.claude/DATABASE_SCHEMA.md](.claude/DATABASE_SCHEMA.md)** - Database schema

---

## 🧪 Sample Data

The system includes pre-loaded sample data:

### Sample Screen: CUSTOMER_LIST
- Query panel with 6 searchable fields
- Table display with 9 columns
- Full CRUD operations

### Sample Data: t_customer
- 5 sample customer records
- Various status types (Active, Passive, Suspended)
- Demonstrates all column types

**Test it**: Once the backend is running, use Swagger UI to explore the sample data.

---

## 🔧 Configuration

### Environment Variables

```bash
# Database
METADATA_DB_URL=jdbc:postgresql://localhost:5432/screen_engine_metadata
METADATA_DB_USERNAME=postgres
METADATA_DB_PASSWORD=postgres

# Server
SERVER_PORT=8080

# System Parameters
SYSTEM_FACTORY=101
SYSTEM_USER=ADMIN
```

### Profiles

```bash
# Development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 🤝 Contributing

This project is in active development. Contributions welcome!

1. Read the documentation in `.claude/` directory
2. Create a feature branch
3. Make changes with tests
4. Update documentation
5. Submit a pull request

---

## 📝 Version

**Current Version**: 0.1.0 (Initial Development)

**Roadmap**:
- v0.1.0: Project setup and core structure ✅
- v0.2.0: Metadata API and SQL engine
- v0.3.0: CRUD operations
- v0.4.0: Frontend integration
- v1.0.0: Production-ready release

---

## 📞 Support

- **Issues**: GitHub Issues
- **Discussions**: GitHub Discussions
- **Documentation**: `.claude/` directory

---

## 📄 License

[To be determined]

---

**Built with ❤️ using Spring Boot, React, and PostgreSQL**

**Last Updated**: January 2025
