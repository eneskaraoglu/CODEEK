package com.screenengine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for Screen Engine.
 * Maps to screen-engine.* properties in application.yml
 */
@Data
@Component
@ConfigurationProperties(prefix = "screen-engine")
public class ScreenEngineProperties {

    private DataDatasource dataDatasource = new DataDatasource();
    private Parameters parameters = new Parameters();
    private Sql sql = new Sql();
    private Security security = new Security();
    private Cache cache = new Cache();

    @Data
    public static class DataDatasource {
        private boolean enabled = false;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private HikariConfig hikari = new HikariConfig();
    }

    @Data
    public static class HikariConfig {
        private String poolName;
        private int maximumPoolSize = 20;
        private int minimumIdle = 5;
        private long connectionTimeout = 30000;
    }

    @Data
    public static class Parameters {
        private Long factory = 101L;
        private String user = "ADMIN";
        private Long personId = 0L;
        private String orgCode = "DEFAULT";
        private Long accountingOrgId = 1L;
        private Long accountingPeriodId = 202401L;
        private Long departmentNo = 10L;

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("FACTORY", factory);
            map.put("FABRIKA", factory);
            map.put("KULLANICI", user);
            map.put("KISIID", personId);
            map.put("ORGKODU", orgCode);
            map.put("MUHORGID", accountingOrgId);
            map.put("MUHORGDONEMID", accountingPeriodId);
            map.put("BIRIMNO", departmentNo);
            return map;
        }
    }

    @Data
    public static class Sql {
        private String dialect = "POSTGRESQL";
        private int maxResults = 1000;
        private int queryTimeout = 30;
        private boolean enableDebugQuery = true;
    }

    @Data
    public static class Security {
        private Jwt jwt = new Jwt();
        private Cors cors = new Cors();
    }

    @Data
    public static class Jwt {
        private String secret = "screen-engine-secret-key-change-in-production";
        private long expiration = 3600000L;
    }

    @Data
    public static class Cors {
        private String allowedOrigins = "http://localhost:5173,http://localhost:3000";
        private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS";
        private String allowedHeaders = "*";
        private boolean allowCredentials = true;
    }

    @Data
    public static class Cache {
        private boolean enabled = false;
        private int ttl = 300;
    }
}
