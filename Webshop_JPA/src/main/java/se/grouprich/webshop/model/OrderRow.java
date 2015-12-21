package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class OrderRow extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 3865658878665558979L;
	private Product product;
	private int quantity;
	private double pricePerOrderRow;

	public OrderRow()
	{
	}

	public OrderRow(Product product)
	{
		this.product = product;
		quantity = 1;
		pricePerOrderRow = product.getPrice();
	}

	public OrderRow(Product product, int quantity)
	{
		this.product = product;
		this.quantity = quantity;
		pricePerOrderRow = product.getPrice() * quantity;
	}

	public Product getProduct()
	{
		return product;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public double getPricePerOrderRow()
	{
		return pricePerOrderRow;
	}

	public void setTotalPrice(final double pricePerOrderRow)
	{
		this.pricePerOrderRow = pricePerOrderRow;
	}

	// public double calculateTotalPrice()
	// {
	// totalPrice = 0;
	// for (Product product : products)
	// {
	// totalPrice += (product.getPrice() * product.getOrderQuantity());
	// }
	// return totalPrice;
	// }
	//
	// public void addProduct(final Product product, final int orderQuantity)
	// throws OrderException
	// {
	// products.add(product);
	// product.setOrderQuantity(orderQuantity);
	// calculateTotalPrice();
	// }

	// public void deleteOneProduct(final Product product) throws OrderException
	// {
	// if (!products.contains(product))
	// {
	// throw new OrderException("Product doesn't exsists.");
	// }
	// products.remove(product);
	// calculateTotalPrice();
	// }
	//
	// public void emptyShoppingCart(final List<Product> products)
	// {
	// products.removeAll(products);
	// calculateTotalPrice();
	// }

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
			return product.equals(otherOrderRow.product) && quantity == otherOrderRow.quantity && pricePerOrderRow == otherOrderRow.pricePerOrderRow;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += product.hashCode() * 37;
		result += quantity * 37;
		result += pricePerOrderRow * 37;
		return result;
	}

	@Override
	public String toString()
	{
		return "OrderRow [product=" + product + ", quantity=" + quantity + ", totalPrice=" + pricePerOrderRow + "]";
	}
}
