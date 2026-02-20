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
        assertEquals(0, trade.getId());
        assertNull(trade.getSymbol());
        assertNull(trade.getTradeType());
        assertEquals(0, trade.getQuantity());
        assertNull(trade.getPrice());
        assertNull(trade.getTradeDate());
    }

    @Test
    void testParameterizedConstructor() {
        BigDecimal price = new BigDecimal("100.50");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Trade trade = new Trade(1, "AAPL", "BUY", 50, price, timestamp);
        
        assertEquals(1, trade.getId());
        assertEquals("AAPL", trade.getSymbol());
        assertEquals("BUY", trade.getTradeType());
        assertEquals(50, trade.getQuantity());
        assertEquals(price, trade.getPrice());
        assertEquals(timestamp, trade.getTradeDate());
    }

    @Test
    void testSettersAndGetters() {
        Trade trade = new Trade();
        BigDecimal price = new BigDecimal("200.75");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        trade.setId(2);
        trade.setSymbol("GOOGL");
        trade.setTradeType("SELL");
        trade.setQuantity(100);
        trade.setPrice(price);
        trade.setTradeDate(timestamp);
        
        assertEquals(2, trade.getId());
        assertEquals("GOOGL", trade.getSymbol());
        assertEquals("SELL", trade.getTradeType());
        assertEquals(100, trade.getQuantity());
        assertEquals(price, trade.getPrice());
        assertEquals(timestamp, trade.getTradeDate());
    }

    @Test
    void testToString() {
        BigDecimal price = new BigDecimal("150.00");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Trade trade = new Trade(3, "MSFT", "BUY", 75, price, timestamp);
        
        String result = trade.toString();
        
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("symbol='MSFT'"));
        assertTrue(result.contains("tradeType='BUY'"));
        assertTrue(result.contains("quantity=75"));
        assertTrue(result.contains("price=150.00"));
        assertTrue(result.contains("tradeDate="));
    }

    @Test
    void testSetPriceWithNull() {
        Trade trade = new Trade();
        trade.setPrice(null);
        assertNull(trade.getPrice());
    }

    @Test
    void testSetSymbolWithNull() {
        Trade trade = new Trade();
        trade.setSymbol(null);
        assertNull(trade.getSymbol());
    }

    @Test
    void testSetTradeDateWithNull() {
        Trade trade = new Trade();
        trade.setTradeDate(null);
        assertNull(trade.getTradeDate());
    }
}
