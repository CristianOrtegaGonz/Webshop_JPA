package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.model.status.ProductStatus;

// funkar det?
@Embeddable
//@Entity
public class OrderRow /*extends AbstractEntity*/ implements Serializable
{
	@Transient
	private static final long serialVersionUID = 3865658878665558979L;

	@OneToOne(cascade = { CascadeType.MERGE })
	private Product product;

	@Column(nullable = false)
	private Integer orderQuantity;
	
	public OrderRow()
	{
	}

	public OrderRow(Product product) throws OrderException
	{
		if (product.getStatus().equals(ProductStatus.OUT_OF_STOCK))
		{
			throw new OrderException("Product is out of stock");
		}
		this.product = product;
		orderQuantity = 1;
	}

	public OrderRow(Product product, Integer orderQuantity) throws OrderException
	{
		if (orderQuantity < 1)
		{
			throw new IllegalArgumentException("Order quantity must be greater than or equal to 1");
		}
		if (product.getStockQuantity() <= orderQuantity)
		{
			throw new OrderException("Stock quantity is " + product.getStockQuantity());
		}
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
		if (product.getStockQuantity() == 0)
		{
			product.setStatus(ProductStatus.OUT_OF_STOCK);
		}
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
