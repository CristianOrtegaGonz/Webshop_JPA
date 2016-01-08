package se.grouprich.webshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import se.grouprich.webshop.model.status.OrderStatus;

@Entity
@Table(name = "`Order`")
@NamedQueries(value = { @NamedQuery(name = "Order.FetchAll", query = "SELECT o FROM Order o"),
		@NamedQuery(name = "Order.FetchOrdersByUser", query = "SELECT o FROM Order o WHERE o.customer.id = :id"),
		@NamedQuery(name = "Order.FetchOrdersByStatus", query = "SELECT o FROM Order o WHERE o.status = :status"),
		@NamedQuery(name = "Order.FetchOrdersByMinimumValue", query = "SELECT o FROM Order o WHERE o.totalPrice >= :totalPrice") })

// Behöver vi implementera Serializable? Jag läste att det är "best practice"
// att ha den enligt den här sidan.
// https://bvaisakh.wordpress.com/2015/03/04/do-jpa-entities-have-to-be-serializable/
// Men just nu behöver vi kanske inte den så jag vet inte vad vi ska göra. Vi kan fråga Anders.
public class Order extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 3380539865925002167L;

	@ManyToOne(cascade = { CascadeType.PERSIST })
	private User customer;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<OrderRow> orderRows;

	@Column(nullable = false)
	private Double totalPrice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	public Order()
	{
	}

	public Order(User customer, OrderRow... orderRows)
	{
		this.customer = customer;
		this.orderRows = new ArrayList<>();
		addOrderRows(orderRows);
		status = OrderStatus.PLACED;
	}

	public User getCustomer()
	{
		return customer;
	}

	public List<OrderRow> getOrderRows()
	{
		return orderRows;
	}

	public Double getTotalPrice()
	{
		return totalPrice;
	}

	public OrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(OrderStatus status)
	{
		this.status = status;
	}

	public Order addOrderRows(OrderRow... orderRows)
	{
		for (OrderRow orderRow : orderRows)
		{
			OrderRow orderRowWithExistingProduct = searchProductInOrderRows(orderRow);
			if (orderRowWithExistingProduct == null)
			{
				this.orderRows.add(orderRow);
			}
			else
			{
				addQuantity(orderRowWithExistingProduct, orderRow.getOrderQuantity());
			}
		}
		calculateTotalPrice();
		return this;
	}

	private void calculateTotalPrice()
	{
		Double totalPrice = 0.0;
		for (OrderRow orderRow : orderRows)
		{
			totalPrice += orderRow.getProduct().getPrice() * orderRow.getOrderQuantity();
		}
		BigDecimal bd = new BigDecimal(totalPrice);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		this.totalPrice = bd.doubleValue();
	}

	private void addQuantity(OrderRow orderRow, int quantity)
	{
		orderRow.setOrderQuantity(orderRow.getOrderQuantity() + quantity);
	}

	public void updateStockQuantities()
	{
		for (OrderRow orderRow : orderRows)
		{
			orderRow.updateStockQuantity(orderRow.getOrderQuantity());
		}
	}

	private OrderRow searchProductInOrderRows(OrderRow orderRow)
	{
		for (OrderRow orderRowInList : orderRows)
		{
			if (orderRowInList.getProduct().getId().equals(orderRow.getProduct().getId()))
			{
				return orderRowInList;
			}
		}
		return null;
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
			return customer.equals(otherOrder.customer) && orderRows.equals(otherOrder.orderRows) && totalPrice.equals(otherOrder.totalPrice)
					&& status.equals(otherOrder.status);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += customer.hashCode() * 37;
		result += orderRows.hashCode() * 37;
		result += totalPrice.hashCode() * 37;
		result += status.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "\n[" + getId() + ", " + customer.getUsername() + ", " + orderRows + ", " + totalPrice + ", " + status + "]";
	}
}
