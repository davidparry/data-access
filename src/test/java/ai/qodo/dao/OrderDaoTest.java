package ai.qodo.dao;

import ai.qodo.pojo.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private OrderDao orderDao;

    @Test
    void testCreateOrder() {
        // Arrange
        String orderNumber = "ORD-001";
        int customerId = 123;

        // Act
        orderDao.createOrder(orderNumber, customerId);

        // Assert
        verify(jdbcTemplate).update(
            eq("INSERT INTO orders (order_number, customer_id) VALUES (?, ?)"),
            eq(orderNumber),
            eq(customerId)
        );
    }

    @Test
    void testUpdateOrder() {
        // Arrange
        int id = 1;
        String orderNumber = "ORD-002";
        int customerId = 456;

        // Act
        orderDao.updateOrder(id, orderNumber, customerId);

        // Assert
        verify(jdbcTemplate).update(
            eq("UPDATE orders SET order_number = ?, customer_id = ? WHERE id = ?"),
            eq(orderNumber),
            eq(customerId),
            eq(id)
        );
    }

    @Test
    void testReadOrders() {
        // Arrange
        Order order1 = new Order(1, "ORD-001", 123, new Timestamp(System.currentTimeMillis()));
        Order order2 = new Order(2, "ORD-002", 456, new Timestamp(System.currentTimeMillis()));
        List<Order> expectedOrders = Arrays.asList(order1, order2);
        
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderDao.readOrders();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedOrders, result);
        verify(jdbcTemplate).query(eq("SELECT * FROM orders"), any(RowMapper.class));
    }

    @Test
    void testReadOrdersEmpty() {
        // Arrange
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        // Act
        List<Order> result = orderDao.readOrders();

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcTemplate).query(eq("SELECT * FROM orders"), any(RowMapper.class));
    }
}
