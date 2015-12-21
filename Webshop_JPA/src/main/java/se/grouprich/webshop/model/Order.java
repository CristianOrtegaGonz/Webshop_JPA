package se.grouprich.webshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Order extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 3380539865925002167L;
	private User user;
	private List<OrderRow> orderRows;
	private double totalPrice;
	private boolean isPayed;

	public Order()
	{
	}

	public Order(User user)
	{
		this.user = user;
		orderRows = new ArrayList<>();
		isPayed = false;
	}

	public User getUser()
	{
		return user;
	}

	public List<OrderRow> getOrderRows()
	{
		return orderRows;
	}

	public double getTotalPrice()
	{
		return totalPrice;
	}

	public boolean isPayed()
	{
		return isPayed;
	}

	public void addOrderRow(OrderRow orderRow)
	{
		orderRows.add(orderRow);
	}

	public void calculateTotalPrice()
	{
		double totalPrice = 0;
		for (OrderRow orderRow : orderRows)
		{
			totalPrice += orderRow.getPricePerOrderRow();
		}
		this.totalPrice = totalPrice;
	}

	// public void pay() throws PaymentException
	// {
	// isPayed = true;
	// for (Product product : orderRows.getProducts())
	// {
	// product.setStockQuantity(product.getStockQuantity() -
	// product.getOrderQuantity());
	// }
	// }

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (other instanceof Order)
		{
			Order otherOrder = (Order) other;
			return user.equals(otherOrder.user) && orderRows.equals(otherOrder.orderRows);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += user.hashCode() * 37;
		result += orderRows.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "Order [user=" + user + ", orderRows=" + orderRows + ", isPayed=" + isPayed + "]";
	}
}
