# Screen Engine Backend

Universal Dynamic Screen Platform - Backend Service

## Overview

The Screen Engine backend is a Spring Boot application that provides a metadata-driven platform for creating dynamic data entry and query screens. It reads metadata from database tables and automatically generates RESTful APIs with full CRUD capabilities.

### Key Features

- **Metadata-Driven Architecture**: Screens are generated from database metadata
- **Database Agnostic**: Supports Oracle, PostgreSQL, MySQL, and SQL Server
- **Dynamic SQL Generation**: Automatic query building with JOINs, WHERE clauses, and parameters
- **20+ Column Types**: String, Number, Date, Boolean, ComboBox, Lookup (JOIN), etc.
- **Role-Based Access Control**: CRUD-level permissions (to be implemented)
- **Multi-Tenant Support**: Factory/organization-based data isolation
- **RESTful API**: Clean JSON API following REST principles
- **OpenAPI Documentation**: Swagger UI for API exploration

## Tech Stack

- **Framework**: Spring Boot 3.2.2
- **Language**: Java 17
- **SQL Builder**: JOOQ (database-agnostic query building)
- **Database Access**: Spring Data JDBC
- **Connection Pooling**: HikariCP
- **Security**: Spring Security (JWT to be implemented)
- **API Documentation**: SpringDoc OpenAPI 3.0
- **Build Tool**: Maven 3.8+

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/screenengine/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── service/             # Business logic
│   │   │   ├── repository/          # Database access
│   │   │   ├── model/               # Domain models
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── mapper/              # DTO mappers
│   │   │   ├── sql/                 # SQL engine
│   │   │   │   ├── builder/         # Query builders
│   │   │   │   ├── dialect/         # Database dialects
│   │   │   │   └── resolver/        # Parameter resolvers
│   │   │   ├── security/            # Security & auth
│   │   │   ├── exception/           # Custom exceptions
│   │   │   └── util/                # Utility classes
│   │   └── resources/
│   │       ├── application.yml      # Main configuration
│   │       └── db/migration/        # Flyway migrations
│   └── test/
│       └── java/com/screenengine/   # Unit and integration tests
├── pom.xml                          # Maven dependencies
└── README.md                        # This file
```

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL 12+ (or Oracle 11g+, MySQL 8+, SQL Server 2016+)
- Docker (optional, for running databases locally)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd screen-engine/backend
```

### 2. Configure Database

Edit `src/main/resources/application.yml` or set environment variables:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/screen_engine_metadata
    username: postgres
    password: postgres
```

Or use environment variables:

```bash
export METADATA_DB_URL=jdbc:postgresql://localhost:5432/screen_engine_metadata
export METADATA_DB_USERNAME=postgres
export METADATA_DB_PASSWORD=postgres
```

### 3. Run Database Migrations

Flyway will automatically run migrations on startup. To run manually:

```bash
mvn flyway:migrate
```

### 4. Build the Application

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run with a specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 6. Verify the Application

- Health Check: http://localhost:8080/api/v1/health
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Docs: http://localhost:8080/v3/api-docs

## Configuration

### Application Profiles

- **default**: Standard configuration
- **dev**: Development environment (debug logging, local database)
- **test**: Test environment (H2 in-memory database)
- **prod**: Production environment (optimized settings)

Run with profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `METADATA_DB_URL` | Metadata database JDBC URL | jdbc:postgresql://localhost:5432/screen_engine_metadata |
| `METADATA_DB_USERNAME` | Database username | postgres |
| `METADATA_DB_PASSWORD` | Database password | postgres |
| `DATA_DB_ENABLED` | Enable separate data database | false |
| `SQL_DIALECT` | SQL dialect (POSTGRESQL, ORACLE, MYSQL, SQLSERVER) | POSTGRESQL |
| `SERVER_PORT` | Server port | 8080 |
| `JWT_SECRET` | JWT secret key | (change in production) |
| `CORS_ORIGINS` | Allowed CORS origins | http://localhost:5173 |

### System Parameters

Configure system parameters in `application.yml`:

```yaml
screen-engine:
  parameters:
    factory: 101
    user: ADMIN
    person-id: 0
    org-code: DEFAULT
```

These parameters are used for substitution in WHERE conditions and default values.

## API Documentation

### Interactive Documentation

Access Swagger UI at: http://localhost:8080/swagger-ui.html

### API Contract

See `.claude/API_CONTRACT.md` for detailed API specification.

### Example API Calls

#### Get Screen Metadata

```bash
curl http://localhost:8080/api/v1/screens/CUSTOMER_LIST/metadata
```

#### Query Screen Data

```bash
curl -X POST http://localhost:8080/api/v1/screens/CUSTOMER_LIST/query \
  -H "Content-Type: application/json" \
  -d '{
    "parameters": {
      "CUSTOMER_CODE": "C%",
      "STATUS": "A"
    },
    "pagination": {
      "page": 1,
      "pageSize": 50
    }
  }'
```

## Development

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=HealthControllerTest

# Run integration tests
mvn verify
```

### Database Migrations

Migrations are in `src/main/resources/db/migration/`:

```
V1__create_metadata_tables.sql
V2__add_indexes.sql
V3__sample_data.sql
```

Add new migrations following Flyway naming convention.

### Code Style

Follow Google Java Style Guide. Key points:

- Use Lombok annotations to reduce boilerplate
- Use meaningful variable names
- Add Javadoc for public methods
- Keep methods short and focused
- Use builder pattern for complex objects

### Debugging

Enable debug logging in `application.yml`:

```yaml
logging:
  level:
    com.screenengine: DEBUG
    org.jooq: DEBUG
```

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│      Controllers (REST API)         │
├─────────────────────────────────────┤
│      Services (Business Logic)      │
├─────────────────────────────────────┤
│      Repositories (Data Access)     │
├─────────────────────────────────────┤
│      SQL Engine (Query Building)    │
├─────────────────────────────────────┤
│      Database (Metadata + Data)     │
└─────────────────────────────────────┘
```

### Key Components

- **Metadata Engine**: Reads and processes screen definitions
- **SQL Engine**: Builds dynamic queries with JOINs and parameters
- **Database Abstraction**: Supports multiple database dialects
- **Parameter Resolver**: Substitutes system parameters in queries

## Database Schema

The system uses three core metadata tables:

1. **T_KUL_EKRAN**: Screen definitions
2. **T_KUL_EKRAN_TABLO**: Table definitions within screens
3. **T_KUL_EKRAN_TABLO_KOLON**: Column definitions

See `.claude/DATABASE_SCHEMA.md` for complete schema documentation.

## Troubleshooting

### Application Won't Start

1. Check Java version: `java -version` (must be 17+)
2. Check database connection in `application.yml`
3. Ensure database is running
4. Check logs: `tail -f logs/spring-boot-logger.log`

### Database Connection Issues

1. Verify database URL format
2. Check username/password
3. Ensure database driver is in classpath
4. Test connection: `telnet localhost 5432`

### Maven Build Fails

1. Clean Maven cache: `mvn clean`
2. Update dependencies: `mvn dependency:resolve`
3. Check Java version in `pom.xml`

## Deployment

### Building for Production

```bash
mvn clean package -Pprod
```

This creates an executable JAR in `target/screen-engine-backend-0.1.0.jar`

### Running in Production

```bash
java -jar target/screen-engine-backend-0.1.0.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://prod-db:5432/screen_engine \
  --spring.datasource.username=app_user \
  --spring.datasource.password=secure_password
```

### Docker Deployment

(To be implemented)

```bash
docker build -t screen-engine-backend .
docker run -p 8080:8080 screen-engine-backend
```

## Contributing

1. Create a feature branch: `git checkout -b feature/my-feature`
2. Make changes and add tests
3. Update documentation if needed
4. Submit a pull request

## Roadmap

### Phase 1: Core Engine (Current)
- [x] Project structure and configuration
- [ ] Metadata model and repository
- [ ] Dynamic SQL query builder
- [ ] Basic CRUD API endpoints
- [ ] Oracle + PostgreSQL support

### Phase 2: Advanced Features
- [ ] Master-detail screens
- [ ] Advanced validation rules
- [ ] Excel import/export
- [ ] Audit trail

### Phase 3: Enterprise Features
- [ ] JWT authentication
- [ ] Multi-tenant SaaS mode
- [ ] Advanced RBAC
- [ ] API rate limiting

## License

[To be determined]

## Support

- **Issues**: GitHub Issues
- **Documentation**: See `.claude/` directory
- **API Contract**: `.claude/API_CONTRACT.md`
- **Database Schema**: `.claude/DATABASE_SCHEMA.md`

---

**Version**: 0.1.0
**Last Updated**: January 2025
**Status**: Active Development
