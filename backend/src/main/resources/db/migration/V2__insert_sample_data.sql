-- Screen Engine Sample Data
-- Version: 1.0.0
-- Description: Inserts sample screen metadata for testing

-- =============================================================================
-- Sample Screen: CUSTOMER_LIST
-- =============================================================================

-- 1. Insert Screen Definition
INSERT INTO t_kul_ekran (
    ekran_kod, ekran_ad, ekran_tip, altsistem, fabrika_kod,
    etkgst, ozellik_1, ozellik_2, ozellik_3
) VALUES (
    'CUSTOMER_LIST',
    'Customer List',
    'T',
    'GNL',
    101,
    1,
    'CRUD',
    '0',
    '4'
);

-- Get the ekran_id for the screen we just created
DO $$
DECLARE
    v_ekran_id BIGINT;
    v_tablo_id BIGINT;
BEGIN
    -- Get screen ID
    SELECT ekran_id INTO v_ekran_id
    FROM t_kul_ekran
    WHERE ekran_kod = 'CUSTOMER_LIST';

    -- 2. Insert Table Definition
    INSERT INTO t_kul_ekran_tablo (
        ekran_id, tablo_ad, db_tablo, tablo_id_kolon,
        tipi, siralama, where_kosul
    ) VALUES (
        v_ekran_id,
        'Customers',
        't_customer',
        'customer_id',
        'T',
        1,
        'fabrika_kod = :FACTORY'
    ) RETURNING tablo_id INTO v_tablo_id;

    -- 3. Insert Column Definitions

    -- Column 1: Primary Key
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        zorunlu, gizli, panel, siralama
    ) VALUES (
        v_tablo_id, 'ID', 'customer_id', 'PK',
        1, 1, 0, 1
    );

    -- Column 2: Customer Code (String, searchable)
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        uzunluk, tablo_genislik, zorunlu, panel,
        ozellik_panel1, ozellik_panel2, siralama
    ) VALUES (
        v_tablo_id, 'Customer Code', 'customer_code', 'STR',
        50, 150, 1, 1,
        'AND', 'LIKE', 2
    );

    -- Column 3: Customer Name
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        uzunluk, tablo_genislik, zorunlu, panel,
        ozellik_panel1, ozellik_panel2, siralama
    ) VALUES (
        v_tablo_id, 'Customer Name', 'customer_name', 'STR',
        200, 200, 1, 1,
        'AND', 'LIKE', 3
    );

    -- Column 4: Status (ComboBox)
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        kodlu_alan, tablo_genislik, panel,
        ozellik_panel1, ozellik_panel2, siralama,
        standart_deger
    ) VALUES (
        v_tablo_id, 'Status', 'status', 'COMBO',
        'A=Active;P=Passive;S=Suspended;;', 100, 1,
        'AND', '=', 4,
        'A'
    );

    -- Column 5: Active (Boolean)
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        tablo_genislik, panel, ozellik_panel1,
        siralama, standart_deger
    ) VALUES (
        v_tablo_id, 'Active', 'active', 'BOOL',
        80, 1, 'AND',
        5, '1'
    );

    -- Column 6: Created Date
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        tablo_genislik, panel, ozellik_panel1,
        ozellik_panel2, siralama, ozellik_kolon1
    ) VALUES (
        v_tablo_id, 'Created Date', 'created_date', 'DATE',
        120, 1, 'AND',
        '>=', 6, ':SISTEMTARIHI'
    );

    -- Column 7: Email
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        uzunluk, tablo_genislik, zorunlu, panel,
        siralama
    ) VALUES (
        v_tablo_id, 'Email', 'email', 'STR',
        100, 180, 0, 0,
        7
    );

    -- Column 8: Phone
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        uzunluk, tablo_genislik, zorunlu, panel,
        siralama
    ) VALUES (
        v_tablo_id, 'Phone', 'phone', 'STR',
        20, 120, 0, 0,
        8
    );

    -- Column 9: Factory Code (hidden, auto-populated)
    INSERT INTO t_kul_ekran_tablo_kolon (
        tablo_id, kolon_ad, db_kolon, tipi,
        zorunlu, gizli, panel, siralama,
        standart_deger
    ) VALUES (
        v_tablo_id, 'Factory Code', 'fabrika_kod', 'LONG',
        1, 1, 0, 9,
        ':FACTORY'
    );

END $$;

-- =============================================================================
-- Sample Data Table: t_customer (for testing)
-- =============================================================================

-- Create sample customer table
CREATE TABLE IF NOT EXISTS t_customer (
    customer_id         BIGSERIAL           PRIMARY KEY,
    customer_code       VARCHAR(50)         NOT NULL UNIQUE,
    customer_name       VARCHAR(200)        NOT NULL,
    status              VARCHAR(1)          DEFAULT 'A',
    active              INTEGER             DEFAULT 1,
    created_date        DATE                DEFAULT CURRENT_DATE,
    email               VARCHAR(100),
    phone               VARCHAR(20),
    fabrika_kod         BIGINT              NOT NULL,
    created_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP           DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample customer data
INSERT INTO t_customer (
    customer_code, customer_name, status, active,
    created_date, email, phone, fabrika_kod
) VALUES
    ('C001', 'ABC Corporation', 'A', 1, CURRENT_DATE, 'contact@abc.com', '+1234567890', 101),
    ('C002', 'XYZ Industries', 'A', 1, CURRENT_DATE, 'info@xyz.com', '+1234567891', 101),
    ('C003', 'Global Trade LLC', 'A', 1, CURRENT_DATE - 30, 'sales@globaltrade.com', '+1234567892', 101),
    ('C004', 'Tech Solutions Inc', 'P', 1, CURRENT_DATE - 60, 'support@techsolutions.com', '+1234567893', 101),
    ('C005', 'Premium Services', 'S', 0, CURRENT_DATE - 90, 'admin@premium.com', '+1234567894', 101);

-- Create trigger for t_customer updated_at
CREATE TRIGGER update_t_customer_updated_at
    BEFORE UPDATE ON t_customer
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =============================================================================
-- Sample Screen 2: SIMPLE_TEST
-- =============================================================================

INSERT INTO t_kul_ekran (
    ekran_kod, ekran_ad, ekran_tip, altsistem, fabrika_kod,
    etkgst, ozellik_1, ozellik_2, ozellik_3
) VALUES (
    'SIMPLE_TEST',
    'Simple Test Screen',
    'T',
    'GNL',
    101,
    1,
    'R',
    '0',
    '2'
);
