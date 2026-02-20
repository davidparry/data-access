package ai.qodo.dao;

import ai.qodo.pojo.Trade;
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
class TradeDaoIntegrationTest {

    @Autowired
    private TradeDao tradeDao;

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
        jdbcTemplate.execute("DELETE FROM trade");
    }

    @Test
    void testCreateAndReadTrade() {
        tradeDao.createTrade("AAPL", "BUY", 100, new BigDecimal("150.50"));

        List<Trade> trades = tradeDao.readTrades();

        assertFalse(trades.isEmpty());
        Trade trade = trades.get(0);
        assertEquals("AAPL", trade.getSymbol());
        assertEquals("BUY", trade.getTradeType());
        assertEquals(100, trade.getQuantity());
        assertEquals(new BigDecimal("150.50"), trade.getPrice());
        assertNotNull(trade.getTradeDate());
    }

    @Test
    void testUpdateTrade() {
        tradeDao.createTrade("GOOGL", "SELL", 50, new BigDecimal("200.00"));
        List<Trade> trades = tradeDao.readTrades();
        int tradeId = trades.get(0).getId();

        tradeDao.updateTrade(tradeId, "GOOGL", "BUY", 75, new BigDecimal("250.00"));

        trades = tradeDao.readTrades();
        Trade updatedTrade = trades.stream()
            .filter(t -> t.getId() == tradeId)
            .findFirst()
            .orElse(null);

        assertNotNull(updatedTrade);
        assertEquals("BUY", updatedTrade.getTradeType());
        assertEquals(75, updatedTrade.getQuantity());
        assertEquals(new BigDecimal("250.00"), updatedTrade.getPrice());
    }

    @Test
    void testReadTrades_MultipleTrades() {
        tradeDao.createTrade("AAPL", "BUY", 100, new BigDecimal("150.50"));
        tradeDao.createTrade("GOOGL", "SELL", 50, new BigDecimal("200.75"));
        tradeDao.createTrade("MSFT", "BUY", 75, new BigDecimal("300.00"));

        List<Trade> trades = tradeDao.readTrades();

        assertEquals(3, trades.size());
    }
}
