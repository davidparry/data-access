package ai.qodo.dao;

import ai.qodo.pojo.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void testCreateOrder_ShouldExecuteInsertQuery() {
        String orderNumber = "ORD-001";
        int customerId = 100;

        orderDao.createOrder(orderNumber, customerId);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), eq(orderNumber), eq(customerId));
        
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("INSERT INTO orders"));
        assertTrue(capturedSql.contains("order_number"));
        assertTrue(capturedSql.contains("customer_id"));
    }

    @Test
    void testUpdateOrder_ShouldExecuteUpdateQuery() {
        int id = 1;
        String orderNumber = "ORD-002";
        int customerId = 200;

        orderDao.updateOrder(id, orderNumber, customerId);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).update(sqlCaptor.capture(), eq(orderNumber), eq(customerId), eq(id));
        
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("UPDATE orders"));
        assertTrue(capturedSql.contains("SET"));
        assertTrue(capturedSql.contains("WHERE id = ?"));
    }

    @Test
    void testReadOrders_ShouldReturnListOfOrders() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Order order1 = new Order(1, "ORD-001", 100, timestamp);
        Order order2 = new Order(2, "ORD-002", 200, timestamp);
        List<Order> expectedOrders = Arrays.asList(order1, order2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedOrders);

        List<Order> result = orderDao.readOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORD-001", result.get(0).getOrderNumber());
        assertEquals("ORD-002", result.get(1).getOrderNumber());
        
        verify(jdbcTemplate).query(eq("SELECT * FROM orders"), any(RowMapper.class));
    }

    @Test
    void testReadOrders_ShouldReturnEmptyListWhenNoOrders() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        List<Order> result = orderDao.readOrders();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateOrder_WithNullOrderNumber() {
        orderDao.createOrder(null, 100);

        verify(jdbcTemplate).update(anyString(), isNull(), eq(100));
    }
}
