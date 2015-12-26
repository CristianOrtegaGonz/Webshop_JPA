package se.grouprich.webshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.PaymentException;
import se.grouprich.webshop.idgenerator.Identifiable;
import sun.plugin2.main.client.ProcessUI;

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
		isPayed = false;
	}

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public User getOrderShopper() {
        return this.shopper;
    }

    public List<OrderRow> getOrderRows() {
        return new ArrayList<>(orderRows);
    }



	public void calculateTotalPrice()
	{
		for (OrderRow orderrow : orderRows)
		{
			orderTotal += (orderrow.getItem().getPrice() * orderrow.getQuantity());
		}
	}


	public void addToOrder(final Product product, final int quantity) throws OrderException {
      orderRows.add(new OrderRow(product, quantity));
      calculateTotalPrice();
    }

    public void addToOrder(final Product product) throws OrderException {
        orderRows.add(new OrderRow(product));
		calculateOrderPrice();
    }

    public void addToOrder(final List<OrderRow> orderRows) throws OrderException {
		orderRows.addAll(orderRows);
		calculateOrderPrice();
    }

	private void deleteProduct (final Product product) throws OrderException {
		for (int i = 0; i < orderRows.size(); i ++) {
			if (orderRows.get(i).getItem().equals(product)) {
				if (orderRows.get(i).getQuantity() == 1) {
					orderRows.remove(i);
					calculateOrderPrice();
				}

				if (orderRows.get(i).getQuantity() > 1) {
					orderRows.get(i).decreaseQuantity();
					calculateOrderPrice();
				}
			}
		}
		throw new OrderException("Product doesn't exsists.");

	}

	public void emptyShoppingCart(final List<OrderRow> orderRows)
	{
		orderRows.removeAll(orderRows);
		orderTotal = 0.0;
	}

	public void calculateOrderPrice(){
        double total = 0.0;
        for(OrderRow orderRow : orderRows){
            total += orderRow.getItem().getPrice() * orderRow.getQuantity();
        }
        this.orderTotal = total;
    }

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

	
	public User getShopper()
	{
		return shopper;
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
