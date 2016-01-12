package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.model.status.ProductStatus;

@Embeddable
public class OrderRow implements Serializable
{
	@Transient
	private static final long serialVersionUID = 3865658878665558979L;

	@OneToOne(cascade = { CascadeType.MERGE })
	private Product product;

	@Column(nullable = false, columnDefinition = "INT(5) UNSIGNED")
	private Integer orderQuantity;

	public OrderRow()
	{
	}

	public OrderRow(final Product product, final Integer orderQuantity) throws OrderException
	{
		if (!product.getStatus().equals(ProductStatus.AVAILABLE))
		{
			throw new OrderException("Product is not available");
		}
		if (orderQuantity < 1)
		{
			throw new IllegalArgumentException("Order quantity must be greater than or equal to 1");
		}
		if (orderQuantity > product.getStockQuantity())
		{
			throw new OrderException(product.getProductName() + ": order quantity is " + orderQuantity + " but stock quantity is " + product.getStockQuantity());
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

	public void setOrderQuantity(final Integer orderQuantity) throws OrderException
	{
		if (orderQuantity > product.getStockQuantity())
		{
			throw new OrderException(product.getProductName() + ": order quantity is " + orderQuantity +
					" but stock quantity is " + product.getStockQuantity());
		}
		this.orderQuantity = orderQuantity;
	}

	public void updateStockQuantity(final Integer orderQuantity)
	{
		Integer stockQuantity = product.getStockQuantity();
		product.setStockQuantity(stockQuantity - orderQuantity);
		if (product.getStockQuantity() <= 0)
		{
			product.setStatus(ProductStatus.NOT_AVAILABLE);
		}
	}

	@Override
	public boolean equals(final Object other)
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
		return "OrderRow [product: " + product.getProductName() + ", quantity: " + orderQuantity + "]";
	}
}
