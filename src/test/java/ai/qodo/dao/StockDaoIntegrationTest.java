package ai.qodo.dao;

import ai.qodo.pojo.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class StockDaoIntegrationTest {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private TableCreator tableCreator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        try {
            tableCreator.init();
        } catch (Exception e) {
            // Tables might already exist
        }
        // Clean up any existing data
        jdbcTemplate.execute("DELETE FROM stock");
    }

    @Test
    void testCreateAndReadStock() {
        stockDao.createStock("AAPL", "Apple Inc", new BigDecimal("150.50"), 100);

        List<Stock> stocks = stockDao.readStocks();

        assertFalse(stocks.isEmpty());
        Stock stock = stocks.get(0);
        assertEquals("AAPL", stock.getSymbol());
        assertEquals("Apple Inc", stock.getCompanyName());
        assertEquals(new BigDecimal("150.50"), stock.getCurrentPrice());
        assertEquals(100, stock.getQuantity());
    }

    @Test
    void testUpdateStock() {
        stockDao.createStock("GOOGL", "Google LLC", new BigDecimal("200.00"), 50);
        List<Stock> stocks = stockDao.readStocks();
        int stockId = stocks.get(0).getId();

        stockDao.updateStock(stockId, "GOOGL", "Alphabet Inc", new BigDecimal("250.00"), 75);

        stocks = stockDao.readStocks();
        Stock updatedStock = stocks.stream()
            .filter(s -> s.getId() == stockId)
            .findFirst()
            .orElse(null);

        assertNotNull(updatedStock);
        assertEquals("Alphabet Inc", updatedStock.getCompanyName());
        assertEquals(new BigDecimal("250.00"), updatedStock.getCurrentPrice());
        assertEquals(75, updatedStock.getQuantity());
    }

    @Test
    void testReadStocks_MultipleStocks() {
        stockDao.createStock("AAPL", "Apple Inc", new BigDecimal("150.50"), 100);
        stockDao.createStock("GOOGL", "Google LLC", new BigDecimal("200.75"), 50);
        stockDao.createStock("MSFT", "Microsoft Corp", new BigDecimal("300.00"), 75);

        List<Stock> stocks = stockDao.readStocks();

        assertEquals(3, stocks.size());
    }
}
