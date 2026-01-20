-- Screen Engine Metadata Tables - PostgreSQL
-- Version: 1.0.0
-- Description: Creates core metadata tables for screen definitions

-- =============================================================================
-- 1. T_KUL_EKRAN (Screen Definitions)
-- =============================================================================
CREATE TABLE t_kul_ekran (
    ekran_id            BIGSERIAL           PRIMARY KEY,
    ekran_kod           VARCHAR(50)         NOT NULL UNIQUE,
    ekran_ad            VARCHAR(200)        NOT NULL,
    ekran_tip           VARCHAR(10)         NOT NULL,
    altsistem           VARCHAR(10),
    fabrika_kod         BIGINT,
    etkgst              INTEGER             DEFAULT 1,
    ozellik_1           VARCHAR(100),       -- CRUD permissions
    ozellik_2           VARCHAR(100),       -- Query panel layout
    ozellik_3           VARCHAR(100),       -- Query panel columns
    ozellik_4           VARCHAR(100),
    ozellik_5           VARCHAR(100),
    ozellik_6           VARCHAR(100),
    ozellik_7           VARCHAR(100),
    ozellik_8           VARCHAR(100),
    ozellik_9           VARCHAR(100),
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for T_KUL_EKRAN
CREATE INDEX idx_kul_ekran_kod ON t_kul_ekran(ekran_kod);
CREATE INDEX idx_kul_ekran_altsistem ON t_kul_ekran(altsistem);
CREATE INDEX idx_kul_ekran_fabrika ON t_kul_ekran(fabrika_kod);

-- Comments for T_KUL_EKRAN
COMMENT ON TABLE t_kul_ekran IS 'Screen definitions metadata table';
COMMENT ON COLUMN t_kul_ekran.ekran_id IS 'Primary key, unique screen identifier';
COMMENT ON COLUMN t_kul_ekran.ekran_kod IS 'Screen code (e.g., CUSTOMER_LIST)';
COMMENT ON COLUMN t_kul_ekran.ekran_ad IS 'Screen display name';
COMMENT ON COLUMN t_kul_ekran.ekran_tip IS 'Screen type: T=Table, MS=Master-Slave';
COMMENT ON COLUMN t_kul_ekran.altsistem IS 'Module code (e.g., GNL, FNS)';
COMMENT ON COLUMN t_kul_ekran.fabrika_kod IS 'Factory code for multi-tenant';
COMMENT ON COLUMN t_kul_ekran.etkgst IS 'Active flag: 1=Active, 0=Inactive';
COMMENT ON COLUMN t_kul_ekran.ozellik_1 IS 'CRUD permissions (e.g., CRUD, RU, R)';
COMMENT ON COLUMN t_kul_ekran.ozellik_2 IS 'Query panel layout: 0=GRID, 1=FLOW, 2=VERTICAL, 3=GROUP';
COMMENT ON COLUMN t_kul_ekran.ozellik_3 IS 'Query panel columns (1-12)';

-- =============================================================================
-- 2. T_KUL_EKRAN_TABLO (Table Definitions)
-- =============================================================================
CREATE TABLE t_kul_ekran_tablo (
    tablo_id            BIGSERIAL           PRIMARY KEY,
    ekran_id            BIGINT              NOT NULL,
    tablo_ad            VARCHAR(100)        NOT NULL,
    db_tablo            VARCHAR(100)        NOT NULL,
    tablo_id_kolon      VARCHAR(100),
    tipi                VARCHAR(10)         DEFAULT 'T',
    siralama            INTEGER             DEFAULT 1,
    where_kosul         VARCHAR(4000),
    ozellik_sorgu1      VARCHAR(200),
    ozellik_sorgu2      VARCHAR(200),
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ekran_tablo_ekran FOREIGN KEY (ekran_id)
        REFERENCES t_kul_ekran(ekran_id) ON DELETE CASCADE
);

-- Indexes for T_KUL_EKRAN_TABLO
CREATE INDEX idx_kul_ekran_tablo_ekran ON t_kul_ekran_tablo(ekran_id);
CREATE INDEX idx_kul_ekran_tablo_db ON t_kul_ekran_tablo(db_tablo);

-- Comments for T_KUL_EKRAN_TABLO
COMMENT ON TABLE t_kul_ekran_tablo IS 'Table definitions within screens';
COMMENT ON COLUMN t_kul_ekran_tablo.tablo_id IS 'Primary key, unique table identifier';
COMMENT ON COLUMN t_kul_ekran_tablo.ekran_id IS 'Foreign key to t_kul_ekran';
COMMENT ON COLUMN t_kul_ekran_tablo.tablo_ad IS 'Table display name';
COMMENT ON COLUMN t_kul_ekran_tablo.db_tablo IS 'Database table name';
COMMENT ON COLUMN t_kul_ekran_tablo.tablo_id_kolon IS 'Primary key column name';
COMMENT ON COLUMN t_kul_ekran_tablo.tipi IS 'Table type: T=Standard, C=Complex, TR=Tree, MS=Master, SM=Slave';
COMMENT ON COLUMN t_kul_ekran_tablo.siralama IS 'Sort order for multiple tables';
COMMENT ON COLUMN t_kul_ekran_tablo.where_kosul IS 'Static WHERE clause with parameters';

-- =============================================================================
-- 3. T_KUL_EKRAN_TABLO_KOLON (Column Definitions)
-- =============================================================================
CREATE TABLE t_kul_ekran_tablo_kolon (
    kolon_id            BIGSERIAL           PRIMARY KEY,
    tablo_id            BIGINT              NOT NULL,
    kolon_ad            VARCHAR(100)        NOT NULL,
    db_kolon            VARCHAR(100)        NOT NULL,
    tipi                VARCHAR(20)         NOT NULL,
    uzunluk             INTEGER             DEFAULT 0,
    uzunluk_skala       INTEGER             DEFAULT 0,
    tablo_genislik      INTEGER             DEFAULT 100,
    zorunlu             INTEGER             DEFAULT 0,
    gizli               INTEGER             DEFAULT 0,
    panel               INTEGER             DEFAULT 0,
    siralama            INTEGER             DEFAULT 1,
    kodlu_alan          VARCHAR(500),
    standart_deger      VARCHAR(500),
    join_tablo          VARCHAR(100),
    join_tablo_db_kod   VARCHAR(100),
    join_tablo_db_ad    VARCHAR(100),
    join_db_kolon       VARCHAR(100),
    join_tipi           VARCHAR(20),
    ozellik_panel1      VARCHAR(100),       -- Query logic: AND/OR
    ozellik_panel2      VARCHAR(100),       -- Query operator: =, LIKE, <, >, IN
    ozellik_panel3      VARCHAR(100),       -- Auto-query flag
    ozellik_kolon1      VARCHAR(100),       -- Default value expression
    ozellik_kolon2      VARCHAR(4000),      -- JOIN WHERE condition
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ekran_kolon_tablo FOREIGN KEY (tablo_id)
        REFERENCES t_kul_ekran_tablo(tablo_id) ON DELETE CASCADE
);

-- Indexes for T_KUL_EKRAN_TABLO_KOLON
CREATE INDEX idx_kul_ekran_kolon_tablo ON t_kul_ekran_tablo_kolon(tablo_id);
CREATE INDEX idx_kul_ekran_kolon_db ON t_kul_ekran_tablo_kolon(db_kolon);
CREATE INDEX idx_kul_ekran_kolon_tipi ON t_kul_ekran_tablo_kolon(tipi);

-- Comments for T_KUL_EKRAN_TABLO_KOLON
COMMENT ON TABLE t_kul_ekran_tablo_kolon IS 'Column definitions within tables';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.kolon_id IS 'Primary key, unique column identifier';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.tablo_id IS 'Foreign key to t_kul_ekran_tablo';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.kolon_ad IS 'Column display name';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.db_kolon IS 'Database column name';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.tipi IS 'Column type: STR, LONG, INT, NUMBER, BIG, DATE, BOOL, COMBO, JOIN, PK';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.uzunluk IS 'Field length (for STR, NUMBER)';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.uzunluk_skala IS 'Decimal scale (for NUMBER, BIG)';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.tablo_genislik IS 'Table column width in pixels';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.zorunlu IS 'Required flag: 1=Required, 0=Optional';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.gizli IS 'Hidden flag: 1=Hidden, 0=Visible';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.panel IS 'Query panel: 0=Table only, 1=Both, 2=Panel only, 3=Hidden';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.kodlu_alan IS 'ComboBox options (code1=Name1;code2=Name2;;)';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.standart_deger IS 'Default value or parameter';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.join_tablo IS 'JOIN table name';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.join_tablo_db_kod IS 'JOIN code column';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.join_tablo_db_ad IS 'JOIN display column';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.join_db_kolon IS 'JOIN primary key column';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.join_tipi IS 'JOIN type: LEFT, INNER';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.ozellik_panel1 IS 'Query logic: AND, OR';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.ozellik_panel2 IS 'Query operator: =, LIKE, <, >, >=, <=, <>, IN';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.ozellik_panel3 IS 'Auto-query flag: 1=Query after selection';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.ozellik_kolon1 IS 'Default value expression';
COMMENT ON COLUMN t_kul_ekran_tablo_kolon.ozellik_kolon2 IS 'JOIN WHERE condition';

-- =============================================================================
-- 4. T_KUL_EKRAN_YETKI (Permissions - Optional)
-- =============================================================================
CREATE TABLE t_kul_ekran_yetki (
    yetki_id            BIGSERIAL           PRIMARY KEY,
    ekran_id            BIGINT              NOT NULL,
    rol                 VARCHAR(50)         NOT NULL,
    servis              VARCHAR(200),
    fabrika_kod         BIGINT,
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ekran_yetki_ekran FOREIGN KEY (ekran_id)
        REFERENCES t_kul_ekran(ekran_id) ON DELETE CASCADE
);

-- Indexes for T_KUL_EKRAN_YETKI
CREATE INDEX idx_kul_ekran_yetki_ekran ON t_kul_ekran_yetki(ekran_id);
CREATE INDEX idx_kul_ekran_yetki_rol ON t_kul_ekran_yetki(rol);

-- Comments for T_KUL_EKRAN_YETKI
COMMENT ON TABLE t_kul_ekran_yetki IS 'Role-based permissions for screens';
COMMENT ON COLUMN t_kul_ekran_yetki.ekran_id IS 'Foreign key to t_kul_ekran';
COMMENT ON COLUMN t_kul_ekran_yetki.rol IS 'Role name';
COMMENT ON COLUMN t_kul_ekran_yetki.servis IS 'Service name';
COMMENT ON COLUMN t_kul_ekran_yetki.fabrika_kod IS 'Factory code';

-- =============================================================================
-- Trigger to update updated_at timestamp
-- =============================================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_t_kul_ekran_updated_at
    BEFORE UPDATE ON t_kul_ekran
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_t_kul_ekran_tablo_updated_at
    BEFORE UPDATE ON t_kul_ekran_tablo
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_t_kul_ekran_tablo_kolon_updated_at
    BEFORE UPDATE ON t_kul_ekran_tablo_kolon
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_t_kul_ekran_yetki_updated_at
    BEFORE UPDATE ON t_kul_ekran_yetki
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
