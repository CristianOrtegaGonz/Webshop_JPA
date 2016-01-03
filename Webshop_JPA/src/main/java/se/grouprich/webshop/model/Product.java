package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import se.grouprich.webshop.exception.ProductRegistrationException;

@Entity
@NamedQueries(value = { @NamedQuery(name = "Product.FetchAll", query = "SELECT p FROM Product p"),
		@NamedQuery(name = "Product.FetchProductsByProductName", query = "SELECT p FROM Product p WHERE p.productName = :productName") })
public class Product extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 5072511887999675702L;
	@Column(nullable = false)
	private String productName;
	@Column(nullable = false)
	private double price;
	@Column(nullable = false)
	private int stockQuantity;
	@Column(nullable = false)
	private String status;
	// private int orderQuantity;

	protected Product()
	{
	}

	public Product(String productName, double price, int stockQuantity, String status) throws ProductRegistrationException
	{
		this.productName = productName;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.status = status;
	}

	public String getProductName()
	{
		return productName;
	}

	public int getStockQuantity()
	{
		return stockQuantity;
	}

	public double getPrice()
	{
		return price;
	}

	public String getStatus()
	{
		return status;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public void setStockQuantity(final int stockQuantity)
	{
		this.stockQuantity = stockQuantity;
	}

	// public void setOrderQuantity(final int orderQuantity) throws
	// OrderException
	// {
	// this.orderQuantity = orderQuantity;
	// }
	//
	// public void addOrderQuantity(final int orderQuantity) throws
	// OrderException
	// {
	// this.orderQuantity += orderQuantity;
	// }
	//
	// public int getOrderQuantity()
	// {
	// return orderQuantity;
	// }

	public void setPrice(final double price)
	{
		this.price = price;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (other instanceof Product)
		{
			Product otherProduct = (Product) other;
			return productName.equals(otherProduct.productName) && price == otherProduct.price;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += productName.hashCode() * 37;
		result += price * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "Product [productName=" + productName + ", price=" + price + ", stockQuantity=" + stockQuantity + ", status=" + status + "]";
	}
}
