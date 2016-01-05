package se.grouprich.webshop.repository;

import java.util.List;

import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.OrderStatus;

public interface OrderRepository extends CrudRepository<Order>
{
	List<Order> fetchAll();
	List<Order> fetchOrdersByUser(User user);
	List<Order> fetchOrdersByStatus(OrderStatus status);
	List<Order> fetchOrdersByMinimumValue(Double minimumValue);
}
