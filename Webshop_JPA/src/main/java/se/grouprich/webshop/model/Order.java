package se.grouprich.webshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.PaymentException;
import se.grouprich.webshop.idgenerator.Identifiable;

public final class Order implements Serializable, Identifiable<String>
{
	private static final long serialVersionUID = 3380539865925002167L;
	private String orderId;
	private final User shopper;
	private List<OrderRow> orderRows;
	private double orderTotal;
	private boolean isPayed;

	public Order(String orderId, User shopper, List<OrderRow> orderRows)
	{
		this.orderId = orderId;
		this.shopper = shopper;
		this.orderRows = orderRows;
		caluculateOrderPrice();
		isPayed = false;
	}

    public double getOrderTotal() {
        return orderTotal;
    }

    public void addToOrder(Product product, int quantity) {
        orderRows.add(new OrderRow(product, quantity));
    }

    public void addToOrder(Product product) {
        orderRows.add(new OrderRow(product));
    }

    public void addToOrder(List<OrderRow> orderRows) {
        this.orderRows = orderRows;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getShopperUserName() {
        return shopper.getUsername();
    }

    public User getOrderShopper() {
        return this.shopper;
    }

    public List<OrderRow> getOrderRows() {
        return new ArrayList<>(orderRows);
    }

	public double calculateTotalPrice()
	{
		totalPrice = 0;
		for (Product product : products)
		{
			totalPrice += (product.getPrice() * product.getOrderQuantity());
		}
		return totalPrice;
	}

	public void addProductInShoppingCart(final Product product, final int orderQuantity) throws OrderException
	{
		products.add(product);
		product.setOrderQuantity(orderQuantity);
		calculateTotalPrice();
	}

	public void deleteOneProduct(final Product product) throws OrderException
	{
		if (!products.contains(product))
		{
			throw new OrderException("Product doesn't exsists.");
		}
		products.remove(product);
		calculateTotalPrice();
	}

	public void emptyShoppingCart(final List<Product> products)
	{
		products.removeAll(products);
		calculateTotalPrice();
	}

	public void calculateOrderPrice(){
        double total = 0.0;
        for(OrderRow orderRow : orderRows){
            total += orderRow.getItem().getPrice() * orderRow.getQuantity();
        }
        this.orderTotal = total;
    }
	 */
	@Override
	public String getId()
	{
		return orderId;
	}

	@Override
	public void setId(final String orderId)
	{
		this.orderId = orderId;
	}

	
	public User getShopperUserName()
	{
		return shopper.getUsername();
	}

	public boolean isPayed()
	{
		return isPayed;
	}
	
	/*public void pay() throws PaymentException
	{
		isPayed = true;
		for (Product product : shoppingCart.getProducts())
		{
			product.setStockQuantity(product.getStockQuantity() - product.getOrderQuantity());
		}
	}*/

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (this instanceof Order) {
			Order otherOrder = (Order) other;
			return orderId.equals(otherOrder.getId()) && shopper.getId().equals(otherOrder.getOrderShopper().getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result += 17 * orderId.hashCode();
		result += 17 * shopper.getId().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Order: " + "orderId: " + orderId + ", shopper " + shopper + ", orderTotal: " + orderTotal;
	}
}
