package ai.qodo.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableCreatorTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TableCreator tableCreator;

    @Test
    void testInitCallsAllTableCreationMethods() {
        // Act
        tableCreator.init();

        // Assert - verify that execute is called 4 times (once for each table)
        verify(jdbcTemplate, times(4)).execute(anyString());
    }

    @Test
    void testInitCreatesOrdersTable() {
        // Act
        tableCreator.init();

        // Assert - verify orders table SQL is executed
        verify(jdbcTemplate).execute(contains("CREATE TABLE orders"));
    }

    @Test
    void testInitCreatesStockTable() {
        // Act
        tableCreator.init();

        // Assert - verify stock table SQL is executed
        verify(jdbcTemplate).execute(contains("CREATE TABLE stock"));
    }

    @Test
    void testInitCreatesTradeTable() {
        // Act
        tableCreator.init();

        // Assert - verify trade table SQL is executed
        verify(jdbcTemplate).execute(contains("CREATE TABLE trade"));
    }

    @Test
    void testInitCreatesUserProfileTable() {
        // Act
        tableCreator.init();

        // Assert - verify user_profile table SQL is executed
        verify(jdbcTemplate).execute(contains("CREATE TABLE user_profile"));
    }

    @Test
    void testInitVerifyAllTablesHaveCorrectStructure() {
        // Act
        tableCreator.init();

        // Assert - verify all tables have primary key and AUTO_INCREMENT
        verify(jdbcTemplate, times(4)).execute(contains("PRIMARY KEY AUTO_INCREMENT"));
    }
}
