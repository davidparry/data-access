package ai.qodo.dao;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigurationTest {

    @Test
    void testJdbcTemplateBean() {
        // Arrange
        Configuration configuration = new Configuration();
        DataSource mockDataSource = mock(DataSource.class);

        // Act
        JdbcTemplate jdbcTemplate = configuration.jdbcTemplate(mockDataSource);

        // Assert
        assertNotNull(jdbcTemplate);
        assertEquals(mockDataSource, jdbcTemplate.getDataSource());
    }

    @Test
    void testJdbcTemplateBeanCreatesNewInstance() {
        // Arrange
        Configuration configuration = new Configuration();
        DataSource mockDataSource = mock(DataSource.class);

        // Act
        JdbcTemplate jdbcTemplate1 = configuration.jdbcTemplate(mockDataSource);
        JdbcTemplate jdbcTemplate2 = configuration.jdbcTemplate(mockDataSource);

        // Assert
        assertNotNull(jdbcTemplate1);
        assertNotNull(jdbcTemplate2);
        assertNotSame(jdbcTemplate1, jdbcTemplate2, "Each call should create a new instance");
    }
}
