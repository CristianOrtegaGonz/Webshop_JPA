package se.grouprich.webshop.repository;

import java.util.List;

import se.grouprich.webshop.ecommerceenum.OrderStatus;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.User;

public interface OrderRepository extends CrudRepository<Order>
{
	List<Order> fetchAll();
	List<Order> fetchOrdersByUser(User user);
	List<Order> fetchOrdersByStatus(OrderStatus status);
	List<Order> fetchOrdersByMinimumValue(double minimumValue);
}
