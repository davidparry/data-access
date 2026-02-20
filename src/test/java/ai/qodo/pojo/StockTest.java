package ai.qodo.pojo;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void testDefaultConstructor() {
        Stock stock = new Stock();
        assertNotNull(stock);
        assertEquals(0, stock.getId());
        assertNull(stock.getSymbol());
        assertNull(stock.getCompanyName());
        assertNull(stock.getCurrentPrice());
        assertEquals(0, stock.getQuantity());
    }

    @Test
    void testParameterizedConstructor() {
        BigDecimal price = new BigDecimal("150.50");
        Stock stock = new Stock(1, "AAPL", "Apple Inc.", price, 100);
        
        assertEquals(1, stock.getId());
        assertEquals("AAPL", stock.getSymbol());
        assertEquals("Apple Inc.", stock.getCompanyName());
        assertEquals(price, stock.getCurrentPrice());
        assertEquals(100, stock.getQuantity());
    }

    @Test
    void testSettersAndGetters() {
        Stock stock = new Stock();
        BigDecimal price = new BigDecimal("200.75");
        
        stock.setId(2);
        stock.setSymbol("GOOGL");
        stock.setCompanyName("Google LLC");
        stock.setCurrentPrice(price);
        stock.setQuantity(50);
        
        assertEquals(2, stock.getId());
        assertEquals("GOOGL", stock.getSymbol());
        assertEquals("Google LLC", stock.getCompanyName());
        assertEquals(price, stock.getCurrentPrice());
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void testToString() {
        BigDecimal price = new BigDecimal("100.00");
        Stock stock = new Stock(3, "MSFT", "Microsoft Corp", price, 75);
        
        String result = stock.toString();
        
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("symbol='MSFT'"));
        assertTrue(result.contains("companyName='Microsoft Corp'"));
        assertTrue(result.contains("currentPrice=100.00"));
        assertTrue(result.contains("quantity=75"));
    }

    @Test
    void testSetCurrentPriceWithNull() {
        Stock stock = new Stock();
        stock.setCurrentPrice(null);
        assertNull(stock.getCurrentPrice());
    }

    @Test
    void testSetSymbolWithNull() {
        Stock stock = new Stock();
        stock.setSymbol(null);
        assertNull(stock.getSymbol());
    }
}
