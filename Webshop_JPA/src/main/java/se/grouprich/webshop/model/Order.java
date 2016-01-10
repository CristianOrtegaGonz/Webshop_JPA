package se.grouprich.webshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
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

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.PermissionException;
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
// Men just nu behöver vi kanske inte den så jag vet inte vad vi ska göra. Vi
// kan fråga Anders.
public class Order extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 3380539865925002167L;

	@ManyToOne(cascade = { CascadeType.PERSIST })
	private User customer;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<OrderRow> orderRows;

	@Column(nullable = false, columnDefinition = "DECIMAL(10,2) UNSIGNED")
	private Double totalPrice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	@Transient
	private List<OrderRow> addedOrderRows = new ArrayList<>();

	public Order()
	{
	}

	public Order(User customer, OrderRow... orderRows) throws OrderException, PermissionException
	{
		this.customer = customer;
		this.orderRows = new ArrayList<>();
		for (OrderRow orderRow : orderRows)
		{
			if (orderRow.getProduct().getId() == null)
			{
				throw new PermissionException("No permissioin to create product");
			}
		}
		addOrderRows(orderRows);
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

	public void setOrderRows(List<OrderRow> orderRows)
	{
		this.orderRows = orderRows;
	}

	public Order addOrderRows(OrderRow... orderRows) throws OrderException
	{
		addedOrderRows.addAll(Arrays.asList(orderRows));
		for (OrderRow orderRow : orderRows)
		{
			OrderRow orderRowAlreadyHasProduct = searchProductInOrder(orderRow);

			Integer stockQuantity = orderRow.getProduct().getStockQuantity();
			Integer addedOrderQuantity = orderRow.getOrderQuantity();
			if (orderRowAlreadyHasProduct == null && stockQuantity >= addedOrderQuantity)
			{
				this.orderRows.add(orderRow);
			}
			else if (getId() == null && stockQuantity >= orderRowAlreadyHasProduct.getOrderQuantity() + addedOrderQuantity)
			{
				orderRowAlreadyHasProduct.setOrderQuantity(orderRowAlreadyHasProduct.getOrderQuantity() + addedOrderQuantity);
			}
			else if (getId() != null && stockQuantity >= addedOrderQuantity)
			{
				orderRowAlreadyHasProduct.getProduct().setStockQuantity(stockQuantity + orderRowAlreadyHasProduct.getOrderQuantity());
				orderRowAlreadyHasProduct.setOrderQuantity(orderRowAlreadyHasProduct.getOrderQuantity() + addedOrderQuantity);
			}
			else
			{
				throw new OrderException(
						orderRow.getProduct().getProductName() + ": order quantity is " + (orderRowAlreadyHasProduct.getOrderQuantity() + addedOrderQuantity)
								+ " but stock quantity is " + stockQuantity);
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

	public void updateStockQuantities(List<OrderRow> orderRows)
	{
		for (OrderRow orderRow : orderRows)
		{
			for (OrderRow addedOrderRow : addedOrderRows)
			{
				if (orderRow.getProduct().equals(
						addedOrderRow.getProduct()) || orderRow.getProduct().getId().equals(
								addedOrderRow.getProduct().getId()))
				{
					orderRow.updateStockQuantity(orderRow.getOrderQuantity());
				}
				else
				{
					continue;
				}
			}
		}
	}

	public OrderRow searchProductInOrder(OrderRow orderRow)
	{
		for (OrderRow orderRowInList : orderRows)
		{
			if (orderRowInList.getProduct().getId().equals(orderRow.getProduct().getId())
					|| orderRowInList.getProduct().equals(orderRow.getProduct()))
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
		return "Order [id: " + getId() + ", username: " + customer.getUsername() + ", orderRows: " + orderRows + ", totalPrice: " + totalPrice + ", status: " + status
				+ "]";
	}
}
