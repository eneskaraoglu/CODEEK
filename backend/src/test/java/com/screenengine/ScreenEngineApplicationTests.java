package com.screenengine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic integration test to verify application context loads successfully.
 */
@SpringBootTest
@ActiveProfiles("test")
class ScreenEngineApplicationTests {

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }

}
