package se.grouprich.webshop.main;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import se.grouprich.webshop.exception.PaymentException;
import se.grouprich.webshop.exception.PermissionException;
import se.grouprich.webshop.exception.ProductRegistrationException;
import se.grouprich.webshop.exception.UserRegistrationException;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.OrderRow;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.repository.JpaOrderRepository;
import se.grouprich.webshop.repository.JpaProductRepository;
import se.grouprich.webshop.repository.JpaUserRepository;
import se.grouprich.webshop.repository.OrderRepository;
import se.grouprich.webshop.repository.ProductRepository;
import se.grouprich.webshop.service.ECommerceService;
import se.grouprich.webshop.service.validation.ProductValidator;
import se.grouprich.webshop.service.validation.UserValidator;

public final class Main
{
	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");

	public static final void main(String[] args) throws ProductRegistrationException, UserRegistrationException, PaymentException, PermissionException
	{
		ProductRepository productRepository = new JpaProductRepository(factory);
		Product product1 = new Product("pen", 10.33, 5, "In Stock");
		Product product2 = new Product("notebook", 10.99, 5, "Ordered");
		Product product3 = new Product("notepad", 10.25, 10, "In Stock");
		productRepository.saveOrUpdate(product1);
		productRepository.saveOrUpdate(product2);
		productRepository.saveOrUpdate(product3);

		OrderRepository orderRepository = new JpaOrderRepository(factory);
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

		List<User> fetchedUsersByUsername = userRepository.fetchUsersByUsername("Eskimooo");
		System.out.println();
		System.out.println("User fetched by username:");
		System.out.println(fetchedUsersByUsername);

		User userFoundById = userRepository.findById(4L);
		System.out.println();
		System.out.println("User found by id:");
		System.out.println(userFoundById);

		user.setStatus("Activated");
		userRepository.saveOrUpdate(user);
		System.out.println();
		System.out.println("After update:");
		System.out.println(user);

		ProductValidator productValidator = new ProductValidator(productRepository);
		UserValidator userValidator = new UserValidator(userRepository);
		ECommerceService eCommerceService = new ECommerceService(orderRepository, userRepository, productRepository, productValidator,
				userValidator);
		
		Order orderFetchedById = eCommerceService.fetchOrderById(4L);
		System.out.println();
		System.out.println("Order Fetched By ID:");
		System.out.println(orderFetchedById);
		
		Product productFetchedById = eCommerceService.fetchProductById(3L);
		System.out.println();
		System.out.println("Product Fetched By ID:");
		System.out.println(productFetchedById);
		
		User userFetchedById = eCommerceService.fetchUserById(5L);
		System.out.println();
		System.out.println("User Fetched By ID:");
		System.out.println(userFetchedById);
		
		Order createdOrder = eCommerceService.createOrder(order);
		System.out.println();
		System.out.println("Created Order:");
		System.out.println(createdOrder);
		
		productRepository.saveOrUpdate(product1);
		List<Product> fetchedProductsByProductName = productRepository.fetchProductsByProductName("pen");
		System.out.println(fetchedProductsByProductName);
		boolean alreadyExists = productValidator.alreadyExists("Tea cup");
		System.out.println(alreadyExists);
		Product product4 = eCommerceService.createProduct("Tea cup", 30.99, 10, "In Stock");
		System.out.println();
		System.out.println("Created Product:");
		System.out.println(product4);
		
		User user2 = eCommerceService.createUser("Mari", "12&OI4", "Mariko", "Hashimoto");
		user2.setRole("Admin");
		user2.setStatus("ACTIVATED");
		System.out.println();
		System.out.println("Created User:");
		System.out.println(user2);
		
		createdOrder.setStatus("Shipped");
		Order updatedOrder = eCommerceService.updateOrder(createdOrder, user2);
		System.out.println();
		System.out.println("Updated Order:");
		System.out.println(updatedOrder);
		
		product4.setProductName("Ittala tea cup");
		Product updatedProduct = eCommerceService.updateProduct(product4, user2);
		System.out.println();
		System.out.println("Updated Product:");
		System.out.println(updatedProduct);
		
		user2.setPassword("55Y?kk");
		User updatedUser = eCommerceService.updateUser(user2, "12&OI4");
		System.out.println();
		System.out.println("Updated User:");
		System.out.println(updatedUser);
		
		List<Product> searchResult = eCommerceService.searchProductsBasedOnProductName("note");
		System.out.println();
		System.out.println("Search result:");
		System.out.println(searchResult);
		
		User fetchedUser = eCommerceService.fetchUserByUsername("Mari");
		System.out.println();
		System.out.println("User fetched by username:");
		System.out.println(fetchedUser);
		
		OrderRow orderRow4 = new OrderRow(product4, 6);
		Order order2 = new Order(user).addOrderRow(orderRow4);
		Order createdOrder2 = eCommerceService.createOrder(order2);
		System.out.println();
		System.out.println("Created order2:");
		System.out.println(createdOrder2);
		
		List<Order> ordersFetchedByUser = eCommerceService.fetchOrdersByUser(user);
		System.out.println();
		System.out.println("Orders Fetched By User:");
		System.out.println(ordersFetchedByUser);
		
		List<Order> ordersByminimValue = eCommerceService.fetchOrdersByMinimumValue(100.00);
		List<Order> allOrdersInDB = eCommerceService.fetchAllOrders();
		System.out.println();
		System.out.println("All Orders");
		System.out.println(allOrdersInDB);
		System.out.println("Orders by minimum value");
		System.out.println(ordersByminimValue);
	}
}
