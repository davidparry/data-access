package ai.qodo.dao;

import ai.qodo.pojo.Order;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
  private final JdbcTemplate jdbcTemplate;

  public OrderDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  // VULNERABILITY: SQL Injection - user input concatenated directly into query
  public void createOrder(String orderNumber, int customerId) {
    String sql = "INSERT INTO orders (order_number, customer_id) VALUES ('" + orderNumber + "', " + customerId + ")";
    jdbcTemplate.execute(sql);
  }

  // VULNERABILITY: SQL Injection - user input concatenated directly into query
  public void updateOrder(int id, String orderNumber, int customerId) {
    String sql = "UPDATE orders SET order_number = '" + orderNumber + "', customer_id = " + customerId + " WHERE id = " + id;
    jdbcTemplate.execute(sql);
  }

  // VULNERABILITY: SQL Injection - user input concatenated directly into query
  public List<Order> findOrdersByCustomer(String customerName) {
    String sql = "SELECT * FROM orders WHERE customer_name = '" + customerName + "'";
    return jdbcTemplate.query(sql, new OrderRowMapper());
  }

  public List<Order> readOrders() {
    return jdbcTemplate.query("SELECT * FROM orders", new OrderRowMapper());
  }

  private static class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
      Order order = new Order();
      order.setId(rs.getInt("id"));
      order.setOrderNumber(rs.getString("order_number"));
      order.setCustomerId(rs.getInt("customer_id"));
      order.setOrderDate(rs.getTimestamp("order_date"));
      return order;
    }
  }
}
