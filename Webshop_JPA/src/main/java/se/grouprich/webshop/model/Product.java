package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import se.grouprich.webshop.exception.StorageException;
import se.grouprich.webshop.model.status.ProductStatus;

@Entity
@NamedQueries(value = { @NamedQuery(name = "Product.FetchAll", query = "SELECT p FROM Product p"),
		@NamedQuery(name = "Product.SearchProductsBasedOnProductName", query = "SELECT p FROM Product p WHERE p.productName LIKE ?1 ORDER BY p.productName ASC"),
		@NamedQuery(name = "Product.FetchProductsByProductName", query = "SELECT p FROM Product p WHERE p.productName = :productName") })
public class Product extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 5072511887999675702L;

	@Column(nullable = false)
	private String productName;

	@Column(nullable = false)
	private Double price;

	@Column(nullable = false)
	private int stockQuantity;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
	
	// private int orderQuantity;

	protected Product()
	{
	}

	public Product(String productName, Double price, int stockQuantity, ProductStatus status) throws StorageException
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

	public Double getPrice()
	{
		return price;
	}
	
	public ProductStatus getStatus()
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

	public void setPrice(final Double price)
	{
		this.price = price;
	}

	public void setStatus(ProductStatus status)
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
			return productName.equals(otherProduct.productName) && price.equals(otherProduct.price);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += productName.hashCode() * 37;
		result += price.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "Product [productName=" + productName + ", price=" + price + ", stockQuantity=" + stockQuantity + ", status=" + status + "]";
	}
}
