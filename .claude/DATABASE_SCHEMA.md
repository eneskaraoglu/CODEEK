# Database Schema - Screen Engine Metadata

## Overview

Screen Engine uses metadata tables to define dynamic screens. These tables store screen definitions, table configurations, and column properties. The system supports multiple databases (Oracle, PostgreSQL, MySQL, SQL Server) through a database-agnostic approach.

## Metadata Database vs Data Database

The system can be configured with:
- **Same Database**: Metadata and data tables in the same database
- **Separate Databases**: Metadata in one database, data tables in another

This is configured in `application.yml`:
```yaml
screen-engine:
  datasources:
    metadata:
      jdbc-url: jdbc:oracle:thin:@localhost:1521:ORCL
    data:
      jdbc-url: jdbc:postgresql://localhost:5432/erp_data
```

---

## Core Metadata Tables

### 1. T_KUL_EKRAN (Screen Definitions)

Stores screen-level metadata.

#### Table Structure (Oracle)

```sql
CREATE TABLE T_KUL_EKRAN (
    EKRAN_ID            NUMBER(22,5)        NOT NULL,
    EKRAN_KOD           VARCHAR2(50)        NOT NULL,
    EKRAN_AD            VARCHAR2(200)       NOT NULL,
    EKRAN_TIP           VARCHAR2(10)        NOT NULL,
    ALTSISTEM           VARCHAR2(10),
    FABRIKA_KOD         NUMBER(22,5),
    ETKGST              NUMBER(1)           DEFAULT 1,
    OZELLIK_1           VARCHAR2(100),
    OZELLIK_2           VARCHAR2(100),
    OZELLIK_3           VARCHAR2(100),
    OZELLIK_4           VARCHAR2(100),
    OZELLIK_5           VARCHAR2(100),
    OZELLIK_6           VARCHAR2(100),
    OZELLIK_7           VARCHAR2(100),
    OZELLIK_8           VARCHAR2(100),
    OZELLIK_9           VARCHAR2(100),
    CONSTRAINT T_KUL_EKRAN_PK PRIMARY KEY (EKRAN_ID),
    CONSTRAINT T_KUL_EKRAN_UK UNIQUE (EKRAN_KOD)
);

CREATE INDEX T_KUL_EKRAN_IDX1 ON T_KUL_EKRAN(EKRAN_KOD);
CREATE INDEX T_KUL_EKRAN_IDX2 ON T_KUL_EKRAN(ALTSISTEM);
```

#### Column Descriptions

| Column | Type | Required | Description |
|--------|------|----------|-------------|
| `EKRAN_ID` | NUMBER(22,5) | Yes | Primary key, unique screen identifier |
| `EKRAN_KOD` | VARCHAR2(50) | Yes | Screen code (e.g., "CUSTOMER_LIST"), unique |
| `EKRAN_AD` | VARCHAR2(200) | Yes | Screen display name (e.g., "Customer List") |
| `EKRAN_TIP` | VARCHAR2(10) | Yes | Screen type: "T" = Table, "MS" = Master-Slave |
| `ALTSISTEM` | VARCHAR2(10) | No | Module code (e.g., "GNL" = General, "FNS" = Finance) |
| `FABRIKA_KOD` | NUMBER(22,5) | No | Factory code for multi-tenant isolation |
| `ETKGST` | NUMBER(1) | No | Active flag: 1 = Active, 0 = Inactive (default: 1) |
| `OZELLIK_1` | VARCHAR2(100) | No | CRUD permissions (e.g., "CRUD", "RU", "R") |
| `OZELLIK_2` | VARCHAR2(100) | No | Query panel layout: "0" = GRID, "1" = FLOW, "2" = VERTICAL, "3" = GROUP |
| `OZELLIK_3` | VARCHAR2(100) | No | Query panel columns (1-12, default: 4) |
| `OZELLIK_4-9` | VARCHAR2(100) | No | Reserved for future use |

#### Sample Data

```sql
INSERT INTO T_KUL_EKRAN (
    EKRAN_ID, EKRAN_KOD, EKRAN_AD, EKRAN_TIP, ALTSISTEM,
    FABRIKA_KOD, ETKGST, OZELLIK_1, OZELLIK_2, OZELLIK_3
) VALUES (
    1, 'CUSTOMER_LIST', 'Customer List', 'T', 'GNL',
    101, 1, 'CRUD', '0', '4'
);
```

---

### 2. T_KUL_EKRAN_TABLO (Table Definitions)

Stores table-level metadata within screens.

#### Table Structure (Oracle)

```sql
CREATE TABLE T_KUL_EKRAN_TABLO (
    TABLO_ID            NUMBER(22,5)        NOT NULL,
    EKRAN_ID            NUMBER(22,5)        NOT NULL,
    TABLO_AD            VARCHAR2(100)       NOT NULL,
    DB_TABLO            VARCHAR2(100)       NOT NULL,
    TABLO_ID_KOLON      VARCHAR2(100),
    TIPI                VARCHAR2(10)        DEFAULT 'C',
    SIRALAMA            NUMBER(5)           DEFAULT 1,
    WHERE_KOSUL         VARCHAR2(4000),
    OZELLIK_SORGU1      VARCHAR2(200),
    OZELLIK_SORGU2      VARCHAR2(200),
    CONSTRAINT T_KUL_EKRAN_TABLO_PK PRIMARY KEY (TABLO_ID),
    CONSTRAINT T_KUL_EKRAN_TABLO_FK1 FOREIGN KEY (EKRAN_ID) 
        REFERENCES T_KUL_EKRAN(EKRAN_ID) ON DELETE CASCADE
);

CREATE INDEX T_KUL_EKRAN_TABLO_IDX1 ON T_KUL_EKRAN_TABLO(EKRAN_ID);
CREATE INDEX T_KUL_EKRAN_TABLO_IDX2 ON T_KUL_EKRAN_TABLO(DB_TABLO);
```

#### Column Descriptions

| Column | Type | Required | Description |
|--------|------|----------|-------------|
| `TABLO_ID` | NUMBER(22,5) | Yes | Primary key, unique table identifier |
| `EKRAN_ID` | NUMBER(22,5) | Yes | Foreign key to T_KUL_EKRAN |
| `TABLO_AD` | VARCHAR2(100) | Yes | Table display name (e.g., "Customers") |
| `DB_TABLO` | VARCHAR2(100) | Yes | Database table name (e.g., "T_CUSTOMER") |
| `TABLO_ID_KOLON` | VARCHAR2(100) | No | Primary key column name (e.g., "CUSTOMER_ID") |
| `TIPI` | VARCHAR2(10) | No | Table type: "T" = Standard, "C" = Complex, "TR" = Tree, "MS" = Master, "SM" = Slave |
| `SIRALAMA` | NUMBER(5) | No | Sort order for multiple tables (default: 1) |
| `WHERE_KOSUL` | VARCHAR2(4000) | No | Static WHERE clause with parameters (e.g., "FACTORY_CODE = :FACTORY") |
| `OZELLIK_SORGU1` | VARCHAR2(200) | No | Reserved (previously column count) |
| `OZELLIK_SORGU2` | VARCHAR2(200) | No | Reserved (previously layout type) |

#### Sample Data

```sql
INSERT INTO T_KUL_EKRAN_TABLO (
    TABLO_ID, EKRAN_ID, TABLO_AD, DB_TABLO, TABLO_ID_KOLON,
    TIPI, SIRALAMA, WHERE_KOSUL
) VALUES (
    1001, 1, 'Customers', 'T_CUSTOMER', 'CUSTOMER_ID',
    'T', 1, 'FACTORY_CODE = :FACTORY AND ACTIVE = 1'
);
```

---

### 3. T_KUL_EKRAN_TABLO_KOLON (Column Definitions)

Stores column-level metadata.

#### Table Structure (Oracle)

```sql
CREATE TABLE T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID            NUMBER(22,5)        NOT NULL,
    TABLO_ID            NUMBER(22,5)        NOT NULL,
    KOLON_AD            VARCHAR2(100)       NOT NULL,
    DB_KOLON            VARCHAR2(100)       NOT NULL,
    TIPI                VARCHAR2(20)        NOT NULL,
    UZUNLUK             NUMBER(5)           DEFAULT 0,
    UZUNLUK_SKALA       NUMBER(5)           DEFAULT 0,
    TABLO_GENISLIK      NUMBER(5)           DEFAULT 100,
    ZORUNLU             NUMBER(1)           DEFAULT 0,
    GIZLI               NUMBER(1)           DEFAULT 0,
    PANEL               NUMBER(1)           DEFAULT 0,
    SIRALAMA            NUMBER(5)           DEFAULT 1,
    KODLU_ALAN          VARCHAR2(500),
    STANDART_DEGER      VARCHAR2(500),
    JOIN_TABLO          VARCHAR2(100),
    JOIN_TABLO_DB_KOD   VARCHAR2(100),
    JOIN_TABLO_DB_AD    VARCHAR2(100),
    JOIN_DB_KOLON       VARCHAR2(100),
    JOIN_TIPI           VARCHAR2(20),
    OZELLIK_PANEL1      VARCHAR2(100),
    OZELLIK_PANEL2      VARCHAR2(100),
    OZELLIK_PANEL3      VARCHAR2(100),
    OZELLIK_KOLON1      VARCHAR2(100),
    OZELLIK_KOLON2      VARCHAR2(4000),
    CONSTRAINT T_KUL_EKRAN_TABLO_KOLON_PK PRIMARY KEY (KOLON_ID),
    CONSTRAINT T_KUL_EKRAN_TABLO_KOLON_FK1 FOREIGN KEY (TABLO_ID)
        REFERENCES T_KUL_EKRAN_TABLO(TABLO_ID) ON DELETE CASCADE
);

CREATE INDEX T_KUL_EKRAN_TABLO_KOLON_IDX1 ON T_KUL_EKRAN_TABLO_KOLON(TABLO_ID);
CREATE INDEX T_KUL_EKRAN_TABLO_KOLON_IDX2 ON T_KUL_EKRAN_TABLO_KOLON(DB_KOLON);
```

#### Column Descriptions

| Column | Type | Required | Description |
|--------|------|----------|-------------|
| `KOLON_ID` | NUMBER(22,5) | Yes | Primary key, unique column identifier |
| `TABLO_ID` | NUMBER(22,5) | Yes | Foreign key to T_KUL_EKRAN_TABLO |
| `KOLON_AD` | VARCHAR2(100) | Yes | Column display name (e.g., "Customer Code") |
| `DB_KOLON` | VARCHAR2(100) | Yes | Database column name (e.g., "CUSTOMER_CODE") |
| `TIPI` | VARCHAR2(20) | Yes | Column type (see Column Types section) |
| `UZUNLUK` | NUMBER(5) | No | Field length (for STR, NUMBER types) |
| `UZUNLUK_SKALA` | NUMBER(5) | No | Decimal scale (for NUMBER, BIG types) |
| `TABLO_GENISLIK` | NUMBER(5) | No | Table column width in pixels (default: 100) |
| `ZORUNLU` | NUMBER(1) | No | Required flag: 1 = Required, 0 = Optional |
| `GIZLI` | NUMBER(1) | No | Hidden flag: 1 = Hidden, 0 = Visible |
| `PANEL` | NUMBER(1) | No | Query panel: 0 = Table only, 1 = Both, 2 = Panel only, 3 = Hidden |
| `SIRALAMA` | NUMBER(5) | No | Sort order (for display) |
| `KODLU_ALAN` | VARCHAR2(500) | No | ComboBox options (format: "code1=Name1;code2=Name2;;") |
| `STANDART_DEGER` | VARCHAR2(500) | No | Default value or parameter (e.g., ":FACTORY") |
| `JOIN_TABLO` | VARCHAR2(100) | No | JOIN table name (for JOIN type) |
| `JOIN_TABLO_DB_KOD` | VARCHAR2(100) | No | JOIN code column |
| `JOIN_TABLO_DB_AD` | VARCHAR2(100) | No | JOIN display column |
| `JOIN_DB_KOLON` | VARCHAR2(100) | No | JOIN primary key column |
| `JOIN_TIPI` | VARCHAR2(20) | No | JOIN type: "LEFT", "INNER" |
| `OZELLIK_PANEL1` | VARCHAR2(100) | No | Query logic: "AND", "OR" |
| `OZELLIK_PANEL2` | VARCHAR2(100) | No | Query operator: "=", "LIKE", "<", ">", "IN" |
| `OZELLIK_PANEL3` | VARCHAR2(100) | No | Auto-query flag: "1" = Query after selection |
| `OZELLIK_KOLON1` | VARCHAR2(100) | No | Default value expression |
| `OZELLIK_KOLON2` | VARCHAR2(4000) | No | JOIN WHERE condition |

---

## Column Types

### Supported Types

| Type | Description | Java Type | SQL Type | UI Component | Example |
|------|-------------|-----------|----------|--------------|---------|
| `PK` | Primary key | Long | NUMBER(10,0) | Hidden | Auto-generated ID |
| `ID` | Primary key (alias) | Long | NUMBER(10,0) | Hidden | Auto-generated ID |
| `STR` | String/text | String | VARCHAR2(n) | Text field | "ABC123" |
| `LONG` | Long integer | Long | NUMBER(10,0) | Number field | 1234567890 |
| `INT` | Integer | Integer | NUMBER(10,0) | Number field | 12345 |
| `NUMBER` | Decimal number | BigDecimal | NUMBER(p,s) | Number field | 123.45 |
| `BIG` | High precision decimal | BigDecimal | NUMBER(18,4) | Currency field | 1234.5678 |
| `DATE` | Date/timestamp | Date | DATE | Date picker | 2024-01-19 |
| `BOOL` | Boolean | Boolean | NUMBER(1,0) | Checkbox | 1 or 0 |
| `COMBO` | Dropdown | String | VARCHAR2(n) | ComboBox | "A" (from options) |
| `JOIN` | Lookup/foreign key | Long | NUMBER(10,0) | Search dialog | 123 (ID) |
| `JOIN-KOLON` | Legacy lookup | Long | NUMBER(10,0) | Search dialog | 123 (ID) |
| `TEXT` | Multi-line text | String | VARCHAR2(4000) | Text area | Long description |

### Type Details

#### STRING (STR)
```sql
-- Column definition
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    UZUNLUK, PANEL, OZELLIK_PANEL1, OZELLIK_PANEL2
) VALUES (
    10001, 1001, 'Customer Code', 'CUSTOMER_CODE', 'STR',
    50, 1, 'AND', 'LIKE'
);

-- Generated WHERE clause
-- UPPER(A.CUSTOMER_CODE) LIKE UPPER('ABC%')
```

#### NUMBER (NUMBER, BIG, LONG, INT)
```sql
-- Column definition
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    UZUNLUK, UZUNLUK_SKALA, PANEL, OZELLIK_PANEL2
) VALUES (
    10002, 1001, 'Amount', 'AMOUNT', 'BIG',
    18, 4, 1, '>='
);

-- Generated WHERE clause
-- A.AMOUNT >= 1000.50
```

#### DATE
```sql
-- Column definition
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    PANEL, OZELLIK_PANEL2, OZELLIK_KOLON1
) VALUES (
    10003, 1001, 'Created Date', 'CREATED_DATE', 'DATE',
    1, '>=', ':SISTEMTARIHI'
);

-- Generated WHERE clause (Oracle)
-- TRUNC(A.CREATED_DATE) >= TO_DATE('2024-01-19', 'YYYY-MM-DD')

-- Generated WHERE clause (PostgreSQL)
-- DATE_TRUNC('day', A.CREATED_DATE) >= DATE '2024-01-19'
```

#### BOOLEAN (BOOL)
```sql
-- Column definition
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    PANEL, OZELLIK_KOLON1, OZELLIK_PANEL3
) VALUES (
    10004, 1001, 'Active', 'ACTIVE', 'BOOL',
    1, '1', '1'
);

-- Generated WHERE clause
-- A.ACTIVE = 1

-- OZELLIK_KOLON1: Default value (1 = checked, 0 = unchecked)
-- OZELLIK_PANEL3: Default state in query panel
```

#### COMBOBOX (COMBO)
```sql
-- Column definition
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    KODLU_ALAN, PANEL, OZELLIK_PANEL2
) VALUES (
    10005, 1001, 'Status', 'STATUS', 'COMBO',
    'A=Active;P=Passive;S=Suspended;;', 1, '='
);

-- KODLU_ALAN format: "code1=Name1;code2=Name2;;"
-- Ending with ";;" adds empty option for clearing selection

-- Generated WHERE clause
-- A.STATUS = 'A'
```

#### LOOKUP/JOIN (JOIN)
```sql
-- Column definition
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    JOIN_TABLO, JOIN_TABLO_DB_KOD, JOIN_TABLO_DB_AD,
    JOIN_DB_KOLON, JOIN_TIPI, OZELLIK_KOLON2,
    PANEL, OZELLIK_PANEL2
) VALUES (
    10006, 1001, 'Company', 'COMPANY_ID', 'JOIN',
    'ERP_COMPANY', 'COMPANY_CODE', 'COMPANY_NAME',
    'COMPANY_ID', 'LEFT', 'FACTORY_CODE = :FACTORY AND ACTIVE = 1',
    1, '='
);

-- Generated SELECT with JOIN
-- SELECT A.COMPANY_ID, J10006.COMPANY_NAME AS COMPANY_ID_NAME
-- FROM T_CUSTOMER A
-- LEFT JOIN ERP_COMPANY J10006 
--   ON A.COMPANY_ID = J10006.COMPANY_ID
--   AND J10006.FACTORY_CODE = 101
--   AND J10006.ACTIVE = 1

-- Generated WHERE clause
-- A.COMPANY_ID = 123

-- For multi-select (OZELLIK_PANEL2 = 'IN')
-- A.COMPANY_ID IN (123, 124, 125)
```

---

## Parameter Substitution

Parameters are replaced in WHERE conditions and default values.

### Available Parameters

| Parameter | Description | Value Type | Example |
|-----------|-------------|------------|---------|
| `:FACTORY` | Factory code | NUMBER | 101 |
| `:FABRIKA` | Factory code (alias) | NUMBER | 101 |
| `:KULLANICI` | User ID | STRING | 'ADMIN' |
| `:KISIID` | Person ID | NUMBER | 12345 |
| `:SISTEMTARIHI` | System date | STRING | '2024-01-19' |
| `:MUHORGID` | Accounting org ID | NUMBER | 1 |
| `:MUHORGDONEMID` | Accounting period ID | NUMBER | 202401 |
| `:BIRIMNO` | Department number | NUMBER | 10 |
| `:ORGKODU` | Organization code | STRING | 'ORG001' |

### Usage Examples

#### WHERE Condition
```sql
WHERE_KOSUL = 'FACTORY_CODE = :FACTORY AND CREATED_BY = :KULLANICI'

-- Resolved to:
-- WHERE FACTORY_CODE = 101 AND CREATED_BY = 'ADMIN'
```

#### Default Value
```sql
STANDART_DEGER = ':FACTORY'
OZELLIK_KOLON1 = ':SISTEMTARIHI'

-- Resolved to:
-- DEFAULT 101
-- DEFAULT '2024-01-19'
```

#### JOIN WHERE Condition
```sql
OZELLIK_KOLON2 = 'FACTORY_CODE = :FACTORY AND ORG_CODE = :ORGKODU'

-- Resolved in JOIN:
-- LEFT JOIN ERP_COMPANY J10006
--   ON A.COMPANY_ID = J10006.COMPANY_ID
--   AND J10006.FACTORY_CODE = 101
--   AND J10006.ORG_CODE = 'ORG001'
```

---

## Sequences (Auto-Increment)

Each metadata table needs a sequence for auto-generating IDs.

### Oracle
```sql
CREATE SEQUENCE SEQ_KUL_EKRAN START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_KUL_EKRAN_TABLO START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_KUL_EKRAN_TABLO_KOLON START WITH 1 INCREMENT BY 1;

-- Trigger example
CREATE OR REPLACE TRIGGER TRG_KUL_EKRAN_ID
BEFORE INSERT ON T_KUL_EKRAN
FOR EACH ROW
BEGIN
    IF :NEW.EKRAN_ID IS NULL THEN
        SELECT SEQ_KUL_EKRAN.NEXTVAL INTO :NEW.EKRAN_ID FROM DUAL;
    END IF;
END;
/
```

### PostgreSQL
```sql
CREATE SEQUENCE seq_kul_ekran START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_kul_ekran_tablo START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_kul_ekran_tablo_kolon START WITH 1 INCREMENT BY 1;

-- Or use SERIAL
CREATE TABLE t_kul_ekran (
    ekran_id SERIAL PRIMARY KEY,
    ...
);
```

---

## Permissions (T_KUL_EKRAN_YETKI)

Optional table for role-based access control.

```sql
CREATE TABLE T_KUL_EKRAN_YETKI (
    EKRAN_ID        NUMBER(22,5)    NOT NULL,
    ROL             VARCHAR2(50)    NOT NULL,
    SERVIS          VARCHAR2(200),
    FABRIKA_KOD     NUMBER(22,5),
    CONSTRAINT T_KUL_EKRAN_YETKI_FK1 FOREIGN KEY (EKRAN_ID)
        REFERENCES T_KUL_EKRAN(EKRAN_ID) ON DELETE CASCADE
);

-- Sample data
INSERT INTO T_KUL_EKRAN_YETKI (EKRAN_ID, ROL, SERVIS, FABRIKA_KOD)
VALUES (1, 'SALES_MANAGER', 'setKulOzelEkranKullaniciDegerYeni', 101);
```

---

## Database Migration Scripts

Migration scripts are in `backend/src/main/resources/db/migration/`

### Flyway Naming Convention
```
V1__create_metadata_tables.sql
V2__add_indexes.sql
V3__sample_data.sql
```

### PostgreSQL Conversion
When converting from Oracle to PostgreSQL:
- `NUMBER(22,5)` → `NUMERIC(22,5)` or `BIGINT`
- `VARCHAR2(n)` → `VARCHAR(n)`
- `SEQUENCE.NEXTVAL` → `NEXTVAL('sequence')`
- Remove `FROM DUAL`

---

## Complete Example: Customer Screen

```sql
-- 1. Screen definition
INSERT INTO T_KUL_EKRAN (
    EKRAN_ID, EKRAN_KOD, EKRAN_AD, EKRAN_TIP, ALTSISTEM,
    FABRIKA_KOD, ETKGST, OZELLIK_1, OZELLIK_2, OZELLIK_3
) VALUES (
    1, 'CUSTOMER_LIST', 'Customer List', 'T', 'GNL',
    101, 1, 'CRUD', '0', '4'
);

-- 2. Table definition
INSERT INTO T_KUL_EKRAN_TABLO (
    TABLO_ID, EKRAN_ID, TABLO_AD, DB_TABLO, TABLO_ID_KOLON,
    TIPI, SIRALAMA, WHERE_KOSUL
) VALUES (
    1001, 1, 'Customers', 'T_CUSTOMER', 'CUSTOMER_ID',
    'T', 1, 'FACTORY_CODE = :FACTORY'
);

-- 3. Column: Primary Key
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    ZORUNLU, GIZLI, PANEL, SIRALAMA
) VALUES (
    10001, 1001, 'ID', 'CUSTOMER_ID', 'PK',
    1, 1, 0, 1
);

-- 4. Column: Customer Code (String, searchable)
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    UZUNLUK, TABLO_GENISLIK, ZORUNLU, PANEL,
    OZELLIK_PANEL1, OZELLIK_PANEL2, SIRALAMA
) VALUES (
    10002, 1001, 'Customer Code', 'CUSTOMER_CODE', 'STR',
    50, 150, 1, 1,
    'AND', 'LIKE', 2
);

-- 5. Column: Company (JOIN/Lookup)
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    TABLO_GENISLIK, PANEL, SIRALAMA,
    JOIN_TABLO, JOIN_TABLO_DB_KOD, JOIN_TABLO_DB_AD,
    JOIN_DB_KOLON, JOIN_TIPI, OZELLIK_KOLON2,
    OZELLIK_PANEL1, OZELLIK_PANEL2
) VALUES (
    10003, 1001, 'Company', 'COMPANY_ID', 'JOIN',
    200, 1, 3,
    'ERP_COMPANY', 'COMPANY_CODE', 'COMPANY_NAME',
    'COMPANY_ID', 'LEFT', 'FACTORY_CODE = :FACTORY',
    'AND', '='
);

-- 6. Column: Status (ComboBox)
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    KODLU_ALAN, TABLO_GENISLIK, PANEL, SIRALAMA,
    OZELLIK_PANEL1, OZELLIK_PANEL2
) VALUES (
    10004, 1001, 'Status', 'STATUS', 'COMBO',
    'A=Active;P=Passive;S=Suspended;;', 100, 1, 4,
    'AND', '='
);

-- 7. Column: Created Date
INSERT INTO T_KUL_EKRAN_TABLO_KOLON (
    KOLON_ID, TABLO_ID, KOLON_AD, DB_KOLON, TIPI,
    TABLO_GENISLIK, PANEL, SIRALAMA,
    OZELLIK_KOLON1, OZELLIK_PANEL1, OZELLIK_PANEL2
) VALUES (
    10005, 1001, 'Created Date', 'CREATED_DATE', 'DATE',
    120, 1, 5,
    ':SISTEMTARIHI', 'AND', '>='
);
```

---

**Last Updated**: January 2025  
**Schema Version**: 1.0.0  
**Status**: Initial Design
