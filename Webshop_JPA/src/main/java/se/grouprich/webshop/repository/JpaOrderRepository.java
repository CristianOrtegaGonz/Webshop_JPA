package se.grouprich.webshop.repository;

import static java.util.function.Function.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.OrderStatus;

public final class JpaOrderRepository extends AbstractJpaRepository<Order> implements OrderRepository
{

	public JpaOrderRepository(EntityManagerFactory factory)
	{
		super(factory, Order.class);
	}

	@Override
	public List<Order> fetchAll()
	{
		return fetchMany("Order.FetchAll", identity());
	}

	@Override
	public List<Order> fetchOrdersByUser(User user)
	{
		return fetchMany("Order.FetchOrdersByUser", queryFunction -> queryFunction.setParameter("id", user.getId()));
	}

	@Override
	public List<Order> fetchOrdersByStatus(OrderStatus status)
	{
		return fetchMany("Order.FetchOrdersByStatus", queryFunction -> queryFunction.setParameter("status", status));
	}

	@Override
	public List<Order> fetchOrdersByMinimumValue(Double minimumValue)
	{
		return fetchMany("Order.FetchOrdersByMinimumValue", queryFunction -> queryFunction.setParameter("totalPrice", minimumValue));
	}
}
