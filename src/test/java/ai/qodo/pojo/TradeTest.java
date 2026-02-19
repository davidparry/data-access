package ai.qodo.pojo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class TradeTest {

    @Test
    void testDefaultConstructor() {
        Trade trade = new Trade();
        assertNotNull(trade);
    }

    @Test
    void testParameterizedConstructor() {
        BigDecimal price = new BigDecimal("150.50");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Trade trade = new Trade(1, "AAPL", "BUY", 100, price, now);

        assertEquals(1, trade.getId());
        assertEquals("AAPL", trade.getSymbol());
        assertEquals("BUY", trade.getTradeType());
        assertEquals(100, trade.getQuantity());
        assertEquals(price, trade.getPrice());
        assertEquals(now, trade.getTradeDate());
    }

    @Test
    void testGettersAndSetters() {
        Trade trade = new Trade();
        BigDecimal price = new BigDecimal("2800.75");
        Timestamp now = new Timestamp(System.currentTimeMillis());

        trade.setId(2);
        trade.setSymbol("GOOGL");
        trade.setTradeType("SELL");
        trade.setQuantity(50);
        trade.setPrice(price);
        trade.setTradeDate(now);

        assertEquals(2, trade.getId());
        assertEquals("GOOGL", trade.getSymbol());
        assertEquals("SELL", trade.getTradeType());
        assertEquals(50, trade.getQuantity());
        assertEquals(price, trade.getPrice());
        assertEquals(now, trade.getTradeDate());
    }

    @Test
    void testToString() {
        BigDecimal price = new BigDecimal("150.50");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Trade trade = new Trade(1, "AAPL", "BUY", 100, price, now);

        String result = trade.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("symbol='AAPL'"));
        assertTrue(result.contains("tradeType='BUY'"));
        assertTrue(result.contains("quantity=100"));
        assertTrue(result.contains("price=150.50"));
        assertTrue(result.contains("tradeDate=" + now));
    }

    @Test
    void testSettersWithNullValues() {
        Trade trade = new Trade();
        
        trade.setSymbol(null);
        trade.setTradeType(null);
        trade.setPrice(null);
        trade.setTradeDate(null);

        assertNull(trade.getSymbol());
        assertNull(trade.getTradeType());
        assertNull(trade.getPrice());
        assertNull(trade.getTradeDate());
    }

    @Test
    void testSettersWithBoundaryValues() {
        Trade trade = new Trade();
        
        trade.setId(Integer.MAX_VALUE);
        trade.setQuantity(Integer.MIN_VALUE);

        assertEquals(Integer.MAX_VALUE, trade.getId());
        assertEquals(Integer.MIN_VALUE, trade.getQuantity());
    }
}
