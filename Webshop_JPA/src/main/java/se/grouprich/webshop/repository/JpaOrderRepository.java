package se.grouprich.webshop.repository;

import static java.util.function.Function.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.OrderStatus;

public final class JpaOrderRepository extends AbstractJpaRepository<Order> implements OrderRepository
{
	public JpaOrderRepository(final EntityManagerFactory factory)
	{
		super(factory, Order.class);
	}

	@Override
	public Order findById(final Long id)
	{
		List<Order> ordersFetchedById = fetchMany("Order.FindById", queryFunction -> queryFunction.setParameter("id", id));
		if (ordersFetchedById.isEmpty())
		{
			return null;
		}	
		return ordersFetchedById.get(0);
	}

	@Override
	public Order saveOrUpdate(final Order order)
	{
		return merge(order);
	}

	@Override
	public List<Order> fetchAll()
	{
		return fetchMany("Order.FetchAll", identity());
	}

	@Override
	public List<Order> fetchOrdersByUser(final User user)
	{
		return fetchMany("Order.FetchOrdersByUser", queryFunction -> queryFunction.setParameter("id", user.getId()));
	}

	@Override
	public List<Order> fetchOrdersByStatus(final OrderStatus status)
	{
		return fetchMany("Order.FetchOrdersByStatus", queryFunction -> queryFunction.setParameter("status", status));
	}

	@Override
	public List<Order> fetchOrdersByMinimumValue(final Double minimumValue)
	{
		return fetchMany("Order.FetchOrdersByMinimumValue", queryFunction -> queryFunction.setParameter("totalPrice", minimumValue));
	}
}
