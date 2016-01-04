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

	@JoinColumn(nullable = false)
	@OneToOne(cascade = { CascadeType.MERGE })
	private Product product;

	@Column(nullable = false)
	private int quantity;

	public OrderRow()
	{
	}

	public OrderRow(Product product)
	{
		this.product = product;
		quantity = 1;
	}

	public OrderRow(Product product, int quantity)
	{
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct()
	{
		return product;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public void decreaseQuantity()
	{
		this.quantity = this.quantity - 1;
	}

	public void increaseQuantity()
	{
		this.quantity = this.quantity + 1;
	}

	// Vi har List<OrderRow> i Order klassen och ArrayList använder equals() för
	// t.ex. sin metod contains() så jag tycker att det är bra att ha
	// equals() och hashCode() i denna klassen.
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
			return product.equals(otherOrderRow.product) && quantity == otherOrderRow.quantity;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += product.hashCode() * 37;
		result += quantity * 37;
		return result;
	}

	@Override
	public String toString()
	{
		return "OrderRow [product=" + product.getProductName() + ", quantity=" + quantity + "]";
	}

	/*
	 * Jag förstår inte värdet av att implementera equals och haschcode här. När
	 * spelar det roll om en orderrad är lik en annan? 10 000 users ska ju kunna
	 * ha exakt samma orderrad och det är ju inte relevant. Det vi inte vill är
	 * att samma user har flera lika ordrar eller att det finns flera lika users
	 * eller flera lika products. Däremot vill vi ju inte ha flera ordrerrader
	 * som är samma på en en order. T ex vill vi ju bara ha en rad med: 1 dator
	 * quantity 1 och om user lägger till en till dator vill vi ju bara att
	 * quantity ska öka till 2. Fortfarande bara en rad, men det kollar vi
	 * lättast i addToOrder tror jag. Är ni med på hur jag menar?
	 * 
	 * Samma med toString() tror jag i alla fall. En orderrad vet ju inte vilken
	 * order den tillhör, så det är nog aldrig relevant att skriva ut en
	 * orderrad. Om man vill skriva ut en ordeerrad i en order så måste vi vet
	 * vilkeen order och det vet vi i orderklassen..
	 */
}
