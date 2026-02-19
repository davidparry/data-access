package ai.qodo.dao;
import ai.qodo.DataAccessApplication;


import ai.qodo.pojo.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DataAccessApplication.class, TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class StockDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StockDao stockDao;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        jdbcTemplate.execute("DELETE FROM stock");
    }

    @Test
    void testCreateStock_WithValidData_Success() {
        // Arrange
        String symbol = "AAPL";
        String companyName = "Apple Inc.";
        BigDecimal currentPrice = new BigDecimal("150.25");
        int quantity = 100;

        // Act
        stockDao.createStock(symbol, companyName, currentPrice, quantity);

        // Assert
        List<Stock> stocks = stockDao.readStocks();
        assertEquals(1, stocks.size());
        Stock stock = stocks.get(0);
        assertEquals(symbol, stock.getSymbol());
        assertEquals(companyName, stock.getCompanyName());
        assertEquals(currentPrice, stock.getCurrentPrice());
        assertEquals(quantity, stock.getQuantity());
    }

    @Test
    void testCreateStock_WithNullCompanyName_Success() {
        // Arrange
        String symbol = "GOOGL";
        String companyName = null;
        BigDecimal currentPrice = new BigDecimal("2800.50");
        int quantity = 50;

        // Act
        stockDao.createStock(symbol, companyName, currentPrice, quantity);

        // Assert
        List<Stock> stocks = stockDao.readStocks();
        assertEquals(1, stocks.size());
        Stock stock = stocks.get(0);
        assertEquals(symbol, stock.getSymbol());
        assertNull(stock.getCompanyName());
        assertEquals(currentPrice, stock.getCurrentPrice());
        assertEquals(quantity, stock.getQuantity());
    }

    @Test
    void testCreateStock_WithZeroQuantity_Success() {
        // Arrange
        String symbol = "TSLA";
        String companyName = "Tesla Inc.";
        BigDecimal currentPrice = new BigDecimal("700.00");
        int quantity = 0;

        // Act
        stockDao.createStock(symbol, companyName, currentPrice, quantity);

        // Assert
        List<Stock> stocks = stockDao.readStocks();
        assertEquals(1, stocks.size());
        assertEquals(0, stocks.get(0).getQuantity());
    }

    @Test
    void testUpdateStock_WithValidData_Success() {
        // Arrange - Create initial stock
        stockDao.createStock("MSFT", "Microsoft", new BigDecimal("300.00"), 75);
        List<Stock> initialStocks = stockDao.readStocks();
        int stockId = initialStocks.get(0).getId();

        // Act - Update the stock
        String newSymbol = "MSFT";
        String newCompanyName = "Microsoft Corporation";
        BigDecimal newPrice = new BigDecimal("310.50");
        int newQuantity = 80;
        stockDao.updateStock(stockId, newSymbol, newCompanyName, newPrice, newQuantity);

        // Assert
        List<Stock> updatedStocks = stockDao.readStocks();
        assertEquals(1, updatedStocks.size());
        Stock updatedStock = updatedStocks.get(0);
        assertEquals(newSymbol, updatedStock.getSymbol());
        assertEquals(newCompanyName, updatedStock.getCompanyName());
        assertEquals(newPrice, updatedStock.getCurrentPrice());
        assertEquals(newQuantity, updatedStock.getQuantity());
    }

    @Test
    void testUpdateStock_NonExistentId_NoEffect() {
        // Arrange
        stockDao.createStock("AMZN", "Amazon", new BigDecimal("3200.00"), 25);

        // Act - Try to update non-existent stock
        stockDao.updateStock(99999, "FAKE", "Fake Company", new BigDecimal("1.00"), 1);

        // Assert - Original stock should remain unchanged
        List<Stock> stocks = stockDao.readStocks();
        assertEquals(1, stocks.size());
        assertEquals("AMZN", stocks.get(0).getSymbol());
    }

    @Test
    void testReadStocks_EmptyDatabase_ReturnsEmptyList() {
        // Act
        List<Stock> stocks = stockDao.readStocks();

        // Assert
        assertNotNull(stocks);
        assertTrue(stocks.isEmpty());
    }

    @Test
    void testReadStocks_MultipleRecords_ReturnsAllRecords() {
        // Arrange
        stockDao.createStock("AAPL", "Apple Inc.", new BigDecimal("150.25"), 100);
        stockDao.createStock("GOOGL", "Alphabet Inc.", new BigDecimal("2800.50"), 50);
        stockDao.createStock("MSFT", "Microsoft Corp.", new BigDecimal("300.00"), 75);

        // Act
        List<Stock> stocks = stockDao.readStocks();

        // Assert
        assertEquals(3, stocks.size());
    }

    @Test
    void testReadStocks_VerifyAllFields_CorrectMapping() {
        // Arrange
        String symbol = "NFLX";
        String companyName = "Netflix Inc.";
        BigDecimal currentPrice = new BigDecimal("500.75");
        int quantity = 60;
        stockDao.createStock(symbol, companyName, currentPrice, quantity);

        // Act
        List<Stock> stocks = stockDao.readStocks();

        // Assert
        assertEquals(1, stocks.size());
        Stock stock = stocks.get(0);
        assertNotNull(stock.getId());
        assertTrue(stock.getId() > 0);
        assertEquals(symbol, stock.getSymbol());
        assertEquals(companyName, stock.getCompanyName());
        assertEquals(0, currentPrice.compareTo(stock.getCurrentPrice()));
        assertEquals(quantity, stock.getQuantity());
    }

    @Test
    void testCreateStock_WithNegativeQuantity_Success() {
        // Arrange - Testing edge case of negative quantity
        String symbol = "EDGE";
        String companyName = "Edge Case Inc.";
        BigDecimal currentPrice = new BigDecimal("10.00");
        int quantity = -5;

        // Act
        stockDao.createStock(symbol, companyName, currentPrice, quantity);

        // Assert
        List<Stock> stocks = stockDao.readStocks();
        assertEquals(1, stocks.size());
        assertEquals(-5, stocks.get(0).getQuantity());
    }

    @Test
    void testCreateStock_WithVeryLargePrice_Success() {
        // Arrange - Testing edge case of very large price
        String symbol = "EXPENSIVE";
        String companyName = "Expensive Stock Inc.";
        BigDecimal currentPrice = new BigDecimal("99999999.99");
        int quantity = 1;

        // Act
        stockDao.createStock(symbol, companyName, currentPrice, quantity);

        // Assert
        List<Stock> stocks = stockDao.readStocks();
        assertEquals(1, stocks.size());
        assertEquals(0, currentPrice.compareTo(stocks.get(0).getCurrentPrice()));
    }
}
