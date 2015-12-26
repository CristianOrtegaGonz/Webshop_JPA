package se.grouprich.webshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.grouprich.webshop.exception.OrderException;

public final class OrderRow implements Serializable
{
	private static final long serialVersionUID = 3865658878665558979L;
	private Product product;
	private int quantity;
	private double orderRowPrice;



        public OrderRow(Product product, int quantity)
        {
            this.product = product;
            this.quantity = quantity;
        }

        public OrderRow(Product product)
        {
            this.product = product;
            this.quantity = 1;
        }

        public Product getItem() {
            return product;
        }

        public void setItem(Product item) {
            this.product = item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void decreaseQuantity(int quantity) {
            this.quantity = this.quantity - quantity;
        }

		public void increaseQuantity(int quantity) {
			this.quantity = this.quantity + quantity;
	}
    }

	public double calculateOrderRowPrice(OrderRow orderRow)
	{
		orderRowPrice = 0.0;
			orderRowPrice += (orderRow.getItem().getPrice() * orderRow.getQuantity());
		return orderRowPrice;
	}

	public double getOrderRowPrice()
	{
		return orderRowPrice;
	}
	
	public void setTotalPrice(final double orderRowPrice)
	{
		this.orderRowPrice = orderRowPrice;
	}


	/* Jag förstår inte värdet av att implementera equals och haschcode här. När spelar det roll om en orderrad är lik en annan?
	10 000 users ska ju kunna ha exakt samma orderrad och det är ju inte relevant. Det vi inte vill är att samma user har flera
	lika ordrar eller att det finns flera lika users eller flera lika products. Däremot vill vi ju inte ha flera ordrerrader
	som är samma på en en order. T ex vill vi ju bara ha en rad med: 1 dator quantity 1 och om user lägger till en till dator vill
	vi ju bara att quantity ska öka till 2. Fortfarande bara en rad, men det kollar vi lättast i addToOrder tror jag. Är ni med på
	hur jag menar?

	Samma med toString() tror jag i alla fall. En orderrad vet ju inte vilken order den tillhör, så det är nog aldrig relevant
	att skriva ut en orderrad. Om man vill skriva ut en ordeerrad i en order så måste vi vet vilkeen order och det vet vi i
	orderklassen..

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (other instanceof ShoppingCart)
		{
			ShoppingCart otherShoppingCart = (ShoppingCart) other;
			return products.equals(otherShoppingCart.products) && totalPrice == otherShoppingCart.totalPrice;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = 1;
		result += products.hashCode() * 37;
		result += totalPrice *37;
		return result;
	}


	@Override
	public String toString()
	{
		return "ShoppingCart [products=" + products + ", totalPrice=" + totalPrice + "]";
	}

	*/
}
