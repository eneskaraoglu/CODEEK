# API Contract - Screen Engine REST API

## Overview

This document defines the RESTful API contract between the Screen Engine frontend and backend. All endpoints follow REST principles and return JSON responses.

**Base URL**: `http://localhost:8080/api/v1`

**Authentication**: JWT Bearer Token (to be implemented)

**Content-Type**: `application/json`

## API Versioning

- Version is included in the URL path (`/api/v1/`)
- Breaking changes will increment the major version (`/api/v2/`)
- Backward-compatible changes will not change version

## Common Response Structure

### Success Response
```json
{
  "success": true,
  "data": { ... },
  "message": "Operation completed successfully"
}
```

### Error Response
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human-readable error message",
    "details": { ... }
  },
  "timestamp": "2025-01-19T10:30:00Z"
}
```

### Pagination Response
```json
{
  "success": true,
  "data": [ ... ],
  "pagination": {
    "page": 1,
    "pageSize": 50,
    "total": 150,
    "totalPages": 3
  }
}
```

## HTTP Status Codes

- `200 OK`: Successful GET, PUT, PATCH requests
- `201 Created`: Successful POST request creating a resource
- `204 No Content`: Successful DELETE request
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Missing or invalid authentication
- `403 Forbidden`: Authenticated but not authorized
- `404 Not Found`: Resource not found
- `422 Unprocessable Entity`: Validation errors
- `500 Internal Server Error`: Server error

---

## Endpoints

### 1. Metadata Endpoints

#### Get Screen Metadata

Retrieves complete metadata for a screen including tables and columns.

**Endpoint**: `GET /api/v1/screens/{screenCode}/metadata`

**Path Parameters**:
- `screenCode` (string, required): Screen identifier (e.g., "CUSTOMER_LIST")

**Query Parameters**:
- `includePermissions` (boolean, optional, default: true): Include user permissions

**Response**: `200 OK`
```json
{
  "success": true,
  "data": {
    "screenId": 100,
    "screenCode": "CUSTOMER_LIST",
    "screenName": "Customer List",
    "screenType": "T",
    "permissions": {
      "create": true,
      "read": true,
      "update": true,
      "delete": false
    },
    "queryPanel": {
      "layout": "GRID",
      "columns": 4
    },
    "tables": [
      {
        "tableId": 1001,
        "tableName": "Customers",
        "dbTable": "T_CUSTOMER",
        "tableType": "T",
        "primaryKey": "CUSTOMER_ID",
        "whereCondition": "FACTORY_CODE = :FACTORY",
        "order": 1,
        "columns": [
          {
            "columnId": 10001,
            "columnName": "Customer Code",
            "dbColumn": "CUSTOMER_CODE",
            "type": "STR",
            "length": 50,
            "scale": 0,
            "required": true,
            "hidden": false,
            "panel": 1,
            "panelOperator": "LIKE",
            "panelLogic": "AND",
            "order": 1,
            "tableWidth": 150,
            "defaultValue": null
          },
          {
            "columnId": 10002,
            "columnName": "Company",
            "dbColumn": "COMPANY_ID",
            "type": "JOIN",
            "length": 10,
            "required": false,
            "hidden": false,
            "panel": 1,
            "panelOperator": "=",
            "panelLogic": "AND",
            "order": 2,
            "tableWidth": 200,
            "joinConfig": {
              "joinTable": "ERP_COMPANY",
              "joinCodeColumn": "COMPANY_CODE",
              "joinNameColumn": "COMPANY_NAME",
              "joinKeyColumn": "COMPANY_ID",
              "joinType": "LEFT",
              "whereCondition": "FACTORY_CODE = :FACTORY AND ACTIVE = 1",
              "multiSelect": false
            }
          },
          {
            "columnId": 10003,
            "columnName": "Status",
            "dbColumn": "STATUS",
            "type": "COMBO",
            "length": 1,
            "required": true,
            "hidden": false,
            "panel": 1,
            "panelOperator": "=",
            "order": 3,
            "tableWidth": 100,
            "comboOptions": [
              { "code": "A", "name": "Active" },
              { "code": "P", "name": "Passive" },
              { "code": "S", "name": "Suspended" }
            ]
          },
          {
            "columnId": 10004,
            "columnName": "Created Date",
            "dbColumn": "CREATED_DATE",
            "type": "DATE",
            "required": false,
            "hidden": false,
            "panel": 1,
            "panelOperator": ">=",
            "order": 4,
            "tableWidth": 120,
            "defaultValue": ":SYSTEM_DATE"
          }
        ]
      }
    ]
  }
}
```

**Error Responses**:
- `404 Not Found`: Screen not found
```json
{
  "success": false,
  "error": {
    "code": "SCREEN_NOT_FOUND",
    "message": "Screen with code 'INVALID_CODE' not found"
  }
}
```

---

#### Get Available Screens

Lists all screens the user has access to.

**Endpoint**: `GET /api/v1/screens`

**Query Parameters**:
- `module` (string, optional): Filter by module (e.g., "GNL", "FNS")
- `search` (string, optional): Search in screen names

**Response**: `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "screenCode": "CUSTOMER_LIST",
      "screenName": "Customer List",
      "module": "GNL",
      "permissions": {
        "create": true,
        "read": true,
        "update": true,
        "delete": false
      }
    }
  ]
}
```

---

### 2. Data Query Endpoints

#### Query Screen Data

Executes a query with parameters and returns data.

**Endpoint**: `POST /api/v1/screens/{screenCode}/query`

**Path Parameters**:
- `screenCode` (string, required): Screen identifier

**Request Body**:
```json
{
  "parameters": {
    "CUSTOMER_CODE": "C%",
    "COMPANY_ID": 123,
    "STATUS": "A",
    "CREATED_DATE": "2024-01-01"
  },
  "pagination": {
    "page": 1,
    "pageSize": 50
  },
  "sorting": {
    "field": "CUSTOMER_CODE",
    "order": "ASC"
  },
  "filters": {
    "ACTIVE": true
  }
}
```

**Field Descriptions**:
- `parameters`: Query panel parameter values (column names as keys)
- `pagination.page`: Page number (1-based)
- `pagination.pageSize`: Records per page (max: 1000)
- `sorting.field`: Column to sort by (database column name)
- `sorting.order`: Sort direction ("ASC" or "DESC")
- `filters`: Additional filters (optional)

**Response**: `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "CUSTOMER_ID": 1,
      "CUSTOMER_CODE": "C001",
      "COMPANY_ID": 123,
      "COMPANY_NAME": "ABC Ltd.",
      "COMPANY_CODE": "ABC",
      "STATUS": "A",
      "STATUS_NAME": "Active",
      "CREATED_DATE": "2024-01-15",
      "FACTORY_CODE": 101
    },
    {
      "CUSTOMER_ID": 2,
      "CUSTOMER_CODE": "C002",
      "COMPANY_ID": 124,
      "COMPANY_NAME": "XYZ Corp.",
      "COMPANY_CODE": "XYZ",
      "STATUS": "A",
      "STATUS_NAME": "Active",
      "CREATED_DATE": "2024-01-16",
      "FACTORY_CODE": 101
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 50,
    "total": 2,
    "totalPages": 1
  },
  "executedQuery": "SELECT ... (for debugging)"
}
```

**Notes**:
- JOIN columns return both ID and display name (e.g., `COMPANY_ID` and `COMPANY_NAME`)
- COMBO columns return both code and name (e.g., `STATUS` and `STATUS_NAME`)
- `executedQuery` is only included in development mode

**Error Responses**:
- `400 Bad Request`: Invalid parameters
```json
{
  "success": false,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "Invalid date format for CREATED_DATE",
    "details": {
      "field": "CREATED_DATE",
      "value": "invalid-date",
      "expected": "YYYY-MM-DD"
    }
  }
}
```

---

### 3. CRUD Endpoints

#### Create Record

Creates a new record.

**Endpoint**: `POST /api/v1/screens/{screenCode}/records`

**Request Body**:
```json
{
  "data": {
    "CUSTOMER_CODE": "C003",
    "COMPANY_ID": 125,
    "STATUS": "A",
    "CREATED_DATE": "2024-01-19"
  }
}
```

**Response**: `201 Created`
```json
{
  "success": true,
  "data": {
    "CUSTOMER_ID": 3,
    "CUSTOMER_CODE": "C003",
    "COMPANY_ID": 125,
    "STATUS": "A",
    "CREATED_DATE": "2024-01-19",
    "FACTORY_CODE": 101,
    "RECORD_CREATE_USER": "admin",
    "RECORD_CREATE_DATE": "2024-01-19T10:30:00Z"
  },
  "message": "Record created successfully"
}
```

**Validation**:
- Required fields must be provided
- Data types must match column definitions
- Foreign keys (JOIN fields) must exist
- COMBO values must be in allowed options

**Error Responses**:
- `422 Unprocessable Entity`: Validation errors
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Validation failed",
    "details": {
      "fields": [
        {
          "field": "CUSTOMER_CODE",
          "message": "Customer code is required"
        },
        {
          "field": "COMPANY_ID",
          "message": "Company with ID 999 does not exist"
        }
      ]
    }
  }
}
```

---

#### Update Record

Updates an existing record.

**Endpoint**: `PUT /api/v1/screens/{screenCode}/records/{id}`

**Path Parameters**:
- `id`: Record primary key value

**Request Body**:
```json
{
  "data": {
    "CUSTOMER_CODE": "C003-UPDATED",
    "COMPANY_ID": 126,
    "STATUS": "S"
  }
}
```

**Response**: `200 OK`
```json
{
  "success": true,
  "data": {
    "CUSTOMER_ID": 3,
    "CUSTOMER_CODE": "C003-UPDATED",
    "COMPANY_ID": 126,
    "STATUS": "S",
    "RECORD_CHANGE_USER": "admin",
    "RECORD_CHANGE_DATE": "2024-01-19T11:00:00Z"
  },
  "message": "Record updated successfully"
}
```

**Error Responses**:
- `404 Not Found`: Record not found
- `403 Forbidden`: No update permission
- `422 Unprocessable Entity`: Validation errors

---

#### Delete Record

Deletes a record.

**Endpoint**: `DELETE /api/v1/screens/{screenCode}/records/{id}`

**Response**: `204 No Content`

**Error Responses**:
- `404 Not Found`: Record not found
- `403 Forbidden`: No delete permission
- `409 Conflict`: Record has dependencies

---

#### Get Single Record

Retrieves a single record with all JOIN values resolved.

**Endpoint**: `GET /api/v1/screens/{screenCode}/records/{id}`

**Response**: `200 OK`
```json
{
  "success": true,
  "data": {
    "CUSTOMER_ID": 3,
    "CUSTOMER_CODE": "C003",
    "COMPANY_ID": 126,
    "COMPANY_NAME": "New Company Ltd.",
    "COMPANY_CODE": "NEW",
    "STATUS": "A",
    "STATUS_NAME": "Active",
    "CREATED_DATE": "2024-01-19",
    "FACTORY_CODE": 101
  }
}
```

---

### 4. Lookup / Search Endpoints

#### Search Lookup Values

Searches lookup table for JOIN fields.

**Endpoint**: `GET /api/v1/lookup/{tableName}`

**Path Parameters**:
- `tableName`: Lookup table name (e.g., "ERP_COMPANY")

**Query Parameters**:
- `search` (string, optional): Search term
- `whereCondition` (string, optional): Additional WHERE clause
- `parameters` (object, optional): Parameter values for whereCondition
- `page` (number, optional, default: 1)
- `pageSize` (number, optional, default: 50, max: 100)

**Example Request**:
```
GET /api/v1/lookup/ERP_COMPANY?search=ABC&whereCondition=FACTORY_CODE = :FACTORY&parameters.FACTORY=101
```

**Response**: `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "id": 123,
      "code": "ABC",
      "name": "ABC Ltd.",
      "displayText": "ABC - ABC Ltd."
    },
    {
      "id": 130,
      "code": "ABC2",
      "name": "ABC Industries",
      "displayText": "ABC2 - ABC Industries"
    }
  ],
  "pagination": {
    "page": 1,
    "pageSize": 50,
    "total": 2,
    "totalPages": 1
  }
}
```

**Notes**:
- `displayText` is formatted as "{code} - {name}"
- Search is case-insensitive and searches both code and name
- `whereCondition` uses same parameter substitution as screen queries

---

### 5. Parameter Substitution

The following parameters are automatically substituted in WHERE conditions and default values:

| Parameter | Description | Example Value | Type |
|-----------|-------------|---------------|------|
| `:FACTORY` | Factory code | `101` | NUMBER |
| `:KULLANICI` | User ID | `'ADMIN'` | STRING (quoted) |
| `:KISIID` | Person ID | `12345` | NUMBER |
| `:SISTEMTARIHI` | System date | `'2024-01-19'` | STRING (quoted) |
| `:MUHORGID` | Accounting org ID | `1` | NUMBER |
| `:MUHORGDONEMID` | Accounting period ID | `202401` | NUMBER |
| `:BIRIMNO` | Department number | `10` | NUMBER |
| `:ORGKODU` | Organization code | `'ORG001'` | STRING (quoted) |

**Usage in Metadata**:
```json
{
  "whereCondition": "FACTORY_CODE = :FACTORY AND CREATED_BY = :KULLANICI",
  "defaultValue": ":SISTEMTARIHI"
}
```

**Resolved Query**:
```sql
WHERE FACTORY_CODE = 101 AND CREATED_BY = 'ADMIN'
DEFAULT '2024-01-19'
```

---

### 6. Column Type Reference

#### String (STR)
```json
{
  "type": "STR",
  "panelOperator": "LIKE",  // or "="
  "value": "ABC%"
}
```

#### Number (NUMBER, LONG, INT, BIG)
```json
{
  "type": "NUMBER",
  "panelOperator": "=",  // or ">", "<", ">=", "<=", "<>"
  "value": 123.45
}
```

#### Date (DATE)
```json
{
  "type": "DATE",
  "panelOperator": "=",  // or ">", "<", ">=", "<=", "BETWEEN"
  "value": "2024-01-19"  // ISO 8601 format (YYYY-MM-DD)
}
```

#### Boolean (BOOL)
```json
{
  "type": "BOOL",
  "value": true  // or false, 1, 0, "1", "0"
}
```

#### ComboBox (COMBO)
```json
{
  "type": "COMBO",
  "panelOperator": "=",  // or "IN"
  "value": "A",  // Single value
  "comboOptions": [
    { "code": "A", "name": "Active" },
    { "code": "P", "name": "Passive" }
  ]
}
```

#### Lookup/JOIN (JOIN)
```json
{
  "type": "JOIN",
  "panelOperator": "=",  // or "IN" for multi-select
  "value": 123,  // Single ID
  // or for multi-select:
  "value": "123,124,125",  // Comma-separated IDs
  "joinConfig": {
    "joinTable": "ERP_COMPANY",
    "multiSelect": false  // or true
  }
}
```

---

## OpenAPI Specification

Full OpenAPI 3.0 specification is available at:
- Development: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Authentication (To Be Implemented)

### Login
**Endpoint**: `POST /api/v1/auth/login`

**Request**:
```json
{
  "username": "admin",
  "password": "password"
}
```

**Response**: `200 OK`
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 3600,
    "user": {
      "username": "admin",
      "fullName": "Admin User",
      "roles": ["ADMIN", "USER"]
    }
  }
}
```

### Using JWT Token
All protected endpoints require:
```
Authorization: Bearer <token>
```

---

## Rate Limiting (Future)

- **Anonymous**: 100 requests/hour
- **Authenticated**: 1000 requests/hour
- **Premium**: 10000 requests/hour

Headers in response:
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1642599600
```

---

## Changelog

### Version 1.0.0 (Target)
- Initial API design
- Metadata endpoints
- CRUD operations
- Lookup/search
- Parameter substitution

### Future Versions
- v1.1.0: Batch operations
- v1.2.0: Export/Import (Excel, CSV)
- v1.3.0: Audit trail queries
- v2.0.0: GraphQL endpoint (breaking change)

---

**Last Updated**: January 2025  
**API Version**: 1.0.0  
**Status**: Design Phase
