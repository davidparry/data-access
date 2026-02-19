package ai.qodo.dao;

import ai.qodo.DataAccessApplication;
import ai.qodo.pojo.Trade;
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
class TradeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TradeDao tradeDao;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        jdbcTemplate.execute("DELETE FROM trade");
    }

    @Test
    void testCreateTrade_WithValidData_Success() {
        // Arrange
        String symbol = "AAPL";
        String tradeType = "BUY";
        int quantity = 100;
        BigDecimal price = new BigDecimal("150.25");

        // Act
        tradeDao.createTrade(symbol, tradeType, quantity, price);

        // Assert
        List<Trade> trades = tradeDao.readTrades();
        assertEquals(1, trades.size());
        Trade trade = trades.get(0);
        assertEquals(symbol, trade.getSymbol());
        assertEquals(tradeType, trade.getTradeType());
        assertEquals(quantity, trade.getQuantity());
        assertEquals(price, trade.getPrice());
        assertNotNull(trade.getTradeDate());
    }

    @Test
    void testCreateTrade_WithNullTradeType_Success() {
        // Arrange
        String symbol = "GOOGL";
        String tradeType = null;
        int quantity = 50;
        BigDecimal price = new BigDecimal("2800.50");

        // Act
        tradeDao.createTrade(symbol, tradeType, quantity, price);

        // Assert
        List<Trade> trades = tradeDao.readTrades();
        assertEquals(1, trades.size());
        Trade trade = trades.get(0);
        assertEquals(symbol, trade.getSymbol());
        // Note: SQL injection vulnerability causes null to be inserted as string "null"
        assertEquals("null", trade.getTradeType());
        assertEquals(quantity, trade.getQuantity());
        assertEquals(price, trade.getPrice());
    }

    @Test
    void testCreateTrade_SQLInjectionAttempt_VulnerabilityExists() {
        // WARNING: This test demonstrates a SQL injection vulnerability in TradeDao.createTrade()
        // The method uses string concatenation: "INSERT INTO trade (symbol, trade_type, quantity, price) VALUES ('" + symbol + "', '" + tradeType + "', " + quantity + ", " + price + ")"
        
        // Arrange - Malicious symbol with SQL injection attempt
        String maliciousSymbol = "HACK'; DROP TABLE trade; --";
        String tradeType = "BUY";
        int quantity = 100;
        BigDecimal price = new BigDecimal("100.00");

        // Act & Assert - This will likely cause an exception due to SQL syntax error
        assertThrows(Exception.class, () -> {
            tradeDao.createTrade(maliciousSymbol, tradeType, quantity, price);
        });
    }

    @Test
    void testUpdateTrade_WithValidData_Success() {
        // Arrange - Create initial trade
        tradeDao.createTrade("MSFT", "SELL", 75, new BigDecimal("300.00"));
        List<Trade> initialTrades = tradeDao.readTrades();
        int tradeId = initialTrades.get(0).getId();

        // Act - Update the trade
        String newSymbol = "MSFT";
        String newTradeType = "BUY";
        int newQuantity = 80;
        BigDecimal newPrice = new BigDecimal("310.50");
        tradeDao.updateTrade(tradeId, newSymbol, newTradeType, newQuantity, newPrice);

        // Assert
        List<Trade> updatedTrades = tradeDao.readTrades();
        assertEquals(1, updatedTrades.size());
        Trade updatedTrade = updatedTrades.get(0);
        assertEquals(newSymbol, updatedTrade.getSymbol());
        assertEquals(newTradeType, updatedTrade.getTradeType());
        assertEquals(newQuantity, updatedTrade.getQuantity());
        assertEquals(newPrice, updatedTrade.getPrice());
    }

    @Test
    void testUpdateTrade_SQLInjectionAttempt_VulnerabilityExists() {
        // WARNING: This test demonstrates a SQL injection vulnerability in TradeDao.updateTrade()
        // The method uses string concatenation: "UPDATE trade SET symbol = '" + symbol + "', trade_type = '" + tradeType + "', quantity = " + quantity + ", price = " + price + " WHERE id = " + id
        
        // Arrange - Create a trade first
        tradeDao.createTrade("TSLA", "BUY", 50, new BigDecimal("700.00"));
        List<Trade> trades = tradeDao.readTrades();
        int tradeId = trades.get(0).getId();

        // Act & Assert - Malicious update attempt
        String maliciousSymbol = "HACK'; DROP TABLE trade; --";
        String tradeType = "SELL";
        int quantity = 100;
        BigDecimal price = new BigDecimal("100.00");
        
        assertThrows(Exception.class, () -> {
            tradeDao.updateTrade(tradeId, maliciousSymbol, tradeType, quantity, price);
        });
    }

    @Test
    void testUpdateTrade_NonExistentId_NoEffect() {
        // Arrange
        tradeDao.createTrade("AMZN", "BUY", 25, new BigDecimal("3200.00"));

        // Act - Try to update non-existent trade
        tradeDao.updateTrade(99999, "FAKE", "SELL", 1, new BigDecimal("1.00"));

        // Assert - Original trade should remain unchanged
        List<Trade> trades = tradeDao.readTrades();
        assertEquals(1, trades.size());
        assertEquals("AMZN", trades.get(0).getSymbol());
    }

    @Test
    void testReadTrades_EmptyDatabase_ReturnsEmptyList() {
        // Act
        List<Trade> trades = tradeDao.readTrades();

        // Assert
        assertNotNull(trades);
        assertTrue(trades.isEmpty());
    }

    @Test
    void testReadTrades_MultipleRecords_ReturnsAllRecords() {
        // Arrange
        tradeDao.createTrade("AAPL", "BUY", 100, new BigDecimal("150.25"));
        tradeDao.createTrade("GOOGL", "SELL", 50, new BigDecimal("2800.50"));
        tradeDao.createTrade("MSFT", "BUY", 75, new BigDecimal("300.00"));

        // Act
        List<Trade> trades = tradeDao.readTrades();

        // Assert
        assertEquals(3, trades.size());
    }

    @Test
    void testReadTrades_VerifyTimestamp_AutoGenerated() {
        // Arrange
        tradeDao.createTrade("NFLX", "BUY", 60, new BigDecimal("500.75"));

        // Act
        List<Trade> trades = tradeDao.readTrades();

        // Assert
        assertEquals(1, trades.size());
        Trade trade = trades.get(0);
        assertNotNull(trade.getTradeDate(), "Trade date should be auto-generated");
    }

    @Test
    void testTradeRowMapper_CorrectMapping_AllFieldsSet() {
        // Arrange
        String symbol = "TSLA";
        String tradeType = "SELL";
        int quantity = 40;
        BigDecimal price = new BigDecimal("700.00");
        tradeDao.createTrade(symbol, tradeType, quantity, price);

        // Act
        List<Trade> trades = tradeDao.readTrades();

        // Assert
        assertEquals(1, trades.size());
        Trade trade = trades.get(0);
        assertNotNull(trade.getId());
        assertTrue(trade.getId() > 0);
        assertEquals(symbol, trade.getSymbol());
        assertEquals(tradeType, trade.getTradeType());
        assertEquals(quantity, trade.getQuantity());
        assertEquals(0, price.compareTo(trade.getPrice()));
        assertNotNull(trade.getTradeDate());
    }

    @Test
    void testCreateTrade_WithZeroQuantity_Success() {
        // Arrange - Testing edge case
        String symbol = "EDGE";
        String tradeType = "BUY";
        int quantity = 0;
        BigDecimal price = new BigDecimal("10.00");

        // Act
        tradeDao.createTrade(symbol, tradeType, quantity, price);

        // Assert
        List<Trade> trades = tradeDao.readTrades();
        assertEquals(1, trades.size());
        assertEquals(0, trades.get(0).getQuantity());
    }

    @Test
    void testCreateTrade_WithNegativeQuantity_Success() {
        // Arrange - Testing edge case
        String symbol = "NEG";
        String tradeType = "SELL";
        int quantity = -5;
        BigDecimal price = new BigDecimal("50.00");

        // Act
        tradeDao.createTrade(symbol, tradeType, quantity, price);

        // Assert
        List<Trade> trades = tradeDao.readTrades();
        assertEquals(1, trades.size());
        assertEquals(-5, trades.get(0).getQuantity());
    }
}
