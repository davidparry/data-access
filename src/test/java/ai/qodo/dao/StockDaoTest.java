package ai.qodo.dao;

import ai.qodo.pojo.Stock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private StockDao stockDao;

    @Test
    void testCreateStock() {
        // Arrange
        String symbol = "AAPL";
        String companyName = "Apple Inc.";
        BigDecimal currentPrice = new BigDecimal("150.50");
        int quantity = 100;

        // Act
        stockDao.createStock(symbol, companyName, currentPrice, quantity);

        // Assert
        verify(jdbcTemplate).update(
            eq("INSERT INTO stock (symbol, company_name, current_price, quantity) VALUES (?, ?, ?, ?)"),
            eq(symbol),
            eq(companyName),
            eq(currentPrice),
            eq(quantity)
        );
    }

    @Test
    void testUpdateStock() {
        // Arrange
        int id = 1;
        String symbol = "GOOGL";
        String companyName = "Alphabet Inc.";
        BigDecimal currentPrice = new BigDecimal("2800.75");
        int quantity = 50;

        // Act
        stockDao.updateStock(id, symbol, companyName, currentPrice, quantity);

        // Assert
        verify(jdbcTemplate).update(
            eq("UPDATE stock SET symbol = ?, company_name = ?, current_price = ?, quantity = ? WHERE id = ?"),
            eq(symbol),
            eq(companyName),
            eq(currentPrice),
            eq(quantity),
            eq(id)
        );
    }

    @Test
    void testReadStocks() {
        // Arrange
        Stock stock1 = new Stock(1, "AAPL", "Apple Inc.", new BigDecimal("150.50"), 100);
        Stock stock2 = new Stock(2, "GOOGL", "Alphabet Inc.", new BigDecimal("2800.75"), 50);
        List<Stock> expectedStocks = Arrays.asList(stock1, stock2);
        
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedStocks);

        // Act
        List<Stock> result = stockDao.readStocks();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedStocks, result);
        verify(jdbcTemplate).query(eq("SELECT * FROM stock"), any(RowMapper.class));
    }

    @Test
    void testReadStocksEmpty() {
        // Arrange
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        // Act
        List<Stock> result = stockDao.readStocks();

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcTemplate).query(eq("SELECT * FROM stock"), any(RowMapper.class));
    }
}
