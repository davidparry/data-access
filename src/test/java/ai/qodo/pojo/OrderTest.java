package ai.qodo.pojo;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testDefaultConstructor() {
        Order order = new Order();
        assertNotNull(order);
    }

    @Test
    void testParameterizedConstructor() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = new Order(1, "ORD-001", 123, now);

        assertEquals(1, order.getId());
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(123, order.getCustomerId());
        assertEquals(now, order.getOrderDate());
    }

    @Test
    void testGettersAndSetters() {
        Order order = new Order();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        order.setId(1);
        order.setOrderNumber("ORD-001");
        order.setCustomerId(123);
        order.setOrderDate(now);

        assertEquals(1, order.getId());
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(123, order.getCustomerId());
        assertEquals(now, order.getOrderDate());
    }

    @Test
    void testToString() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = new Order(1, "ORD-001", 123, now);

        String result = order.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("orderNumber='ORD-001'"));
        assertTrue(result.contains("customerId=123"));
        assertTrue(result.contains("orderDate=" + now));
    }

    @Test
    void testSettersWithNullValues() {
        Order order = new Order();
        
        order.setOrderNumber(null);
        order.setOrderDate(null);

        assertNull(order.getOrderNumber());
        assertNull(order.getOrderDate());
    }

    @Test
    void testSettersWithBoundaryValues() {
        Order order = new Order();
        
        order.setId(Integer.MAX_VALUE);
        order.setCustomerId(Integer.MIN_VALUE);

        assertEquals(Integer.MAX_VALUE, order.getId());
        assertEquals(Integer.MIN_VALUE, order.getCustomerId());
    }
}
