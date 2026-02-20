package ai.qodo.dao;

import ai.qodo.pojo.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private StockDao stockDao;

    @BeforeEach
    void setUp() {
        stockDao = new StockDao(jdbcTemplate);
    }

    @Test
    void testCreateStock_ShouldExecuteInsertQuery() {
        String symbol = "AAPL";
        String companyName = "Apple Inc.";
        BigDecimal price = new BigDecimal("150.50");
        int quantity = 100;

        stockDao.createStock(symbol, companyName, price, quantity);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), eq(symbol), eq(companyName), eq(price), eq(quantity));
        
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("INSERT INTO stock"));
        assertTrue(capturedSql.contains("symbol"));
        assertTrue(capturedSql.contains("company_name"));
        assertTrue(capturedSql.contains("current_price"));
        assertTrue(capturedSql.contains("quantity"));
    }

    @Test
    void testUpdateStock_ShouldExecuteUpdateQuery() {
        int id = 1;
        String symbol = "GOOGL";
        String companyName = "Google LLC";
        BigDecimal price = new BigDecimal("200.75");
        int quantity = 50;

        stockDao.updateStock(id, symbol, companyName, price, quantity);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), eq(symbol), eq(companyName), eq(price), eq(quantity), eq(id));
        
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("UPDATE stock"));
        assertTrue(capturedSql.contains("SET"));
        assertTrue(capturedSql.contains("WHERE id = ?"));
    }

    @Test
    void testReadStocks_ShouldReturnListOfStocks() {
        Stock stock1 = new Stock(1, "AAPL", "Apple Inc.", new BigDecimal("150.50"), 100);
        Stock stock2 = new Stock(2, "GOOGL", "Google LLC", new BigDecimal("200.75"), 50);
        List<Stock> expectedStocks = Arrays.asList(stock1, stock2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedStocks);

        List<Stock> result = stockDao.readStocks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
        assertEquals("GOOGL", result.get(1).getSymbol());
        
        verify(jdbcTemplate).query(eq("SELECT * FROM stock"), any(RowMapper.class));
    }

    @Test
    void testReadStocks_ShouldReturnEmptyListWhenNoStocks() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        List<Stock> result = stockDao.readStocks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateStock_WithNullValues() {
        stockDao.createStock(null, null, null, 0);

        verify(jdbcTemplate).update(anyString(), isNull(), isNull(), isNull(), eq(0));
    }
}
