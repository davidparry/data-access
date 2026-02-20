package ai.qodo.dao;

import ai.qodo.pojo.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

@ExtendWith(MockitoExtension.class)
class TradeDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private TradeDao tradeDao;

    @BeforeEach
    void setUp() {
        tradeDao = new TradeDao(jdbcTemplate);
    }

    @Test
    void testCreateTrade_ShouldExecuteInsertQuery() {
        // NOTE: This test validates the current implementation which has SQL injection vulnerability
        // This should be fixed in a separate security remediation task
        String symbol = "AAPL";
        String tradeType = "BUY";
        int quantity = 100;
        BigDecimal price = new BigDecimal("150.50");

        tradeDao.createTrade(symbol, tradeType, quantity, price);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(sqlCaptor.capture());
        
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("INSERT INTO trade"));
        assertTrue(capturedSql.contains("AAPL"));
        assertTrue(capturedSql.contains("BUY"));
        assertTrue(capturedSql.contains("100"));
        assertTrue(capturedSql.contains("150.50"));
    }

    @Test
    void testUpdateTrade_ShouldExecuteUpdateQuery() {
        // NOTE: This test validates the current implementation which has SQL injection vulnerability
        // This should be fixed in a separate security remediation task
        int id = 1;
        String symbol = "GOOGL";
        String tradeType = "SELL";
        int quantity = 50;
        BigDecimal price = new BigDecimal("200.75");

        tradeDao.updateTrade(id, symbol, tradeType, quantity, price);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(sqlCaptor.capture());
        
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("UPDATE trade"));
        assertTrue(capturedSql.contains("SET"));
        assertTrue(capturedSql.contains("WHERE id = " + id));
    }

    @Test
    void testReadTrades_ShouldReturnListOfTrades() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Trade trade1 = new Trade(1, "AAPL", "BUY", 100, new BigDecimal("150.50"), timestamp);
        Trade trade2 = new Trade(2, "GOOGL", "SELL", 50, new BigDecimal("200.75"), timestamp);
        List<Trade> expectedTrades = Arrays.asList(trade1, trade2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedTrades);

        List<Trade> result = tradeDao.readTrades();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
        assertEquals("GOOGL", result.get(1).getSymbol());
        
        verify(jdbcTemplate).query(eq("SELECT * FROM trade"), any(RowMapper.class));
    }

    @Test
    void testReadTrades_ShouldReturnEmptyListWhenNoTrades() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        List<Trade> result = tradeDao.readTrades();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
