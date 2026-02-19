package ai.qodo.dao;

import ai.qodo.DataAccessApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.jdbc.core.JdbcTemplate;

@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public TableCreator tableCreator(JdbcTemplate jdbcTemplate) {
        // Return a no-op TableCreator for tests
        return new TableCreator(jdbcTemplate) {
            @Override
            public void init() {
                // Do nothing - tables are created via schema-test.sql
            }
        };
    }
    
    @Bean
    @Primary
    public ApplicationListener<ApplicationReadyEvent> testInitListener() {
        // Override the init listener from DataAccessApplication to prevent table creation
        return event -> {
            // Do nothing in tests
        };
    }
}
