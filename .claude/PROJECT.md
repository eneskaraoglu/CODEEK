# Screen Engine - Universal Dynamic Screen Platform

## Project Overview

Screen Engine is a universal, database-agnostic platform for creating dynamic data entry and query screens without writing code. It reads metadata from database tables and automatically generates modern web interfaces with full CRUD capabilities.

### Vision

Transform the legacy Java Swing-based dynamic screen system into a modern, cloud-ready web application that can work with any SQL database (Oracle, PostgreSQL, MySQL, SQL Server).

### Key Features

- **Metadata-Driven UI**: Screens are generated from database metadata
- **Database Agnostic**: Works with Oracle, PostgreSQL, MySQL, SQL Server
- **20+ Column Types**: String, Number, Date, Boolean, ComboBox, Lookup (JOIN), etc.
- **Dynamic SQL Generation**: Automatic query building with JOINs, WHERE clauses, parameters
- **Role-Based Access Control**: CRUD-level permissions
- **Multi-Tenant**: Factory/organization-based data isolation
- **Modern UI**: React-based responsive interface
- **RESTful API**: Clean separation between frontend and backend

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend (React)                      │
│  - Generic Screen Renderer                              │
│  - Metadata-driven UI Components                        │
│  - No business logic                                    │
└─────────────────────────────────────────────────────────┘
                           ↕ REST API
┌─────────────────────────────────────────────────────────┐
│                Backend (Spring Boot)                     │
│  ┌────────────────────────────────────────────────────┐ │
│  │          Metadata Engine Layer                     │ │
│  │  - Screen Definition Reader                        │ │
│  │  - Column Type Registry                            │ │
│  │  - Permission Resolver                             │ │
│  └────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────┐ │
│  │          SQL Engine Layer                          │ │
│  │  - Dynamic Query Builder                           │ │
│  │  - Parameter Substitution                          │ │
│  │  - JOIN Resolution                                 │ │
│  │  - WHERE Clause Builder                            │ │
│  └────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────┐ │
│  │       Database Abstraction Layer                   │ │
│  │  - Oracle Dialect                                  │ │
│  │  - PostgreSQL Dialect                              │ │
│  │  - MySQL Dialect                                   │ │
│  │  - SQL Server Dialect                              │ │
│  └────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.2+
- **Language**: Java 17+
- **SQL Builder**: JOOQ (database-agnostic query building)
- **Database Access**: Spring Data JDBC
- **Connection Pooling**: HikariCP
- **Security**: Spring Security with JWT
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18+
- **Language**: TypeScript 5+
- **UI Library**: Ant Design Pro Components
- **State Management**: TanStack Query (React Query)
- **Table Component**: TanStack Table
- **Form Handling**: React Hook Form + Zod
- **HTTP Client**: Axios
- **Build Tool**: Vite

### Database Support
- Oracle 11g+
- PostgreSQL 12+
- MySQL 8+
- SQL Server 2016+

## Project Structure

```
screen-engine/
├── .claude/                       # Claude Code documentation
│   ├── PROJECT.md                 # This file - project overview
│   ├── API_CONTRACT.md            # REST API specification
│   ├── DATABASE_SCHEMA.md         # Metadata database schema
│   └── ARCHITECTURE.md            # Detailed architecture docs
├── backend/                       # Spring Boot backend
│   ├── .claude/
│   │   └── BACKEND.md            # Backend-specific documentation
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/screenengine/
│   │   │   │       ├── config/           # Configuration classes
│   │   │   │       ├── controller/       # REST controllers
│   │   │   │       ├── service/          # Business logic
│   │   │   │       ├── repository/       # Database access
│   │   │   │       ├── model/            # Domain models
│   │   │   │       ├── dto/              # Data transfer objects
│   │   │   │       ├── mapper/           # DTO mappers
│   │   │   │       ├── sql/              # SQL engine
│   │   │   │       │   ├── builder/      # Query builders
│   │   │   │       │   ├── dialect/      # Database dialects
│   │   │   │       │   └── resolver/     # Parameter resolvers
│   │   │   │       └── security/         # Security & auth
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/         # Flyway migrations
│   │   └── test/
│   ├── pom.xml
│   └── README.md
├── frontend/                      # React frontend
│   ├── .claude/
│   │   └── FRONTEND.md           # Frontend-specific documentation
│   ├── src/
│   │   ├── components/
│   │   │   ├── DynamicScreen/    # Main screen components
│   │   │   ├── fields/           # Field type components
│   │   │   └── common/           # Shared components
│   │   ├── services/             # API services
│   │   ├── hooks/                # Custom React hooks
│   │   ├── types/                # TypeScript types
│   │   ├── utils/                # Utility functions
│   │   └── App.tsx
│   ├── package.json
│   ├── tsconfig.json
│   ├── vite.config.ts
│   └── README.md
├── docs/                          # Additional documentation
│   ├── deployment.md
│   ├── development.md
│   └── examples/
├── docker-compose.yml             # Local development setup
├── .gitignore
└── README.md
```

## Key Documents

### Essential Reading (Read First)
1. **This File** (PROJECT.md) - Project overview and navigation
2. **API_CONTRACT.md** - REST API specification (OpenAPI/Swagger format)
3. **DATABASE_SCHEMA.md** - Metadata tables structure and column types

### Implementation Guides
4. **backend/.claude/BACKEND.md** - Backend implementation details
5. **frontend/.claude/FRONTEND.md** - Frontend implementation details
6. **ARCHITECTURE.md** - Detailed architecture and design decisions

## Core Concepts

### Metadata Tables

The system uses three core metadata tables:

1. **T_KUL_EKRAN** (Screen Definitions)
   - Defines screens and their properties
   - CRUD permissions, layout settings

2. **T_KUL_EKRAN_TABLO** (Table Definitions)
   - Defines tables within screens
   - Links to database tables, WHERE conditions

3. **T_KUL_EKRAN_TABLO_KOLON** (Column Definitions)
   - Defines columns and their properties
   - Column types, JOIN configurations, validators

See DATABASE_SCHEMA.md for detailed schema.

### Column Types

- **STR**: String/text field
- **LONG**: Long integer
- **INT**: Integer
- **NUMBER**: Decimal number
- **BIG**: BigDecimal (high precision)
- **DATE**: Date/timestamp
- **BOOL**: Boolean/checkbox
- **COMBO**: Dropdown with predefined options
- **JOIN**: Lookup field with search dialog (foreign key)
- **PK/ID**: Primary key (hidden, auto-generated)

### Dynamic SQL Engine

The SQL engine:
1. Reads metadata for a screen
2. Builds SELECT query with:
   - Column projections
   - LEFT JOINs for lookup fields
   - WHERE clause from table and user parameters
   - Parameter substitution (:FABRIKA, :KULLANICI, etc.)
3. Executes query with database-specific dialect
4. Returns results as JSON

## Claude Code Workflow

### Initial Setup
When you first open this project:
```bash
# Read these documents in order:
1. .claude/PROJECT.md (this file)
2. .claude/API_CONTRACT.md
3. .claude/DATABASE_SCHEMA.md
```

### Working on Backend
```bash
cd backend
# Read: backend/.claude/BACKEND.md
# This contains Spring Boot specifics, package structure, coding patterns
```

### Working on Frontend
```bash
cd frontend
# Read: frontend/.claude/FRONTEND.md
# This contains React patterns, component structure, state management
```

### Adding New Features

#### New Column Type
1. Read: `.claude/DATABASE_SCHEMA.md` (understand column types)
2. Backend: Update `backend/.claude/BACKEND.md` section on column types
3. Frontend: Update `frontend/.claude/FRONTEND.md` section on field components
4. Update: `.claude/API_CONTRACT.md` if API changes

#### New API Endpoint
1. Read: `.claude/API_CONTRACT.md`
2. Define contract FIRST (request/response format)
3. Backend: Implement in controller + service
4. Frontend: Implement in service + hook
5. Update both BACKEND.md and FRONTEND.md

#### Database Dialect Support
1. Read: `.claude/ARCHITECTURE.md` (dialect pattern)
2. Backend: Implement new dialect class
3. Test: Add integration test with that database
4. Update: `backend/.claude/BACKEND.md`

## Development Environment Setup

### Prerequisites
- Java 17+
- Node.js 18+
- Docker & Docker Compose (for databases)
- Maven 3.8+
- Git

### Quick Start
```bash
# 1. Clone repository
git clone <repo-url>
cd screen-engine

# 2. Start databases (Oracle, PostgreSQL)
docker-compose up -d

# 3. Start backend
cd backend
mvn spring-boot:run

# 4. Start frontend (in another terminal)
cd frontend
npm install
npm run dev

# 5. Access application
# Frontend: http://localhost:5173
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

See `docs/development.md` for detailed setup instructions.

## Testing Strategy

### Backend
- **Unit Tests**: Service layer logic
- **Integration Tests**: Database queries with Testcontainers
- **API Tests**: REST endpoint contracts

### Frontend
- **Unit Tests**: Component logic with Vitest
- **Integration Tests**: User flows with React Testing Library
- **E2E Tests**: Full scenarios with Playwright (future)

## Deployment

See `docs/deployment.md` for:
- Docker containerization
- Kubernetes deployment
- Cloud deployment (AWS, Azure, GCP)
- Database migration strategies

## Contributing Guidelines

### Code Style
- **Backend**: Follow Google Java Style Guide
- **Frontend**: ESLint + Prettier configuration
- **Commits**: Conventional Commits format

### Branch Strategy
- `main`: Production-ready code
- `develop`: Integration branch
- `feature/*`: Feature branches
- `bugfix/*`: Bug fix branches

### Pull Request Process
1. Create feature branch from `develop`
2. Update relevant .claude/*.md files
3. Write tests
4. Update API_CONTRACT.md if API changes
5. Submit PR with description

## Versioning

We use Semantic Versioning (SemVer):
- **Major**: Breaking API changes
- **Minor**: New features, backward compatible
- **Patch**: Bug fixes

Current Version: 0.1.0 (initial development)

## License

[To be determined]

## Support & Contact

- **Issues**: GitHub Issues
- **Discussions**: GitHub Discussions
- **Documentation**: This repository

## Roadmap

### Phase 1: Core Engine (Current)
- [x] Project structure and documentation
- [ ] Metadata model and repository
- [ ] Dynamic SQL query builder
- [ ] Basic CRUD API endpoints
- [ ] React screen renderer
- [ ] Oracle + PostgreSQL support

### Phase 2: Advanced Features
- [ ] Master-detail screens
- [ ] Tree table support
- [ ] Advanced validation rules
- [ ] Excel import/export
- [ ] Audit trail

### Phase 3: Enterprise Features
- [ ] Multi-tenant SaaS mode
- [ ] Advanced RBAC
- [ ] Workflow integration
- [ ] Report builder
- [ ] API rate limiting

### Phase 4: Ecosystem
- [ ] Screen designer UI
- [ ] Plugin system
- [ ] Marketplace for screen templates
- [ ] Mobile app (React Native)

## Known Limitations

1. **Complex Joins**: Currently supports LEFT JOIN only (no subqueries)
2. **Validation**: Basic validation only (no complex cross-field rules yet)
3. **Performance**: Large datasets (>10k rows) may need pagination optimization
4. **Caching**: No metadata caching yet (reads from DB each time)

## FAQ

**Q: Can I use this with my existing ERP database?**
A: Yes! Create metadata tables in your database and define screens for your existing tables.

**Q: Does it support stored procedures?**
A: Not yet. Currently supports dynamic SQL only. Planned for Phase 2.

**Q: Can I customize the UI?**
A: Yes. Frontend is React-based, you can override components and styling.

**Q: Is it production-ready?**
A: Not yet. Currently in active development (v0.1.0). Target: v1.0.0 by Q3 2025.

---

**Last Updated**: January 2025  
**Version**: 0.1.0  
**Status**: Active Development
