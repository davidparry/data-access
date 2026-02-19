package ai.qodo.pojo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void testDefaultConstructor() {
        Stock stock = new Stock();
        assertNotNull(stock);
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
    void testGettersAndSetters() {
        Stock stock = new Stock();
        BigDecimal price = new BigDecimal("2800.75");

        stock.setId(2);
        stock.setSymbol("GOOGL");
        stock.setCompanyName("Alphabet Inc.");
        stock.setCurrentPrice(price);
        stock.setQuantity(50);

        assertEquals(2, stock.getId());
        assertEquals("GOOGL", stock.getSymbol());
        assertEquals("Alphabet Inc.", stock.getCompanyName());
        assertEquals(price, stock.getCurrentPrice());
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void testToString() {
        BigDecimal price = new BigDecimal("150.50");
        Stock stock = new Stock(1, "AAPL", "Apple Inc.", price, 100);

        String result = stock.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("symbol='AAPL'"));
        assertTrue(result.contains("companyName='Apple Inc.'"));
        assertTrue(result.contains("currentPrice=150.50"));
        assertTrue(result.contains("quantity=100"));
    }

    @Test
    void testSettersWithNullValues() {
        Stock stock = new Stock();
        
        stock.setSymbol(null);
        stock.setCompanyName(null);
        stock.setCurrentPrice(null);

        assertNull(stock.getSymbol());
        assertNull(stock.getCompanyName());
        assertNull(stock.getCurrentPrice());
    }

    @Test
    void testSettersWithBoundaryValues() {
        Stock stock = new Stock();
        
        stock.setId(Integer.MAX_VALUE);
        stock.setQuantity(0);

        assertEquals(Integer.MAX_VALUE, stock.getId());
        assertEquals(0, stock.getQuantity());
    }

    @Test
    void testSettersWithNegativeQuantity() {
        Stock stock = new Stock();
        
        stock.setQuantity(-10);

        assertEquals(-10, stock.getQuantity());
    }
}
