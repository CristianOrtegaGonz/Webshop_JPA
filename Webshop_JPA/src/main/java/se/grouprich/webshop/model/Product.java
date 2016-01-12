package se.grouprich.webshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

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

	@Column(nullable = false, columnDefinition = "DECIMAL(10,2) UNSIGNED")
	private Double price;

	@Column(nullable = false, columnDefinition = "INT(5) UNSIGNED")
	private Integer stockQuantity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductStatus status;

	protected Product()
	{
	}

	public Product(final String productName, final Double price, final Integer stockQuantity) throws StorageException
	{
		this.productName = productName;
		roundPrice(price);
		this.stockQuantity = stockQuantity;
		status = ProductStatus.NOT_PUBLISHED;
	}

	public String getProductName()
	{
		return productName;
	}

	public Integer getStockQuantity()
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

	public void setProductName(final String productName)
	{
		this.productName = productName;
	}

	public void setStockQuantity(final Integer stockQuantity)
	{
		this.stockQuantity = stockQuantity;
	}

	public void setPrice(final Double price)
	{
		this.price = price;
	}

	public Product setStatus(final ProductStatus status)
	{
		this.status = status;
		return this;
	}

	public void roundPrice(final Double price)
	{
		BigDecimal bd = new BigDecimal(price);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		this.price = bd.doubleValue();
	}

	@Override
	public boolean equals(final Object other)
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
		return "Product [id: " + getId() + ", productName: " + productName + ", price: " + price + ", stockQuantity: " + stockQuantity + ", status: " + status + "]";
	}
}
