package ai.qodo.pojo;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Timestamp;
import org.junit.jupiter.api.Test;

public class OrderTest {


  // Create Order object with valid constructor parameters
  @Test
  public void test_create_order_with_valid_parameters() {
    int id = 1;
    String orderNumber = "ORD-001";
    int customerId = 100;
    Timestamp orderDate = new Timestamp(System.currentTimeMillis());

    Order order = new Order(id, orderNumber, customerId, orderDate);

    assertEquals(id, order.getId());
    assertEquals(orderNumber, order.getOrderNumber());
    assertEquals(customerId, order.getCustomerId());
    assertEquals(orderDate, order.getOrderDate());
  }

  // Set null values for orderNumber and orderDate fields
  @Test
  public void test_set_null_values_for_order_fields() {
    Order order = new Order();
    order.setOrderNumber(null);
    order.setOrderDate(null);

    assertNull(order.getOrderNumber());
    assertNull(order.getOrderDate());
  }

  // Set negative values for id and customerId
  @Test
  public void test_set_negative_values_for_ids() {
    Order order = new Order();
    order.setId(-1);
    order.setCustomerId(-100);

    assertEquals(-1, order.getId());
    assertEquals(-100, order.getCustomerId());
  }
}