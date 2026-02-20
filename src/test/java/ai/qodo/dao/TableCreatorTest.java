package ai.qodo.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableCreatorTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private TableCreator tableCreator;

    @BeforeEach
    void setUp() {
        tableCreator = new TableCreator(jdbcTemplate);
    }

    @Test
    void testInit_ShouldCreateAllTables() {
        tableCreator.init();

        // Verify that execute is called 4 times (orders, stock, trade, user_profile)
        verify(jdbcTemplate, times(4)).execute(anyString());
    }

    @Test
    void testInit_ShouldCreateOrdersTable() {
        tableCreator.init();

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, atLeastOnce()).execute(sqlCaptor.capture());

        boolean ordersTableCreated = sqlCaptor.getAllValues().stream()
            .anyMatch(sql -> sql.contains("CREATE TABLE orders"));
        
        assertTrue(ordersTableCreated, "Orders table should be created");
    }

    @Test
    void testInit_ShouldCreateStockTable() {
        tableCreator.init();

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, atLeastOnce()).execute(sqlCaptor.capture());

        boolean stockTableCreated = sqlCaptor.getAllValues().stream()
            .anyMatch(sql -> sql.contains("CREATE TABLE stock"));
        
        assertTrue(stockTableCreated, "Stock table should be created");
    }

    @Test
    void testInit_ShouldCreateTradeTable() {
        tableCreator.init();

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, atLeastOnce()).execute(sqlCaptor.capture());

        boolean tradeTableCreated = sqlCaptor.getAllValues().stream()
            .anyMatch(sql -> sql.contains("CREATE TABLE trade"));
        
        assertTrue(tradeTableCreated, "Trade table should be created");
    }

    @Test
    void testInit_ShouldCreateUserProfileTable() {
        tableCreator.init();

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, atLeastOnce()).execute(sqlCaptor.capture());

        boolean userProfileTableCreated = sqlCaptor.getAllValues().stream()
            .anyMatch(sql -> sql.contains("CREATE TABLE user_profile"));
        
        assertTrue(userProfileTableCreated, "UserProfile table should be created");
    }

    @Test
    void testInit_VerifyTableStructures() {
        tableCreator.init();

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(4)).execute(sqlCaptor.capture());

        // Verify that all captured SQL contains necessary keywords
        for (String sql : sqlCaptor.getAllValues()) {
            assertTrue(sql.contains("CREATE TABLE"));
            assertTrue(sql.contains("id INT PRIMARY KEY AUTO_INCREMENT"));
        }
    }
}
