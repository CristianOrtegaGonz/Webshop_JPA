package se.grouprich.webshop.repository;

import java.util.List;

import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.OrderStatus;

public interface OrderRepository extends CrudRepository<Order>
{
	Order fetchById(Long id);
	List<Order> fetchAll();
	List<Order> fetchOrdersByUser(final User user);
	List<Order> fetchOrdersByStatus(final OrderStatus status);
	List<Order> fetchOrdersByMinimumValue(final Double minimumValue);
}
