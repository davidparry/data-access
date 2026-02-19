package ai.qodo.dao;

import ai.qodo.pojo.Trade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TradeDao.
 * 
 * SECURITY WARNING: TradeDao.createTrade() and TradeDao.updateTrade() use string concatenation
 * which creates SQL injection vulnerabilities. These tests cover the current implementation
 * but the code should be refactored to use prepared statements.
 */
@ExtendWith(MockitoExtension.class)
class TradeDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TradeDao tradeDao;

    @Test
    void testCreateTrade() {
        // Arrange
        String symbol = "AAPL";
        String tradeType = "BUY";
        int quantity = 100;
        BigDecimal price = new BigDecimal("150.50");

        // Act
        tradeDao.createTrade(symbol, tradeType, quantity, price);

        // Assert
        // NOTE: SQL injection vulnerability - string concatenation used
        String expectedSql = "INSERT INTO trade (symbol, trade_type, quantity, price) VALUES ('AAPL', 'BUY', 100, 150.50)";
        verify(jdbcTemplate).execute(eq(expectedSql));
    }

    @Test
    void testUpdateTrade() {
        // Arrange
        int id = 1;
        String symbol = "GOOGL";
        String tradeType = "SELL";
        int quantity = 50;
        BigDecimal price = new BigDecimal("2800.75");

        // Act
        tradeDao.updateTrade(id, symbol, tradeType, quantity, price);

        // Assert
        // NOTE: SQL injection vulnerability - string concatenation used
        String expectedSql = "UPDATE trade SET symbol = 'GOOGL', trade_type = 'SELL', quantity = 50, price = 2800.75 WHERE id = 1";
        verify(jdbcTemplate).execute(eq(expectedSql));
    }

    @Test
    void testReadTrades() {
        // Arrange
        Trade trade1 = new Trade(1, "AAPL", "BUY", 100, new BigDecimal("150.50"), new Timestamp(System.currentTimeMillis()));
        Trade trade2 = new Trade(2, "GOOGL", "SELL", 50, new BigDecimal("2800.75"), new Timestamp(System.currentTimeMillis()));
        List<Trade> expectedTrades = Arrays.asList(trade1, trade2);
        
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedTrades);

        // Act
        List<Trade> result = tradeDao.readTrades();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedTrades, result);
        verify(jdbcTemplate).query(eq("SELECT * FROM trade"), any(RowMapper.class));
    }

    @Test
    void testReadTradesEmpty() {
        // Arrange
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        // Act
        List<Trade> result = tradeDao.readTrades();

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcTemplate).query(eq("SELECT * FROM trade"), any(RowMapper.class));
    }
}
