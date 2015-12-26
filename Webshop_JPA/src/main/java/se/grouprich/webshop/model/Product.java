package se.grouprich.webshop.model;

import java.io.Serializable;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.ProductRegistrationException;
import se.grouprich.webshop.idgenerator.Identifiable;

public final class Product implements Serializable, Identifiable<String>
{
	private static final long serialVersionUID = 5072511887999675702L;
	private String productId;
	private String productName;
	private double price;
	private int stockQuantity;
	private int orderQuantity;

	/*Vad är orderQuantity till för? Menar ni det antal produkter en order har? I så fall kan vi väl ta bort den här
	* och ha den som ligger på order... */

	public Product(String productId, String productName, double price, int stockQuantity) throws ProductRegistrationException
	{
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.stockQuantity = stockQuantity;
	}

	/* Lite osäker på varför ha med stockquantity när man skapar en produkt. Man vill ju kunna skapa en produkt som ännu ej finns i lager t ex.
	Sen är det frågan om det hör hemma i denna modellklass som beskriver produkten. Vi har ju ännu inga krav på att hantera lager menar jag.
	Vill bara förstå, hoppas att mina kommentarer tas på rätt sätt!
	 */

	@Override
	public String getId()
	{
		return productId;
	}

	@Override
	public void setId(final String productId)
	{
		this.productId = productId;
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

	public void setStockQuantity(final int stockQuantity)
	{
		this.stockQuantity = stockQuantity;
	}

	public void setOrderQuantity(final int orderQuantity) throws OrderException
	{
		this.orderQuantity = orderQuantity;
	}

	public void addOrderQuantity(final int orderQuantity) throws OrderException
	{
		this.orderQuantity += orderQuantity;
	}

	public int getOrderQuantity()
	{
		return orderQuantity;
	}

	public void setPrice(final double price)
	{
		this.price = price;
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
			return productId.equals(otherProduct.productId) && productName.equals(otherProduct.productName) && price == otherProduct.price;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += productId.hashCode() * 37;
		result += productName.hashCode() * 37;
		result += price * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return productName + "(" + productId + ") ----"
				+ " Price: " + price + " kr ---- Order quantity: " + orderQuantity + " Stock quantity: " + stockQuantity;
	}
}
