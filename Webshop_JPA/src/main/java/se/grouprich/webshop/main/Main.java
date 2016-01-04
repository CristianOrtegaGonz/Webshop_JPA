package se.grouprich.webshop.main;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import se.grouprich.webshop.exception.ProductRegistrationException;
import se.grouprich.webshop.exception.UserRegistrationException;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.OrderRow;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.repository.JpaOrderRepository;
import se.grouprich.webshop.repository.JpaProductRepository;
import se.grouprich.webshop.repository.JpaUserRepository;

public final class Main
{
	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");

	public static final void main(String[] args) throws ProductRegistrationException, UserRegistrationException
	{
		JpaProductRepository productRepository = new JpaProductRepository(factory);
		Product product1 = new Product("pen", 10.33, 5, "In Stock");
		Product product2 = new Product("notebook", 10.99, 5, "Ordered");
		productRepository.saveOrUpdate(product1);
		productRepository.saveOrUpdate(product2);
		
		List<Product> allProducts = productRepository.fetchAll();
		System.out.println(allProducts);
		
		List<Product> fetchedProducts = productRepository.fetchProductsByProductName("pen");
		for (Product fetchedProduct : fetchedProducts)
		{
			System.out.println(fetchedProduct);
		}
		
		Product productFoundById = productRepository.findById(2L);
		System.out.println(productFoundById);
		
		product2.setStatus("In Stock");
		Product productUpdated = productRepository.saveOrUpdate(product2);
		System.out.println(productUpdated);
		
		JpaOrderRepository orderRepository = new JpaOrderRepository(factory);
		User user = new User("Eskimooo", "55I!", "Eski", "Mooo");
		Order order = new Order(user);
		OrderRow orderRow = new OrderRow(product1, 2);
		OrderRow orderRow2 = new OrderRow(product2, 2);
		OrderRow orderRow3 = new OrderRow(product2, 1);
		order.addOrderRow(orderRow).addOrderRow(orderRow2);
		System.out.println(orderRow);
		orderRepository.saveOrUpdate(order);
		
		List<Order> allOrders = orderRepository.fetchAll();
		System.out.println();
		System.out.println("All Orders:");
		System.out.println(allOrders);
		
		List<Order> ordersByMinimumValue = orderRepository.fetchOrdersByMinimumValue(50.00);
		System.out.println();
		System.out.println("Orders By Minimum Value:");
		System.out.println(ordersByMinimumValue);
		
		List<Order> ordersByStatus = orderRepository.fetchOrdersByStatus("Placed");
		System.out.println();
		System.out.println("Orders By Status:");
		System.out.println(ordersByStatus);
		
		Order orderFoundById = orderRepository.findById(3L);
		System.out.println();
		System.out.println("Order Found By ID:");
		System.out.println(orderFoundById);
		
		order.addOrderRow(orderRow3);
		orderRepository.saveOrUpdate(order);
		System.out.println();
		System.out.println("After update:");
		System.out.println(order);
		
		JpaUserRepository userRepository = new JpaUserRepository(factory);
		List<User> allUsers = userRepository.fetchAll();
		System.out.println();
		System.out.println("All users:");
		System.out.println(allUsers);
		
		User userFetchedByUsername = userRepository.fetchUserByUsername("Eskimooo");
		System.out.println();
		System.out.println("User fetched by username:");
		System.out.println(userFetchedByUsername);
		
		User userFoundById = userRepository.findById(4L);
		System.out.println();
		System.out.println("User found by id:");
		System.out.println(userFoundById);
		
		user.setStatus("Activated");
		userRepository.saveOrUpdate(user);
		System.out.println();
		System.out.println("After update:");
		System.out.println(user);
	}
}
