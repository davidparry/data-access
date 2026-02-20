package ai.qodo.pojo;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testDefaultConstructor() {
        Order order = new Order();
        assertNotNull(order);
        assertEquals(0, order.getId());
        assertNull(order.getOrderNumber());
        assertEquals(0, order.getCustomerId());
        assertNull(order.getOrderDate());
    }

    @Test
    void testParameterizedConstructor() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Order order = new Order(1, "ORD-001", 100, timestamp);
        
        assertEquals(1, order.getId());
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(100, order.getCustomerId());
        assertEquals(timestamp, order.getOrderDate());
    }

    @Test
    void testSettersAndGetters() {
        Order order = new Order();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        order.setId(2);
        order.setOrderNumber("ORD-002");
        order.setCustomerId(200);
        order.setOrderDate(timestamp);
        
        assertEquals(2, order.getId());
        assertEquals("ORD-002", order.getOrderNumber());
        assertEquals(200, order.getCustomerId());
        assertEquals(timestamp, order.getOrderDate());
    }

    @Test
    void testToString() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Order order = new Order(3, "ORD-003", 300, timestamp);
        
        String result = order.toString();
        
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("orderNumber='ORD-003'"));
        assertTrue(result.contains("customerId=300"));
        assertTrue(result.contains("orderDate="));
    }

    @Test
    void testSetOrderNumberWithNull() {
        Order order = new Order();
        order.setOrderNumber(null);
        assertNull(order.getOrderNumber());
    }

    @Test
    void testSetOrderDateWithNull() {
        Order order = new Order();
        order.setOrderDate(null);
        assertNull(order.getOrderDate());
    }
}
