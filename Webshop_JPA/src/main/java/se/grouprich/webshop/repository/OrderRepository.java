package se.grouprich.webshop.repository;

import java.util.List;

import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.User;

public interface OrderRepository extends CrudRepository<Order>
{
	List<Order> fetchOrdersByUser(User user);
	List<Order> fetchOrdersByStatus(String status);
	List<Order> fetchOrdersByMinimumValue(double minumumValue);
	Order changeStatus(String status);
}
