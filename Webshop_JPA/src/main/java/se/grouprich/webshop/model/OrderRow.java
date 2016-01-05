package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class OrderRow extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 3865658878665558979L;

	@OneToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(nullable = false)
	private Product product;

	@Column(nullable = false)
	private Integer orderQuantity;

	public OrderRow()
	{
	}

	public OrderRow(Product product)
	{
		this.product = product;
		orderQuantity = 1;
	}

	public OrderRow(Product product, Integer orderQuantity)
	{
		this.product = product;
		this.orderQuantity = orderQuantity;
	}

	public Product getProduct()
	{
		return product;
	}

	public int getOrderQuantity()
	{
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity)
	{
		this.orderQuantity = orderQuantity;
	}

	public void updateStockQuantity(int orderQuantity)
	{
		int stockQuantity = product.getStockQuantity();
		product.setStockQuantity(stockQuantity - orderQuantity);
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (other instanceof OrderRow)
		{
			OrderRow otherOrderRow = (OrderRow) other;
			return product.equals(otherOrderRow.product) && orderQuantity == otherOrderRow.orderQuantity;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += product.hashCode() * 37;
		result += orderQuantity.hashCode() * 37;
		return result;
	}

	@Override
	public String toString()
	{
		return "OrderRow [product=" + product.getProductName() + ", orderQuantity=" + orderQuantity + "]";
	}
}
