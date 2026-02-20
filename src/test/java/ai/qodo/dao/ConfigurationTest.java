package ai.qodo.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ConfigurationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testJdbcTemplateBean_IsCreated() {
        assertNotNull(jdbcTemplate, "JdbcTemplate bean should be created by Configuration");
    }

    @Test
    void testJdbcTemplateBean_IsConfigured() {
        assertNotNull(jdbcTemplate.getDataSource(), "JdbcTemplate should have a DataSource configured");
    }

    @Test
    void testJdbcTemplateBean_CanExecuteQuery() {
        // Simple query to verify JdbcTemplate is functional
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertEquals(1, result, "JdbcTemplate should be able to execute queries");
    }
}
