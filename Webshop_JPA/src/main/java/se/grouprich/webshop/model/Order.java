package se.grouprich.webshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Order extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 3380539865925002167L;
	@Column(nullable = false)
	private User user;
	@Column(nullable = false)
	@OneToMany
	private List<OrderRow> orderRows;
	@Column(nullable = false)
	private double totalPrice;
	@Column(nullable = false)
	private String status;

	public Order()
	{
	}

	public Order(User user, String status)
	{
		this.user = user;
		orderRows = new ArrayList<>();
		status = "Placed";
		calculateTotalPrice();
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

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public void addOrderRow(OrderRow orderRow)
	{
		orderRows.add(orderRow);
		calculateTotalPrice();
	}

	public void calculateTotalPrice()
	{
		double totalPrice = 0;
		for (OrderRow orderRow : orderRows)
		{
			totalPrice += orderRow.getProduct().getPrice() * orderRow.getQuantity();
		}
		this.totalPrice = totalPrice;
	}

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
			return user.equals(otherOrder.user) && orderRows.equals(otherOrder.orderRows) && totalPrice == otherOrder.totalPrice
					&& status.equals(otherOrder.status);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += user.hashCode() * 37;
		result += orderRows.hashCode() * 37;
		result += totalPrice * 37;
		result += status.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "Order [user=" + user + ", orderRows=" + orderRows + ", totalPrice=" + totalPrice + ", status=" + status + "]";
	}
}
