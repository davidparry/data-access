package ai.qodo.dao;

import ai.qodo.pojo.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderDaoIntegrationTest {

    @Autowired
    private OrderDao orderDao;

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
        jdbcTemplate.execute("DELETE FROM orders");
    }

    @Test
    void testCreateAndReadOrder() {
        orderDao.createOrder("ORD-001", 100);

        List<Order> orders = orderDao.readOrders();

        assertFalse(orders.isEmpty());
        Order order = orders.get(0);
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(100, order.getCustomerId());
        assertNotNull(order.getOrderDate());
    }

    @Test
    void testUpdateOrder() {
        orderDao.createOrder("ORD-002", 200);
        List<Order> orders = orderDao.readOrders();
        int orderId = orders.get(0).getId();

        orderDao.updateOrder(orderId, "ORD-002-UPDATED", 250);

        orders = orderDao.readOrders();
        Order updatedOrder = orders.stream()
            .filter(o -> o.getId() == orderId)
            .findFirst()
            .orElse(null);

        assertNotNull(updatedOrder);
        assertEquals("ORD-002-UPDATED", updatedOrder.getOrderNumber());
        assertEquals(250, updatedOrder.getCustomerId());
    }

    @Test
    void testReadOrders_MultipleOrders() {
        orderDao.createOrder("ORD-001", 100);
        orderDao.createOrder("ORD-002", 200);
        orderDao.createOrder("ORD-003", 300);

        List<Order> orders = orderDao.readOrders();

        assertEquals(3, orders.size());
    }
}
